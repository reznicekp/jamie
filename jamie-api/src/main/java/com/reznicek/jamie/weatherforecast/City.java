package com.reznicek.jamie.weatherforecast;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class City implements Serializable {

	private static final long serialVersionUID = 5214815773000607193L;

	private Integer id;
	private String name;
	@JsonProperty(value = "coord")
	private Coordinates coordinates;
	private String country;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", coordinates=" + coordinates + ", country=" + country + "]";
	}
}
