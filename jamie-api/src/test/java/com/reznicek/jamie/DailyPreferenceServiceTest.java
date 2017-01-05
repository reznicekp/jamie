package com.reznicek.jamie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.reznicek.jamie.dailypreference.DailyPreferenceService;
import com.reznicek.jamie.developer.DeveloperDBO;
import com.reznicek.jamie.developer.DeveloperService;
import com.reznicek.jamie.history.HistoryDBO;
import com.reznicek.jamie.history.HistoryService;
import com.reznicek.jamie.liking.LikingDBO;
import com.reznicek.jamie.restaurant.RestaurantDBO;
import com.reznicek.jamie.weatherforecast.Hour;
import com.reznicek.jamie.weatherforecast.MainData;
import com.reznicek.jamie.weatherforecast.PrecipitationData;
import com.reznicek.jamie.weatherforecast.WeatherForecastService;

@RunWith(MockitoJUnitRunner.class)
public class DailyPreferenceServiceTest {

	private static final List<String> DEVELOPERS_IDS = Arrays.asList(new String[] { "1", "2", "3" });

	@InjectMocks
	private DailyPreferenceService classToTest;
	@Mock
	private DeveloperService developerService;
	@Mock
	private HistoryService historyService;
	@Mock
	private WeatherForecastService weatherForecastService;

	private RestaurantDBO restaurantA;
	private RestaurantDBO restaurantB;
	private RestaurantDBO restaurantC;
	private RestaurantDBO restaurantD;
	private DeveloperDBO developer1;
	private DeveloperDBO developer2;
	private DeveloperDBO developer3;
	private HistoryDBO history1DayAgo;
	private HistoryDBO history2DaysAgo;
	private HistoryDBO history3DaysAgo;
	private HistoryDBO history4DaysAgo;

	@Test
	@SuppressWarnings("unchecked")
	public void testNoDevelopersReturnsNoRestaurants() {
		// given
		when(developerService.findAllDevelopersById(anySet())).thenReturn(new ArrayList<>());

		// when
		List<LikingDBO> dailyPreferences = classToTest.calculateDailyPreferences(DEVELOPERS_IDS);

		// then
		assertNotNull(dailyPreferences);
		assertEquals(0, dailyPreferences.size());
	}

	@Test(expected = IllegalArgumentException.class)
	@SuppressWarnings("unchecked")
	public void testDeveloperIdTypo() {
		// given
		List<String> developersIds = Arrays.asList(new String[] { "1a", "2", "3" });

		// when
		classToTest.calculateDailyPreferences(developersIds);

		// then
		verify(developerService, never()).findAllDevelopersById(anySet());
	}

	@Test
	public void testInitializeScore() {
		// given
		createRestaurants();
		createDevelopers();

		mockJoiningDevelopers();
		when(historyService.findHistoryByDate(any(LocalDate.class))).thenReturn(null);
		when(weatherForecastService.fetchWeatherForecastForNow()).thenReturn(null);

		// when
		List<LikingDBO> dailyPreferences = classToTest.calculateDailyPreferences(DEVELOPERS_IDS);

		// then
		assertEquals(1, dailyPreferences.get(0).getRestaurant().getId().intValue());
		assertEquals(100, dailyPreferences.get(0).getRestaurant().getScore(), 0);
		assertEquals(33, dailyPreferences.get(1).getRestaurant().getScore(), 0);
		assertEquals(33, dailyPreferences.get(2).getRestaurant().getScore(), 0);
		assertEquals(3, dailyPreferences.get(3).getRestaurant().getId().intValue());
		assertEquals(0, dailyPreferences.get(3).getRestaurant().getScore(), 0);
	}

	@Test
	public void testAdjustScoreByHistory() {
		// given
		createRestaurants();
		createDevelopers();
		createHistory();

		mockJoiningDevelopers();
		mockHistory();
		when(weatherForecastService.fetchWeatherForecastForNow()).thenReturn(null);

		// when
		List<LikingDBO> dailyPreferences = classToTest.calculateDailyPreferences(DEVELOPERS_IDS);

		// then
		assertEquals(3, dailyPreferences.get(0).getRestaurant().getId().intValue());
		assertEquals(100, dailyPreferences.get(0).getRestaurant().getScore(), 0);
		assertEquals(4, dailyPreferences.get(1).getRestaurant().getId().intValue());
		assertEquals(86, dailyPreferences.get(1).getRestaurant().getScore(), 0);
		assertEquals(2, dailyPreferences.get(2).getRestaurant().getId().intValue());
		assertEquals(57, dailyPreferences.get(2).getRestaurant().getScore(), 0);
		assertEquals(1, dailyPreferences.get(3).getRestaurant().getId().intValue());
		assertEquals(0, dailyPreferences.get(3).getRestaurant().getScore(), 0);
	}

	@Test
	public void testAdjustScoreByRainForecastTodayIsBetter() {
		// given
		createRestaurants();
		createDevelopers();
		createHistory();

		mockJoiningDevelopers();
		mockHistory();
		mockWeatherForecast(0.5, 1.5, 0, 0, 22, 22);

		// when
		List<LikingDBO> dailyPreferences = classToTest.calculateDailyPreferences(DEVELOPERS_IDS);

		// then
		assertEquals(3, dailyPreferences.get(0).getRestaurant().getId().intValue());
		assertEquals(100, dailyPreferences.get(0).getRestaurant().getScore(), 0);
		assertEquals(1, dailyPreferences.get(1).getRestaurant().getId().intValue());
		assertEquals(38, dailyPreferences.get(1).getRestaurant().getScore(), 0);
		assertEquals(2, dailyPreferences.get(2).getRestaurant().getId().intValue());
		assertEquals(6, dailyPreferences.get(2).getRestaurant().getScore(), 0);
		assertEquals(4, dailyPreferences.get(3).getRestaurant().getId().intValue());
		assertEquals(0, dailyPreferences.get(3).getRestaurant().getScore(), 0);
	}

	@Test
	public void testAdjustScoreByRainForecastTomorrowIsBetter() {
		// given
		createRestaurants();
		createDevelopers();
		createHistory();

		mockJoiningDevelopers();
		mockHistory();
		mockWeatherForecast(1.5, 0.5, 0, 0, 22, 22);

		// when
		List<LikingDBO> dailyPreferences = classToTest.calculateDailyPreferences(DEVELOPERS_IDS);

		// then
		assertEquals(4, dailyPreferences.get(0).getRestaurant().getId().intValue());
		assertEquals(100, dailyPreferences.get(0).getRestaurant().getScore(), 0);
		assertEquals(2, dailyPreferences.get(1).getRestaurant().getId().intValue());
		assertEquals(72, dailyPreferences.get(1).getRestaurant().getScore(), 0);
		assertEquals(3, dailyPreferences.get(2).getRestaurant().getId().intValue());
		assertEquals(22, dailyPreferences.get(2).getRestaurant().getScore(), 0);
		assertEquals(1, dailyPreferences.get(3).getRestaurant().getId().intValue());
		assertEquals(0, dailyPreferences.get(3).getRestaurant().getScore(), 0);
	}

	@Test
	public void testAdjustScoreBySnowForecastTodayIsBetter() {
		// given
		createRestaurants();
		createDevelopers();
		createHistory();

		mockJoiningDevelopers();
		mockHistory();
		mockWeatherForecast(0, 0, 0.1, 0.25, 22, 22);

		// when
		List<LikingDBO> dailyPreferences = classToTest.calculateDailyPreferences(DEVELOPERS_IDS);

		// then
		assertEquals(3, dailyPreferences.get(0).getRestaurant().getId().intValue());
		assertEquals(100, dailyPreferences.get(0).getRestaurant().getScore(), 0);
		assertEquals(4, dailyPreferences.get(1).getRestaurant().getId().intValue());
		assertEquals(30, dailyPreferences.get(1).getRestaurant().getScore(), 0);
		assertEquals(2, dailyPreferences.get(2).getRestaurant().getId().intValue());
		assertEquals(16, dailyPreferences.get(2).getRestaurant().getScore(), 0);
		assertEquals(1, dailyPreferences.get(3).getRestaurant().getId().intValue());
		assertEquals(0, dailyPreferences.get(3).getRestaurant().getScore(), 0);
	}

	@Test
	public void testAdjustScoreBySnowForecastTomorrowIsBetter() {
		// given
		createRestaurants();
		createDevelopers();
		createHistory();

		mockJoiningDevelopers();
		mockHistory();
		mockWeatherForecast(0, 0, 0.25, 0.1, 22, 22);

		// when
		List<LikingDBO> dailyPreferences = classToTest.calculateDailyPreferences(DEVELOPERS_IDS);

		// then
		assertEquals(4, dailyPreferences.get(0).getRestaurant().getId().intValue());
		assertEquals(100, dailyPreferences.get(0).getRestaurant().getScore(), 0);
		assertEquals(2, dailyPreferences.get(1).getRestaurant().getId().intValue());
		assertEquals(70, dailyPreferences.get(1).getRestaurant().getScore(), 0);
		assertEquals(3, dailyPreferences.get(2).getRestaurant().getId().intValue());
		assertEquals(64, dailyPreferences.get(2).getRestaurant().getScore(), 0);
		assertEquals(1, dailyPreferences.get(3).getRestaurant().getId().intValue());
		assertEquals(0, dailyPreferences.get(3).getRestaurant().getScore(), 0);
	}

	@Test
	public void testAdjustScoreByTemperatureForecastColdTodayIsBetter() {
		// given
		createRestaurants();
		createDevelopers();
		createHistory();

		mockJoiningDevelopers();
		mockHistory();
		mockWeatherForecast(0, 0, 0, 0, 10, 5);

		// when
		List<LikingDBO> dailyPreferences = classToTest.calculateDailyPreferences(DEVELOPERS_IDS);

		// then
		assertEquals(3, dailyPreferences.get(0).getRestaurant().getId().intValue());
		assertEquals(100, dailyPreferences.get(0).getRestaurant().getScore(), 0);
		assertEquals(1, dailyPreferences.get(1).getRestaurant().getId().intValue());
		assertEquals(46, dailyPreferences.get(1).getRestaurant().getScore(), 0);
		assertEquals(2, dailyPreferences.get(2).getRestaurant().getId().intValue());
		assertEquals(9, dailyPreferences.get(2).getRestaurant().getScore(), 0);
		assertEquals(4, dailyPreferences.get(3).getRestaurant().getId().intValue());
		assertEquals(0, dailyPreferences.get(3).getRestaurant().getScore(), 0);
	}

	@Test
	public void testAdjustScoreByTemperatureForecastColdTomorrowIsBetter() {
		// given
		createRestaurants();
		createDevelopers();
		createHistory();

		mockJoiningDevelopers();
		mockHistory();
		mockWeatherForecast(0, 0, 0, 0, 5, 10);

		// when
		List<LikingDBO> dailyPreferences = classToTest.calculateDailyPreferences(DEVELOPERS_IDS);

		// then
		assertEquals(4, dailyPreferences.get(0).getRestaurant().getId().intValue());
		assertEquals(100, dailyPreferences.get(0).getRestaurant().getScore(), 0);
		assertEquals(2, dailyPreferences.get(1).getRestaurant().getId().intValue());
		assertEquals(73, dailyPreferences.get(1).getRestaurant().getScore(), 0);
		assertEquals(3, dailyPreferences.get(2).getRestaurant().getId().intValue());
		assertEquals(15, dailyPreferences.get(2).getRestaurant().getScore(), 0);
		assertEquals(1, dailyPreferences.get(3).getRestaurant().getId().intValue());
		assertEquals(0, dailyPreferences.get(3).getRestaurant().getScore(), 0);
	}

	@Test
	public void testAdjustScoreByTemperatureForecastWarmTodayIsBetter() {
		// given
		createRestaurants();
		createDevelopers();
		createHistory();

		mockJoiningDevelopers();
		mockHistory();
		mockWeatherForecast(0, 0, 0, 0, 30, 35);

		// when
		List<LikingDBO> dailyPreferences = classToTest.calculateDailyPreferences(DEVELOPERS_IDS);

		// then
		assertEquals(3, dailyPreferences.get(0).getRestaurant().getId().intValue());
		assertEquals(100, dailyPreferences.get(0).getRestaurant().getScore(), 0);
		assertEquals(1, dailyPreferences.get(1).getRestaurant().getId().intValue());
		assertEquals(46, dailyPreferences.get(1).getRestaurant().getScore(), 0);
		assertEquals(2, dailyPreferences.get(2).getRestaurant().getId().intValue());
		assertEquals(9, dailyPreferences.get(2).getRestaurant().getScore(), 0);
		assertEquals(4, dailyPreferences.get(3).getRestaurant().getId().intValue());
		assertEquals(0, dailyPreferences.get(3).getRestaurant().getScore(), 0);
	}

	@Test
	public void testAdjustScoreByTemperatureForecastWarmTomorrowIsBetter() {
		// given
		createRestaurants();
		createDevelopers();
		createHistory();

		mockJoiningDevelopers();
		mockHistory();
		mockWeatherForecast(0, 0, 0, 0, 35, 30);

		// when
		List<LikingDBO> dailyPreferences = classToTest.calculateDailyPreferences(DEVELOPERS_IDS);

		// then
		assertEquals(4, dailyPreferences.get(0).getRestaurant().getId().intValue());
		assertEquals(100, dailyPreferences.get(0).getRestaurant().getScore(), 0);
		assertEquals(2, dailyPreferences.get(1).getRestaurant().getId().intValue());
		assertEquals(73, dailyPreferences.get(1).getRestaurant().getScore(), 0);
		assertEquals(3, dailyPreferences.get(2).getRestaurant().getId().intValue());
		assertEquals(15, dailyPreferences.get(2).getRestaurant().getScore(), 0);
		assertEquals(1, dailyPreferences.get(3).getRestaurant().getId().intValue());
		assertEquals(0, dailyPreferences.get(3).getRestaurant().getScore(), 0);
	}

	@SuppressWarnings("unchecked")
	private void mockJoiningDevelopers() {
		ArrayList<DeveloperDBO> joiningDevelopers = new ArrayList<>();
		joiningDevelopers.add(developer1);
		joiningDevelopers.add(developer2);
		joiningDevelopers.add(developer3);
		when(developerService.findAllDevelopersById(anySet())).thenReturn(joiningDevelopers);
	}

	private void mockHistory() {
		when(historyService.findHistoryByDate(LocalDate.now().minusDays(1))).thenReturn(history1DayAgo);
		when(historyService.findHistoryByDate(LocalDate.now().minusDays(2))).thenReturn(history2DaysAgo);
		when(historyService.findHistoryByDate(LocalDate.now().minusDays(3))).thenReturn(history3DaysAgo);
		when(historyService.findHistoryByDate(LocalDate.now().minusDays(4))).thenReturn(history4DaysAgo);
	}

	private void mockWeatherForecast(double rainNow, double rainTomorrow, double snowNow, double snowTomorrow,
			double temperatureNow, double temperatureTomorrow) {
		Hour forecastNow = new Hour();
		MainData mainDataNow = new MainData();
		mainDataNow.setTemperature(temperatureNow);
		forecastNow.setMain(mainDataNow);
		PrecipitationData rainDataNow = new PrecipitationData();
		rainDataNow.setThreeHours(rainNow);
		forecastNow.setRain(rainDataNow);
		PrecipitationData snowDataNow = new PrecipitationData();
		snowDataNow.setThreeHours(snowNow);
		forecastNow.setSnow(snowDataNow);
		when(weatherForecastService.fetchWeatherForecastForNow()).thenReturn(forecastNow);

		Hour forecastTomorrow = new Hour();
		MainData mainDataTomorrow = new MainData();
		mainDataTomorrow.setTemperature(temperatureTomorrow);
		forecastTomorrow.setMain(mainDataTomorrow);
		PrecipitationData rainDataTomorrow = new PrecipitationData();
		rainDataTomorrow.setThreeHours(rainTomorrow);
		forecastTomorrow.setRain(rainDataTomorrow);
		PrecipitationData snowDataTomorrow = new PrecipitationData();
		snowDataTomorrow.setThreeHours(snowTomorrow);
		forecastTomorrow.setSnow(snowDataTomorrow);
		when(weatherForecastService.fetchWeatherForecastForTomorrow()).thenReturn(forecastTomorrow);
	}

	private void createRestaurants() {
		restaurantA = new RestaurantDBO();
		restaurantA.setId(1);
		restaurantA.setName("Mela");
		restaurantA.setDistance(5);
		restaurantB = new RestaurantDBO();
		restaurantB.setId(2);
		restaurantB.setName("Pizza");
		restaurantB.setDistance(2);
		restaurantC = new RestaurantDBO();
		restaurantC.setId(3);
		restaurantC.setName("Burger");
		restaurantC.setDistance(6);
		restaurantD = new RestaurantDBO();
		restaurantD.setId(4);
		restaurantD.setName("Mexican");
		restaurantD.setDistance(1);
	}

	private void createDevelopers() {
		developer1 = new DeveloperDBO();
		developer1.setId(1);
		developer1.setName("Jana");
		List<LikingDBO> likings1 = new ArrayList<>();
		LikingDBO liking1A = new LikingDBO();
		liking1A.setRestaurant(restaurantA);
		liking1A.setValue(1);
		likings1.add(liking1A);
		LikingDBO liking1B = new LikingDBO();
		liking1B.setRestaurant(restaurantB);
		liking1B.setValue(0);
		likings1.add(liking1B);
		LikingDBO liking1C = new LikingDBO();
		liking1C.setRestaurant(restaurantC);
		liking1C.setValue(0);
		likings1.add(liking1C);
		LikingDBO liking1D = new LikingDBO();
		liking1D.setRestaurant(restaurantD);
		liking1D.setValue(0);
		likings1.add(liking1D);
		developer1.setLikings(likings1);

		developer2 = new DeveloperDBO();
		developer2.setId(2);
		developer2.setName("Marco");
		List<LikingDBO> likings2 = new ArrayList<>();
		LikingDBO liking2A = new LikingDBO();
		liking2A.setRestaurant(restaurantA);
		liking2A.setValue(0);
		likings2.add(liking2A);
		LikingDBO liking2B = new LikingDBO();
		liking2B.setRestaurant(restaurantB);
		liking2B.setValue(-1);
		likings2.add(liking2B);
		LikingDBO liking2C = new LikingDBO();
		liking2C.setRestaurant(restaurantC);
		liking2C.setValue(0);
		likings2.add(liking2C);
		LikingDBO liking2D = new LikingDBO();
		liking2D.setRestaurant(restaurantD);
		liking2D.setValue(0);
		likings2.add(liking2D);
		developer2.setLikings(likings2);

		developer3 = new DeveloperDBO();
		developer3.setId(3);
		developer3.setName("Sukhmeet");
		List<LikingDBO> likings3 = new ArrayList<>();
		LikingDBO liking3A = new LikingDBO();
		liking3A.setRestaurant(restaurantA);
		liking3A.setValue(1);
		likings3.add(liking3A);
		LikingDBO liking3B = new LikingDBO();
		liking3B.setRestaurant(restaurantB);
		liking3B.setValue(1);
		likings3.add(liking3B);
		LikingDBO liking3C = new LikingDBO();
		liking3C.setRestaurant(restaurantC);
		liking3C.setValue(-1);
		likings3.add(liking3C);
		LikingDBO liking3D = new LikingDBO();
		liking3D.setRestaurant(restaurantD);
		liking3D.setValue(0);
		likings3.add(liking3D);
		developer3.setLikings(likings3);
	}

	private void createHistory() {
		history1DayAgo = new HistoryDBO();
		history1DayAgo.setRestaurant(restaurantA);
		history1DayAgo.setDevelopers(Arrays.asList(new DeveloperDBO[] { developer1, developer2, developer3 }));
		history2DaysAgo = new HistoryDBO();
		history2DaysAgo.setRestaurant(restaurantB);
		history2DaysAgo.setDevelopers(Arrays.asList(new DeveloperDBO[] { developer1, developer3 }));
		history3DaysAgo = new HistoryDBO();
		history3DaysAgo.setRestaurant(restaurantD);
		history3DaysAgo.setDevelopers(Arrays.asList(new DeveloperDBO[] { developer1, developer3 }));
		history4DaysAgo = new HistoryDBO();
		history4DaysAgo.setRestaurant(restaurantC);
		history4DaysAgo.setDevelopers(Arrays.asList(new DeveloperDBO[] { developer1, developer2 }));
	}
}
