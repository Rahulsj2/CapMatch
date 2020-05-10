package com.stacko.capmatch.Security;


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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;

import com.stacko.capmatch.Models.User;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Table(name = "permissions")
public class UserPermission implements GrantedAuthority, Comparable<UserPermission> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int permissionId;
	
	@Size(min=4)
	@Column(unique=true)
	private String name;
	
	@ManyToMany(mappedBy="permissions", fetch=FetchType.LAZY,
				cascade= {CascadeType.PERSIST, CascadeType.REFRESH})			// Must always be lazy
	private Set<User> users = new TreeSet<>();	

	// Add Constructor
	public UserPermission(String name) {
		if (name == null) throw new IllegalArgumentException("Permission name cannot be null");
		this.name = name.toUpperCase();
	}
	
	@Override
	public String getAuthority() {
		return this.name.toLowerCase();
	}
	
	@PrePersist
	private void makeNameUppercase() {
		if (this.name != null)
			this.name = name.toUpperCase();
	}
	
	
	public void addUser(User user) {
		if (user != null)
			this.users.add(user);
		user.getPermissions().add(this);
	}
	
	public void removeUser(User user) {
		if (user != null) {
			this.users.remove(user);
			user.getPermissions().remove(this);
		}
	}
	
	public int compareTo(UserPermission permission) {
		if (permission == null)
			return -1;
		return this.name.compareTo(permission.getName());
	}
	
	
	
	// ----------------------------- Hash Code and Equals ------------------------------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + permissionId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UserPermission))
			return false;
		UserPermission other = (UserPermission) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (permissionId != other.permissionId)
			return false;
		return true;
	}
	

	
}
