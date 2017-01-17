package com.reznicek.jamie.dailypreference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reznicek.jamie.developer.DeveloperDBO;
import com.reznicek.jamie.developer.DeveloperService;
import com.reznicek.jamie.history.HistoryDBO;
import com.reznicek.jamie.history.HistoryService;
import com.reznicek.jamie.liking.LikingDBO;
import com.reznicek.jamie.restaurant.RestaurantDBO;
import com.reznicek.jamie.weatherforecast.Hour;
import com.reznicek.jamie.weatherforecast.PrecipitationData;
import com.reznicek.jamie.weatherforecast.WeatherForecastService;

@Service
public class DailyPreferenceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DailyPreferenceService.class);

	private final int CHECKED_DAYS_IN_HISTORY = 8;
	private final int MIN_COMFORTABLE_TEMPERATURE = 20;
	private final int MAX_COMFORTABLE_TEMPERATURE = 25;

	@Autowired
	private DeveloperService developerService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private WeatherForecastService weatherForecastService;

	/**
	 * Main calculation method called from the controller.
	 */
	public List<LikingDBO> calculateDailyPreferences(List<String> developersIds) {
		List<DeveloperDBO> developers = findJoiningDevelopers(developersIds);

		if (developers.size() == 0) {
			return new ArrayList<>();
		}

		initializeScore(developers);
		adjustScoreByHistory(developers);
		adjustScoreByWeatherForecast(developers);
		convertScore(developers);

		return developers.get(0).getLikings().stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
		// replaced by lambda
		// Collections.sort(developers.get(0).getLikings(), Collections.reverseOrder());
		// Collections.reverse(developers.get(0).getLikings());
		// return developers.get(0).getLikings();
	}

	/**
	 * Finds developers (as whole objects) who go for lunch today.
	 */
	private List<DeveloperDBO> findJoiningDevelopers(List<String> developersIds) {
		Set<Integer> ids = new HashSet<>();

		developersIds.forEach(id -> {
			if (StringUtils.isNumeric(id.trim())) {
				ids.add(Integer.valueOf(id.trim()));
			} else {
				throw new IllegalArgumentException("Some of developers ids is not numeric: " + id);
			}
		});
		return developerService.findAllDevelopersById(ids);
	}

	/**
	 * Initialises score of all restaurants by summing likings of all developers who go for lunch today.
	 *
	 * @param developers
	 *            Developers who are going for lunch today
	 */
	private void initializeScore(List<DeveloperDBO> developers) {
		for (DeveloperDBO developer : developers) {
			for (LikingDBO liking : developer.getLikings()) {
				double newScore;
				if (liking.getValue() == -2) {
					// if somebody strongly dislikes a restaurant it is strongly suppressed
					newScore = liking.getRestaurant().getScore() + (10 * liking.getValue());
				} else {
					newScore = liking.getRestaurant().getScore() + liking.getValue();
				}
				liking.getRestaurant().setScore(newScore);
			}
		}
		printScore("Score initialized", developers);
	}

	private void adjustScoreByHistory(List<DeveloperDBO> developers) {
		for (int i = CHECKED_DAYS_IN_HISTORY; i > 0; i--) {
			adjustScoreByDayOfHistory(developers, i);
			printScore("Score adjusted by " + i + ". day of history", developers);
		}
	}

	/**
	 * Decreases the score of restaurant from the day of history (e.g. yesterday) for all developers who went that day
	 * and also today. More the day of history is far more points are taken from the score for given restaurant.
	 *
	 * @param developers
	 *            Developers who are going for lunch today
	 * @param amountOfDaysAgo
	 *            For yesterday this value is 1, the day before yesterday 2, the day before 3, ...
	 */
	private void adjustScoreByDayOfHistory(List<DeveloperDBO> developers, int amountOfDaysAgo) {
		LocalDate date = LocalDate.now().minusDays(amountOfDaysAgo);
		HistoryDBO history = historyService.findHistoryByDate(date);

		if (history == null) {
			return;
		}

		// find out how many developers went in the day of history (e.g. yesterday) and go also today
		List<DeveloperDBO> historyDevelopers = history.getDevelopers();
		long amountOfCommonDevelopers = developers.stream().filter(developer -> historyDevelopers.contains(developer))
				.count();

		// find restaurant from the day of history (e.g. yesterday) in the list of developers and decrease its score
		RestaurantDBO historyRestaurant = developers.get(0).getLikings().stream()
				.filter(liking -> history.getRestaurant().equals(liking.getRestaurant()))
				.findFirst().get().getRestaurant();
		historyRestaurant.setScore(historyRestaurant.getScore()
				- (amountOfCommonDevelopers * ((CHECKED_DAYS_IN_HISTORY - amountOfDaysAgo) + 1)));
		// replaced by lambda
		// for (LikingDBO liking : developers.get(0).getLikings()) {
		// if (history.getRestaurant().equals(liking.getRestaurant())) {
		// liking.getRestaurant().setScore(liking.getRestaurant().getScore()
		// - (amountOfCommonDevelopers * ((CHECKED_DAYS_IN_HISTORY - amountOfDaysAgo) + 1)));
		// break;
		// }
		// }
	}

	/**
	 * Increases or decreases the score of restaurants depending on weather forecast.
	 *
	 * @param developers
	 *            Developers who are going for lunch today
	 */
	private void adjustScoreByWeatherForecast(List<DeveloperDBO> developers) {
		Hour weatherForecastForNow = weatherForecastService.fetchWeatherForecastForNow();
		Hour weatherForecastForTomorrow = weatherForecastService.fetchWeatherForecastForTomorrow();

		if ((weatherForecastForNow != null) && (weatherForecastForTomorrow != null)) {
			adjustScoreByRainForecast(developers, weatherForecastForNow, weatherForecastForTomorrow);
			printScore("Score adjusted by rain forecast", developers);
			adjustScoreBySnowForecast(developers, weatherForecastForNow, weatherForecastForTomorrow);
			printScore("Score adjusted by snow forecast", developers);
			adjustScoreByTemperatureForecast(developers, weatherForecastForNow, weatherForecastForTomorrow);
			printScore("Score adjusted by temperature forecast", developers);
		}
	}

	/**
	 * Increases or decreases the score of restaurants depending on rain forecast. If today is less rain then tomorrow
	 * more distant restaurants are preferred, if today is more rain then tomorrow closer restaurants are preferred.
	 *
	 * @param developers
	 *            Developers who are going for lunch today
	 * @param weatherForecastForNow
	 *            Current weather conditions
	 * @param weatherForecastForTomorrow
	 *            Weather forecast for tomorrow's noon
	 */
	private void adjustScoreByRainForecast(List<DeveloperDBO> developers, Hour weatherForecastForNow,
			Hour weatherForecastForTomorrow) {
		PrecipitationData rainNow = weatherForecastForNow.getRain();
		if ((rainNow == null) || (rainNow.getThreeHours() == null)) {
			rainNow = new PrecipitationData();
			rainNow.setThreeHours(0D);
		}
		PrecipitationData rainTomorrow = weatherForecastForTomorrow.getRain();
		if ((rainTomorrow == null) || (rainTomorrow.getThreeHours() == null)) {
			rainTomorrow = new PrecipitationData();
			rainTomorrow.setThreeHours(0D);
		}
		LOGGER.info("Rain: now " + rainNow.getThreeHours() + ", tommorow " + rainTomorrow.getThreeHours());

		adjustScore(developers, rainTomorrow.getThreeHours() - rainNow.getThreeHours(), 0.1, "rain");
	}

	/**
	 * Increases or decreases the score of restaurants depending on snow forecast. If today is less snowing then
	 * tomorrow
	 * more distant restaurants are preferred, if today is more snowing then tomorrow closer restaurants are preferred.
	 *
	 * @param developers
	 *            Developers who are going for lunch today
	 * @param weatherForecastForNow
	 *            Current weather conditions
	 * @param weatherForecastForTomorrow
	 *            Weather forecast for tomorrow's noon
	 */
	private void adjustScoreBySnowForecast(List<DeveloperDBO> developers, Hour weatherForecastForNow,
			Hour weatherForecastForTomorrow) {
		PrecipitationData snowNow = weatherForecastForNow.getSnow();
		if ((snowNow == null) || (snowNow.getThreeHours() == null)) {
			snowNow = new PrecipitationData();
			snowNow.setThreeHours(0D);
		}
		PrecipitationData snowTomorrow = weatherForecastForTomorrow.getSnow();
		if ((snowTomorrow == null) || (snowTomorrow.getThreeHours() == null)) {
			snowTomorrow = new PrecipitationData();
			snowTomorrow.setThreeHours(0D);
		}
		LOGGER.info("Snow: now " + snowNow.getThreeHours() + ", tommorow " + snowTomorrow.getThreeHours());

		adjustScore(developers, snowTomorrow.getThreeHours() - snowNow.getThreeHours(), 0.1, "snow");
	}

	/**
	 * Increases or decreases the score of restaurants depending on temperature forecast. If both current temperature
	 * and temperature tomorrow at noon are same score is not affected. If one of the temperatures is below comfortable
	 * range of temperatures the rule is valid that higher temperature is better. If one of the temperatures is above
	 * comfortable range of temperatures the rule is valid that lower temperature is better.
	 *
	 * @param developers
	 *            Developers who are going for lunch today
	 * @param weatherForecastForNow
	 *            Current weather conditions
	 * @param weatherForecastForTomorrow
	 *            Weather forecast for tomorrow's noon
	 */
	private void adjustScoreByTemperatureForecast(List<DeveloperDBO> developers, Hour weatherForecastForNow,
			Hour weatherForecastForTomorrow) {
		double temperatureNow = weatherForecastForNow.getMain().getTemperature();
		double temperatureTomorrow = weatherForecastForTomorrow.getMain().getTemperature();
		LOGGER.info("Temperature: now " + temperatureNow + ", tommorow " + temperatureTomorrow);

		boolean isTemperatureNowComfortable = (temperatureNow >= MIN_COMFORTABLE_TEMPERATURE)
				&& (temperatureNow <= MAX_COMFORTABLE_TEMPERATURE);
		boolean isTemperatureTomorrowComfortable = (temperatureTomorrow >= MIN_COMFORTABLE_TEMPERATURE)
				&& (temperatureTomorrow <= MAX_COMFORTABLE_TEMPERATURE);
		boolean isTemperatureNowCold = temperatureNow < MIN_COMFORTABLE_TEMPERATURE;
		boolean isTemperatureNowWarm = temperatureNow > MAX_COMFORTABLE_TEMPERATURE;
		boolean isTemperatureTomorrowCold = temperatureTomorrow < MIN_COMFORTABLE_TEMPERATURE;
		boolean isTemperatureTomorrowWarm = temperatureTomorrow > MAX_COMFORTABLE_TEMPERATURE;

		if (isTemperatureNowComfortable && isTemperatureTomorrowComfortable) {
			LOGGER.info(
					"Temperature doesn't affect score of restaurants because current temperature and temperature tomorrow are comfortable");
			return;
		} else if ((isTemperatureNowComfortable && isTemperatureTomorrowCold)
				|| (isTemperatureNowCold && isTemperatureTomorrowComfortable)
				|| (isTemperatureNowCold && isTemperatureTomorrowCold)
				|| (isTemperatureNowCold && isTemperatureTomorrowWarm)
				|| (isTemperatureNowWarm && isTemperatureTomorrowCold)) {
			// higher temperature better
			adjustScore(developers, temperatureNow - temperatureTomorrow, 0.1, "temperature");
		} else if ((isTemperatureNowComfortable && isTemperatureTomorrowWarm)
				|| (isTemperatureNowWarm && isTemperatureTomorrowComfortable)
				|| (isTemperatureNowWarm && isTemperatureTomorrowWarm)) {
			// lower temperature better
			adjustScore(developers, temperatureTomorrow - temperatureNow, 0.1, "temperature");
		}
	}

	private void adjustScore(List<DeveloperDBO> developers, double difference, double correction,
			String calculationSubject) {
		for (LikingDBO liking : developers.get(0).getLikings()) {
			Integer restaurantDistance = liking.getRestaurant().getDistance();
			if (restaurantDistance != null) {
				liking.getRestaurant().setScore(liking.getRestaurant().getScore()
						+ (developers.size() * difference * correction * restaurantDistance));
			} else {
				LOGGER.warn("Restaurant " + liking.getRestaurant().getName()
						+ " was excluded from " + calculationSubject + " calculation because distance is unknown");
			}
		}
	}

	/**
	 * Converts score of restaurants from point-representation to percentage-values.
	 * Point-representation can be negative or positive (e.g. from -15 to 10) but percentage-values should be always
	 * from 0 to 100.
	 *
	 * @param developers
	 *            Developers who are going for lunch today
	 */
	private void convertScore(List<DeveloperDBO> developers) {
		double minScore = developers.stream().findFirst().get().getLikings().stream()
				.mapToDouble(liking -> liking.getRestaurant().getScore())
				.min().getAsDouble();
		// replaced by lambda
		// Double minScore = Collections.min(developers.get(0).getLikings()).getRestaurant().getScore();
		for (LikingDBO liking : developers.get(0).getLikings()) {
			liking.getRestaurant().setScore(liking.getRestaurant().getScore() - minScore);
		}

		double maxScore = developers.stream().findFirst().get().getLikings().stream()
				.mapToDouble(liking -> liking.getRestaurant().getScore())
				.max().getAsDouble();
		// replaced by lambda
		// Double maxScore = Collections.max(developers.get(0).getLikings()).getRestaurant().getScore();
		for (LikingDBO liking : developers.get(0).getLikings()) {
			liking.getRestaurant().setScore(
					Long.valueOf(Math.round((liking.getRestaurant().getScore() * 100) / maxScore)).doubleValue());
		}
		printScore("Score converted into percentage values", developers);
	}

	/**
	 * For logging purposes. Prints the table of all developers, all restaurants, likings and score.
	 */
	private void printScore(String logHeader, List<DeveloperDBO> developers) {
		String logging = "\n" + logHeader + "\n\t\t";
		for (int i = 0; i < developers.size(); i++) {
			DeveloperDBO developer = developers.get(i);
			String loggingLine = "\n" + developer.getId() + " (" + developer.getName() + ")\t";
			for (LikingDBO liking : developer.getLikings()) {
				if (i == 0) {
					logging += liking.getRestaurant().getId() + " " + liking.getRestaurant().getName().substring(0, 3)
							+ "\t";
				}
				loggingLine += liking.getValue() + "\t";
			}
			logging += loggingLine;
			if (i == (developers.size() - 1)) {
				logging += "\n\t\t";
				for (LikingDBO liking : developer.getLikings()) {
					logging += liking.getRestaurant().getScore() + "\t";
				}
			}
		}
		LOGGER.info(logging);
	}
}
