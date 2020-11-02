package com.stacko.capmatch.Models.HATEOAS;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.stacko.capmatch.Configuration.SpringContext;
import com.stacko.capmatch.Configuration.StorageConfig;
import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Models.User.AccountStatus;
import com.stacko.capmatch.Security.UserPermission;

import lombok.Getter;

@Relation(collectionRelation = "users", itemRelation = "user")
public class UserModel extends RepresentationModel<UserModel> implements Comparable<UserModel> {
	
	@Getter
	protected int userId;
	
	@Getter
	protected String firstname;
	
	@Getter
	protected String lastname;
	
	@Getter
	protected String email;
	
	@Getter
	protected String bio;
	
	@Getter
	protected AccountStatus accountStatus;
	
	@Getter
	protected Date registrationDate;
	
//	@Getter
//	private Set<UserPermissionModel> permissions;
	
	@Getter
	protected Set<User.Role> roles;
	
	@Getter
	protected String department;
	
	@Getter
	protected String major;
	
	@Getter
	protected String profilePhoto;
	
	@Getter
	protected String cv;
	
	
	public UserModel(User user) {
		if (user == null)
			return;
//			throw new IllegalArgumentException("Cannot construct a user model from 'null'");
		
		this.userId = user.getUserId();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.email = user.getEmail();
		this.accountStatus = user.getAccountStatus();
		this.registrationDate = user.getRegistrationDate();
		this.bio = user.getBio();
//		this.permissions = this.convertPermissionsToModels(user.getPermissions());
		this.roles = assignRoles(user.getPermissions());
		
		// Media Filenames
		this.profilePhoto = user.getProfilePhoto() == null ? 
								null : String.format("%s/%s/%s", getStorageConfig().getRemoteStorageBasepath(),
																getStorageConfig().getCVDirectory(),
																user.getProfilePhoto());
		this.cv = user.getCV() == null ?
					null : String.format("%s/%s/%s", getStorageConfig().getRemoteStorageBasepath(),
													getStorageConfig().getCVDirectory(),
													user.getCV());
		
		
		try {		// Try casting to student
			this.major = ((Student) user).getMajor().getName();
			this.department = ((Student) user).getMajor().getDepartment().getName();
		}catch (Exception e) {
			try { // if that fails, try casting to faculty
				this.department = ((Faculty) user).getDepartment().getName();
			}catch (Exception exc){
				// Do nothing
			}
		}		
	}
	
	
	private Set<User.Role> assignRoles(Set<UserPermission> permissions){
		if (permissions == null) return null;
		Set<User.Role> roles = new TreeSet<>();
		Iterator<UserPermission> iter = permissions.iterator();
		
		UserPermission currentPermission;
		while (iter.hasNext()) {
			currentPermission = iter.next();
			if (currentPermission.toString().equalsIgnoreCase("STUDENT"))
				roles.add(User.Role.STUDENT);
			else if (currentPermission.toString().equalsIgnoreCase("FACULTY"))
				roles.add(User.Role.FACULTY);
			if (currentPermission.toString().equalsIgnoreCase("ADMIN"))
				roles.add(User.Role.ADMIN);
		}		
		return roles;
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
		return true;
	}


	@Override
	public int compareTo(UserModel o) {
		if (o == null)
			return -20;
		return this.getEmail().compareTo(o.getEmail());
	}
	
	
	// ------------------------------------- Add helper methods to access Spring managed beans from this POJO ---------------------------------
	/**
	 * This method retrieves the StorageConfig Spring bean and makes it available in this POJO
	 * @return
	 */
	private StorageConfig getStorageConfig() {
        return SpringContext.getBean(StorageConfig.class);
    }
}
