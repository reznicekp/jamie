package com.reznicek.jamie.developer;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.reznicek.jamie.liking.LikingDBO;

@Entity
@Table(name = "DEVELOPER")
public class DeveloperDBO implements Serializable {

	private static final long serialVersionUID = -1843102366864791245L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DEV_ID")
	private Integer id;
	@Column(name = "DEV_NAME")
	private String name;
	@Column(name = "DEV_USUALLY_JOINS")
	private boolean usuallyJoins;
	@OneToMany(mappedBy = "developer", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@OrderBy(value = "restaurant.id")
	private List<LikingDBO> likings;

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

	public boolean isUsuallyJoins() {
		return usuallyJoins;
	}

	public void setUsuallyJoins(boolean usuallyJoins) {
		this.usuallyJoins = usuallyJoins;
	}

	public List<LikingDBO> getLikings() {
		return likings;
	}

	public void setLikings(List<LikingDBO> likings) {
		this.likings = likings;
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
		DeveloperDBO other = (DeveloperDBO) obj;
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
		return "DeveloperDBO [id=" + id + ", name=" + name + ", usuallyJoins=" + usuallyJoins + ", likings="
				+ likings + "]";
	}
}
