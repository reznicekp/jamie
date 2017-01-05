package com.reznicek.jamie.weatherforecast;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hour implements Serializable {

	private static final long serialVersionUID = 4851113136844162519L;

	@JsonProperty(value = "dt")
	private Long forecastedTimestamp;
	private MainData main;
	private List<WeatherData> weather;
	private CloudsData clouds;
	private WindData wind;
	private PrecipitationData rain;
	private PrecipitationData snow;
	@JsonProperty(value = "dt_txt")
	private String calculationTimestamp;

	public Long getForecastedTimestamp() {
		return forecastedTimestamp;
	}

	public void setForecastedTimestamp(Long forecastedTimestamp) {
		this.forecastedTimestamp = forecastedTimestamp;
	}

	public MainData getMain() {
		return main;
	}

	public void setMain(MainData main) {
		this.main = main;
	}

	public List<WeatherData> getWeather() {
		return weather;
	}

	public void setWeather(List<WeatherData> weather) {
		this.weather = weather;
	}

	public CloudsData getClouds() {
		return clouds;
	}

	public void setClouds(CloudsData clouds) {
		this.clouds = clouds;
	}

	public WindData getWind() {
		return wind;
	}

	public void setWind(WindData wind) {
		this.wind = wind;
	}

	public PrecipitationData getRain() {
		return rain;
	}

	public void setRain(PrecipitationData rain) {
		this.rain = rain;
	}

	public PrecipitationData getSnow() {
		return snow;
	}

	public void setSnow(PrecipitationData snow) {
		this.snow = snow;
	}

	public String getCalculationTimestamp() {
		return calculationTimestamp;
	}

	public void setCalculationTimestamp(String calculationTimestamp) {
		this.calculationTimestamp = calculationTimestamp;
	}

	@Override
	public String toString() {
		return "Hour [forecastedTimestamp=" + forecastedTimestamp + ", main=" + main + ", weather=" + weather
				+ ", clouds=" + clouds + ", wind=" + wind + ", rain=" + rain + ", snow=" + snow
				+ ", calculationTimestamp=" + calculationTimestamp + "]";
	}
}
