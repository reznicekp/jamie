package com.reznicek.jamie.weatherforecast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weatherforecast")
public class WeatherForecastController {

	@Autowired
	private WeatherForecastService weatherForecastService;

	@RequestMapping(value = "/now", method = RequestMethod.GET)
	public Hour fetchWeatherForecastForNow() {
		return weatherForecastService.fetchWeatherForecastForNow();
	}

	@RequestMapping(value = "/tomorrow", method = RequestMethod.GET)
	public Hour fetchWeatherForecastForTomorrow() {
		return weatherForecastService.fetchWeatherForecastForTomorrow();
	}
}
