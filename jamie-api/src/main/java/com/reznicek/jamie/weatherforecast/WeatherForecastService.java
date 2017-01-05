package com.reznicek.jamie.weatherforecast;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherForecastService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherForecastService.class);

	private final String WEATHER_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast?id=2950159&cnt=20&units=metric&appid=474c0e17229efed626fd89b1e5d33c23";

	public Hour fetchWeatherForecastForNow() {
		WeatherForecast weatherForecast = fetchWeatherForecast();
		return weatherForecast != null ? weatherForecast.getHours().get(0) : null;
	}

	public Hour fetchWeatherForecastForTomorrow() {
		WeatherForecast weatherForecast = fetchWeatherForecast();
		return weatherForecast != null ? findWeatherForecastForTomorrow(weatherForecast) : null;
	}

	private WeatherForecast fetchWeatherForecast() {
		RestTemplate restTemplate = new RestTemplate();
		WeatherForecast weatherForecast = null;
		try {
			weatherForecast = restTemplate.getForObject(WEATHER_FORECAST_URL, WeatherForecast.class);
		} catch (RuntimeException e) {
			LOGGER.warn("Fetching weather forecast failed", e);
		}
		return weatherForecast;
	}

	private Hour findWeatherForecastForTomorrow(WeatherForecast weatherForecast) {
		for (Hour hour : weatherForecast.getHours()) {
			long tomorrowNoonTimestamp = LocalDateTime.now().plusDays(1).withHour(12).withMinute(0).withSecond(0)
					.withNano(0).toEpochSecond(ZoneOffset.UTC);
			if (hour.getForecastedTimestamp().equals(tomorrowNoonTimestamp)) {
				return hour;
			}
		}
		return null;
	}
}
