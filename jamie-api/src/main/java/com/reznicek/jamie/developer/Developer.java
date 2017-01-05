package com.reznicek.jamie.developer;

import java.io.Serializable;
import java.util.List;

import com.reznicek.jamie.liking.Liking;

public class Developer implements Serializable {

	private static final long serialVersionUID = -6914475369894663148L;

	private Integer id;
	private String name;
	private boolean usuallyJoins;
	private List<Liking> likings;

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

	public List<Liking> getLikings() {
		return likings;
	}

	public void setLikings(List<Liking> likings) {
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
		Developer other = (Developer) obj;
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
		return "Developer [id=" + id + ", name=" + name + ", usuallyJoins=" + usuallyJoins + "]";
	}
}
