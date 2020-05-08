package com.stackO.capmatch.Models;

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
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class Interest implements Comparable<Interest> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int interestId;
	
	@Column(unique=true, nullable=false)
	private final String name;
	
	private final String description;	
	
	@ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.REFRESH})
	private InterestCategory category;
	
	@ManyToMany(mappedBy="interests", fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.REFRESH})			// Must always be lazy
	private Set<User> interestedUsers = new TreeSet<>();
	
	/**
	 * This method is created to set the category in the bidirectional way for consistency. The regular setCategory should be used
	 * only if the intent is to set the category of this interest without updating the interest listing of the Category
	 * @param category
	 */
	public void bidirectionalSetCategory(InterestCategory category) {
		if (category != null) {
			this.category = category;
			category.addInterest(this);
		}
	}
	
	public void addInterestedUser(User user) {
		if (user != null) {
			this.interestedUsers.add(user);
			user.getInterests().add(this);
		}
	}
	
	public void removeInterestedUser(User user) {
		if (user != null) {
			this.interestedUsers.remove(user);
			user.getInterests().remove(this);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Interest))
			return false;
		Interest other = (Interest) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (interestId != other.interestId)
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
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + interestId;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	

	@Override
	public int compareTo(Interest interest) {
		if (interest == null)
			return -1;
		return this.name.compareToIgnoreCase(interest.getName());
	}

}
