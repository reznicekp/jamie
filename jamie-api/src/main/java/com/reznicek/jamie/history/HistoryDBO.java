package com.reznicek.jamie.history;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.reznicek.jamie.developer.DeveloperDBO;
import com.reznicek.jamie.restaurant.RestaurantDBO;

@Entity
@Table(name = "HISTORY")
public class HistoryDBO implements Serializable {

	private static final long serialVersionUID = -221005674759896515L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "HST_ID")
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "HST_RES_ID")
	private RestaurantDBO restaurant;
	@Column(name = "HST_DATE")
	private LocalDate date;
	@ManyToMany
	@JoinTable(name = "HISTORY_DEVELOPER",
			joinColumns = { @JoinColumn(name = "HSD_HST_ID", referencedColumnName = "HST_ID") },
			inverseJoinColumns = { @JoinColumn(name = "HSD_DEV_ID", referencedColumnName = "DEV_ID") })
	private List<DeveloperDBO> developers;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RestaurantDBO getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(RestaurantDBO restaurant) {
		this.restaurant = restaurant;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<DeveloperDBO> getDevelopers() {
		return developers;
	}

	public void setDevelopers(List<DeveloperDBO> developers) {
		this.developers = developers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((date == null) ? 0 : date.hashCode());
		result = (prime * result) + ((id == null) ? 0 : id.hashCode());
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
		HistoryDBO other = (HistoryDBO) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "HistoryDBO [id=" + id + ", restaurant=" + restaurant + ", date=" + date + ", developers=" + developers
				+ "]";
	}
}
