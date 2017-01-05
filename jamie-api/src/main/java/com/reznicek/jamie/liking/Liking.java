package com.reznicek.jamie.liking;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reznicek.jamie.developer.Developer;
import com.reznicek.jamie.restaurant.Restaurant;

public class Liking implements Serializable {

	private static final long serialVersionUID = 1666149783802491335L;

	private Integer id;
	@JsonIgnore
	private Developer developer;
	private Restaurant restaurant;
	private int value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Developer getDeveloper() {
		return developer;
	}

	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((developer == null) ? 0 : developer.hashCode());
		result = (prime * result) + ((id == null) ? 0 : id.hashCode());
		result = (prime * result) + ((restaurant == null) ? 0 : restaurant.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Liking other = (Liking) obj;
		if (developer == null) {
			if (other.developer != null) {
				return false;
			}
		} else if (!developer.equals(other.developer)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (restaurant == null) {
			if (other.restaurant != null) {
				return false;
			}
		} else if (!restaurant.equals(other.restaurant)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Liking [id=" + id + ", developer=" + developer + ", restaurant=" + restaurant + ", value=" + value
				+ "]";
	}
}
