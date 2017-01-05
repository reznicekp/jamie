package com.reznicek.jamie.weatherforecast;

import java.io.Serializable;

public class WeatherData implements Serializable {

	private static final long serialVersionUID = 7952667147247283491L;

	private String description;
	private String icon;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "WeatherData [description=" + description + ", icon=" + icon + "]";
	}
}
