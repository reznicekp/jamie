package com.reznicek.jamie.weatherforecast;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CloudsData implements Serializable {

	private static final long serialVersionUID = 3933917542567052215L;

	@JsonProperty(value = "all")
	private Integer cloudiness;

	public Integer getCloudiness() {
		return cloudiness;
	}

	public void setCloudiness(Integer cloudiness) {
		this.cloudiness = cloudiness;
	}

	@Override
	public String toString() {
		return "CloudsData [cloudiness=" + cloudiness + "]";
	}
}
