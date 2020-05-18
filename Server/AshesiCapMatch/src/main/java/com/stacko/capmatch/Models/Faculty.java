package com.stacko.capmatch.Models;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class Faculty extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7656272807182708590L;	

	private int menteeLimit;
	
	@ManyToOne(cascade= {CascadeType.REFRESH}, optional=true)
	public Department department;
	
	@OneToMany(mappedBy="supervisor", fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REFRESH})
	private Set<Student> supervisedStudents = new TreeSet<>();
	
	
	
	/**
	 * Note that this relationship is unidirectional. Some extra effort will be needed to keep things consistent
	 * (for example, when a student is deleted, they will still be a favorite. Find a way to handle that)
	 */
	@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name="faculty_student",
				joinColumns = { @JoinColumn(name = "fk_faculty") },
				inverseJoinColumns = { @JoinColumn(name = "fk_student") })
    private Set<Student> favouriteStudents = new TreeSet<>();
	
	
	/**
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param password
	 * @param menteeLimit
	 */
	public Faculty(String firstname, String lastname, String email, String password, int menteeLimit) {
		super(firstname, lastname, email, password);
		this.menteeLimit = menteeLimit;
	}
	
	
	public void superviseStudent(Student student) {
		if (student != null) {
			this.supervisedStudents.add(student);
			student.setSupervisor(this);
		}
	}
	
	
	public void stopSupervisingStudent(Student student) {
		if (student != null) {
			if (student.getSupervisor().equals(this))
				student.setSupervisor(null);
			this.supervisedStudents.remove(student);
		}
	}
	
	
	public void leaveDepartment() {
		if (this.department != null) {
			department.getFaculty().remove(this);
			this.department = null;
		}
	}
	
	
	public void joinDepartment(Department department) {
		if (department != null) {
			leaveDepartment();
			department.getFaculty().add(this);
			this.department = department;
		}
	}
	
	
	public void addStudentFavourite(Student student) {
		if (student != null && !this.favouriteStudents.contains(student)) {
			this.favouriteStudents.add(student);
		}
	}
	
	public void removeStudentFavourite(Student student) {
		if (student != null && this.favouriteStudents.contains(student))
			this.favouriteStudents.remove(student);
	}
	
	
	public boolean isSupervisorFull() {
		return this.menteeLimit <= this.supervisedStudents.size();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Faculty))
			return false;
		Faculty other = (Faculty) obj;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		return true;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		return result;
	}
	
		

}
