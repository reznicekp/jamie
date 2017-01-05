package com.reznicek.jamie.weatherforecast;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherForecast implements Serializable {

	private static final long serialVersionUID = 7852388869958419887L;

	private City city;
	@JsonProperty(value = "cnt")
	private Integer count;
	@JsonProperty(value = "list")
	private List<Hour> hours;

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<Hour> getHours() {
		return hours;
	}

	public void setHours(List<Hour> hours) {
		this.hours = hours;
	}

	@Override
	public String toString() {
		return "WeatherForecast [city=" + city + ", count=" + count + ", hours=" + hours + "]";
	}
}
