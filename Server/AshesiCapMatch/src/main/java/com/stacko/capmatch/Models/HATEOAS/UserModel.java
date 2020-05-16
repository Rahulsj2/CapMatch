package com.stacko.capmatch.Models.HATEOAS;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.hateoas.RepresentationModel;

import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Models.User.AccountStatus;
import com.stacko.capmatch.Security.UserPermission;

import lombok.Getter;

public class UserModel extends RepresentationModel<UserModel> implements Comparable<UserModel> {
	
	@Getter
	private int userId;
	
	@Getter
	private String firstname;
	
	@Getter
	private String lastname;
	
	@Getter
	private String email;
	
	@Getter
	private String password;
	
	@Getter
	private String bio;
	
	@Getter
	private AccountStatus accountStatus;
	
	@Getter
	private Date registrationDate;
	
	@Getter
	private Set<UserPermissionModel> permissions;
	
	
	public UserModel(User user) {
		if (user == null)
			throw new IllegalArgumentException("Cannot construct a user model from 'null'");
		
		this.userId = user.getUserId();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.accountStatus = user.getAccountStatus();
		this.registrationDate = user.getRegistrationDate();
		this.bio = user.getBio();
		this.permissions = this.convertPermissionsToModels(user.getPermissions());		
	}
	
	
	private Set<UserPermissionModel> convertPermissionsToModels(Set<UserPermission> permissions){
		if (permissions == null)
			return null;
		Set<UserPermissionModel> set = new TreeSet<>();
		Iterator<UserPermission> iter = permissions.iterator();
		while (iter.hasNext()) {
			set.add(new UserPermissionModel(iter.next()));
		}		
		return set;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((accountStatus == null) ? 0 : accountStatus.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + userId;
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserModel other = (UserModel) obj;
		if (accountStatus != other.accountStatus)
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
		if (userId != other.userId)
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}


	@Override
	public int compareTo(UserModel o) {
		if (o == null)
			return -20;
		return this.getEmail().compareTo(o.getEmail());
	}
	
	

}
