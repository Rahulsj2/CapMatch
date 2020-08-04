package com.stacko.capmatch.Models.HATEOAS;

import org.springframework.hateoas.server.core.Relation;

import com.stacko.capmatch.Models.Faculty;

@Relation(collectionRelation = "faculties", itemRelation = "faculty")
public class FacultyModel extends UserModel{	
	
	public FacultyModel(Faculty faculty) {
		super(faculty);		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((accountStatus == null) ? 0 : accountStatus.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + userId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof FacultyModel))
			return false;
		FacultyModel other = (FacultyModel) obj;
		if (accountStatus != other.accountStatus)
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}	
}
