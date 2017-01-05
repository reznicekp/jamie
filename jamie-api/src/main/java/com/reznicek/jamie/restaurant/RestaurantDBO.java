package com.reznicek.jamie.restaurant;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.reznicek.jamie.liking.LikingDBO;

@Entity
@Table(name = "RESTAURANT")
public class RestaurantDBO implements Serializable {

	private static final long serialVersionUID = -4449976601837098159L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RES_ID")
	private Integer id;
	@Column(name = "RES_NAME")
	private String name;
	@Column(name = "RES_DISTANCE")
	private Integer distance;
	@OneToMany(mappedBy = "restaurant", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private List<LikingDBO> likings;
	@Transient
	private Double score = 0D;

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

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public List<LikingDBO> getLikings() {
		return likings;
	}

	public void setLikings(List<LikingDBO> likings) {
		this.likings = likings;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((id == null) ? 0 : id.hashCode());
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
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
		RestaurantDBO other = (RestaurantDBO) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RestaurantDBO [id=" + id + ", name=" + name + ", distance=" + distance + ", score=" + score + "]";
	}
}
