package com.stacko.capmatch.Models;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@EqualsAndHashCode(callSuper=false)					// Play around with this a bit
public class Student extends User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8102568931312693344L;
	
	@ManyToOne(cascade= {CascadeType.REFRESH}, fetch= FetchType.EAGER, optional=true)
	private Major major;
	
	
	@ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.REFRESH}, optional=true)
	private Faculty supervisor;
	
	/**
	 * Note that this relationship is unidirectional. Some extra effort will be needed to keep things consistent
	 * (for example, when a faculty is deleted, they will still be a favorite. Find a way to handle that)
	 */
	@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name="student_faculty",
				joinColumns = { @JoinColumn(name = "fk_student") },
				inverseJoinColumns = { @JoinColumn(name = "fk_faculty") })
    private Set<Faculty> favouriteSupervisors = new TreeSet<>();
	
	
	/**
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param password
	 * @param major
	 */
	public Student(String firstname, String lastname, String email, String password, Major major) {
		super(firstname, lastname, email, password);
		this.major = major;
	}
	
	/**
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param password
	 */
	public Student(String firstname, String lastname, String email, String password) {
		super(firstname, lastname, email, password);
	}
	
	
	
	public void bidirectionalSetSupervisor(Faculty faculty) {
		if (faculty != null) {
			if (this.supervisor != null) {
				// remove from old supervisor's list
				this.supervisor.getSupervisedStudents().remove(this);
			}
			
			faculty.getSupervisedStudents().add(this);
			this.supervisor = faculty;
		}
	}
	
	
	public void removeSupervisor() {
		if (this.supervisor != null) {
			supervisor.getSupervisedStudents().remove(this);
			this.supervisor = null;
		}
	}
	
	
	public void bidirectionalSetMajor(Major major) {
		if (major != null) {
			if (this.major != null)
				this.major.getStudents().remove(this);		// remove student to maintain consistency
			this.major = major;
			major.getStudents().add(this);
		}
	}
	
	
	public void addFacultyFavourite(Faculty faculty) {
		if (faculty != null && !this.favouriteSupervisors.contains(faculty)) {
			this.favouriteSupervisors.add(faculty);
		}
	}
	
	public void removeFacultyFavourite(Faculty faculty) {
		if (faculty != null && this.favouriteSupervisors.contains(faculty)) {
			this.favouriteSupervisors.remove(faculty);
		}
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Student))
			return false;
		Student other = (Student) obj;
		if (major == null) {
			if (other.major != null)
				return false;
		} else if (!major.equals(other.major))
			return false;
		return true;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((major == null) ? 0 : major.hashCode());
		return result;
	}
	
	
	
	
	
	
	
//	public void removeMajor() {
//		if (this.major != null) {
//			this.major.getStudents().remove(this);
//			this.major = null;
//		}		
//	}			Commented out because Student always has major
	
}
