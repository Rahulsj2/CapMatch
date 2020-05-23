package com.stacko.capmatch.Models;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@RequiredArgsConstructor
public class SDG implements Comparable<SDG>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4111279920056519076L;

	@Id
	@Column(nullable=false, unique=true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false, unique=true)
	private final Integer number;
	
	@Column(nullable=false)
	private final String name;
	
	private final String description;
	
	@ManyToMany(mappedBy="SDGs", fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.REFRESH})			// Must always be lazy
	private Set<User> interestedUsers = new TreeSet<>();
	
	
	public void addInterestedUser(User user) {
		if (user != null) {
			this.interestedUsers.add(user);
			user.getSDGs().add(this);
		}
	}
	
	public void removeInterestedUser(User user) {
		if (user != null) {
			this.interestedUsers.remove(user);
			user.getSDGs().remove(this);
		}
	}
	
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SDG))
			return false;
		SDG other = (SDG) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public int compareTo(SDG SDG) {
		if (SDG == null)
			return -1;
		return this.name.compareToIgnoreCase(SDG.getName());
	}


}
