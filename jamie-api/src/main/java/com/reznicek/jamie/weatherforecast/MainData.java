package com.reznicek.jamie.weatherforecast;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MainData implements Serializable {

	private static final long serialVersionUID = -7584391210037329457L;

	@JsonProperty(value = "temp")
	private Double temperature;
	@JsonProperty(value = "temp_min")
	private Double temperatureMin;
	@JsonProperty(value = "temp_max")
	private Double temperatureMax;
	private Double pressure;
	@JsonProperty(value = "sea_level")
	private Double seaLevel;
	@JsonProperty(value = "grnd_level")
	private Double groundLevel;
	private Integer humidity;

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getTemperatureMin() {
		return temperatureMin;
	}

	public void setTemperatureMin(Double temperatureMin) {
		this.temperatureMin = temperatureMin;
	}

	public Double getTemperatureMax() {
		return temperatureMax;
	}

	public void setTemperatureMax(Double temperatureMax) {
		this.temperatureMax = temperatureMax;
	}

	public Double getPressure() {
		return pressure;
	}

	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}

	public Double getSeaLevel() {
		return seaLevel;
	}

	public void setSeaLevel(Double seaLevel) {
		this.seaLevel = seaLevel;
	}

	public Double getGroundLevel() {
		return groundLevel;
	}

	public void setGroundLevel(Double groundLevel) {
		this.groundLevel = groundLevel;
	}

	public Integer getHumidity() {
		return humidity;
	}

	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}

	@Override
	public String toString() {
		return "MainData [temperature=" + temperature + ", temperatureMin=" + temperatureMin + ", temperatureMax="
				+ temperatureMax + ", pressure=" + pressure + ", seaLevel=" + seaLevel + ", groundLevel=" + groundLevel
				+ ", humidity=" + humidity + "]";
	}
}
