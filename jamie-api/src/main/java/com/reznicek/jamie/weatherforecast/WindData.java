package com.reznicek.jamie.weatherforecast;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WindData implements Serializable {

	private static final long serialVersionUID = -2826250423990200865L;

	private Double speed;
	@JsonProperty(value = "deg")
	private Double direction;

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getDirection() {
		return direction;
	}

	public void setDirection(Double direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "WindData [speed=" + speed + ", direction=" + direction + "]";
	}
}
