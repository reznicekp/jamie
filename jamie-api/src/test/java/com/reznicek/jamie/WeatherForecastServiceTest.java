package com.reznicek.jamie;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.reznicek.jamie.weatherforecast.Hour;
import com.reznicek.jamie.weatherforecast.WeatherForecast;
import com.reznicek.jamie.weatherforecast.WeatherForecastService;

@RunWith(MockitoJUnitRunner.class)
public class WeatherForecastServiceTest {

	@InjectMocks
	private WeatherForecastService classToTest;

	@Mock
	private RestTemplate restTemplate;

	@Test
	public void testFetchWeatherForecastForTomorrow() {
		// given
		WeatherForecast weatherForecast = new WeatherForecast();
		when(restTemplate.getForObject(anyString(), eq(WeatherForecast.class))).thenReturn(weatherForecast);

		// when
		Hour weatherForecastForTomorrow = classToTest.fetchWeatherForecastForTomorrow();

		// then
		assertTrue(weatherForecastForTomorrow.getCalculationTimestamp().endsWith("12:00:00"));
	}
}
