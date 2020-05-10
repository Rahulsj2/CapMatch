package com.stacko.capmatch.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Data
public class Department {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int departmentId;
	
	@Column(unique=true, nullable=false)
	private final String departmentCode;
	
	private final String name;
	
	@OneToMany(mappedBy="department", cascade=CascadeType.ALL)
	private List<Major> majors = new ArrayList<>();
	
	@OneToMany(mappedBy="department", cascade={CascadeType.PERSIST, CascadeType.REFRESH})
	private Set<Faculty> faculty = new TreeSet<>();
	
	public void addMajor(Major major) {
		if (major != null) {
			major.setDepartment(this);
			//Avoid duplicates
			if (!this.getMajors().contains(major))
				this.majors.add(major);
		}
	}
	
	public void removeMajor(Major major) {
		if (major != null) {
			if (this.majors.contains(major))
				this.majors.remove(major);
		}
	}	
	
	public void addFaculty(Faculty faculty) {
		if (faculty != null) 
			faculty.joinDepartment(this);
	}
	
	/**
	 * The below method is written like so to make sure a department that
	 * deosn't include a faculty does not have to opportunity to set the 
	 * department of the faculty to null
	 * 
	 * @param faculty
	 */
	public void removeFaculty(Faculty faculty) {
		if (faculty != null) {
			if (this.faculty.contains(faculty)) {
				this.faculty.remove(faculty);
				faculty.setDepartment(null);
			}
		}
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Department))
			return false;
		Department other = (Department) obj;
		if (departmentCode == null) {
			if (other.departmentCode != null)
				return false;
		} else if (!departmentCode.equals(other.departmentCode))
			return false;
		if (departmentId != other.departmentId)
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
		result = prime * result + ((departmentCode == null) ? 0 : departmentCode.hashCode());
		result = prime * result + departmentId;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}	
}
