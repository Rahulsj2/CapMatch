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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class Major implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6695209665713622262L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int majorId;
	
	@Column(unique=true)
	private final String majorCode;
	
	private final String name;
	
	@ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.REFRESH}, fetch=FetchType.EAGER)
	@JoinColumn(name="department_id")
	private Department department;	
	
	@OneToMany(mappedBy="major", fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REFRESH})
	private Set<Student> students = new TreeSet<>();
	
	
	public void bidirectionalSetDepartement(Department department) {
		this.department = department;
		department.addMajor(this);
	}
	
	
	public void addStudent(Student student) {
		if (student != null) {
			student.setMajor(this);
			this.students.add(student);
		}
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Major))
			return false;
		Major other = (Major) obj;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (majorCode == null) {
			if (other.majorCode != null)
				return false;
		} else if (!majorCode.equals(other.majorCode))
			return false;
		if (majorId != other.majorId)
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
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((majorCode == null) ? 0 : majorCode.hashCode());
		result = prime * result + majorId;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
//	public void removeStudent(Student student) {
//		if (student != null) {
//			students.remove(student);
//			student.setMajor(null);
//		}
//	} Student major field not nullable, so leave control to student to switch majors
	
	
	

}
