package com.reznicek.jamie.liking;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.reznicek.jamie.developer.DeveloperDBO;
import com.reznicek.jamie.restaurant.RestaurantDBO;

@Entity
@Table(name = "LIKING")
public class LikingDBO implements Serializable, Comparable<LikingDBO> {

	private static final long serialVersionUID = 5904917941350659498L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LIK_ID")
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "LIK_DEV_ID")
	private DeveloperDBO developer;
	@ManyToOne
	@JoinColumn(name = "LIK_RES_ID")
	private RestaurantDBO restaurant;
	@Column(name = "LIK_VALUE")
	private int value;

	public LikingDBO() {
		super();
	}

	public LikingDBO(DeveloperDBO developer, RestaurantDBO restaurant, int value) {
		this.developer = developer;
		this.restaurant = restaurant;
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DeveloperDBO getDeveloper() {
		return developer;
	}

	public void setDeveloper(DeveloperDBO developer) {
		this.developer = developer;
	}

	public RestaurantDBO getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(RestaurantDBO restaurant) {
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
		LikingDBO other = (LikingDBO) obj;
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
		return "LikingDBO [id=" + id + ", restaurant=" + restaurant + ", value=" + value + "]";
	}

	@Override
	public int compareTo(LikingDBO liking) {
		if (restaurant.getScore() < liking.getRestaurant().getScore()) {
			return -1;
		} else if (restaurant.getScore() > liking.getRestaurant().getScore()) {
			return 1;
		} else {
			return 0;
		}
	}
}
