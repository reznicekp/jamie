package com.reznicek.jamie.weatherforecast;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PrecipitationData implements Serializable {

	private static final long serialVersionUID = 123895091672740115L;

	@JsonProperty(value = "3h")
	private Double threeHours;

	public Double getThreeHours() {
		return threeHours;
	}

	public void setThreeHours(Double threeHours) {
		this.threeHours = threeHours;
	}

	@Override
	public String toString() {
		return "PrecipitationData [threeHours=" + threeHours + "]";
	}
}
