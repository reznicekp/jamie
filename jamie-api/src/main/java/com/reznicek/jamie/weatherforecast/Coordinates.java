package com.reznicek.jamie.weatherforecast;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinates implements Serializable {

	private static final long serialVersionUID = -2835065637196917168L;

	@JsonProperty(value = "lon")
	private Double longitude;
	@JsonProperty(value = "lat")
	private Double latitude;

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "Coordinates [longitude=" + longitude + ", latitude=" + latitude + "]";
	}
}
