package com.stacko.capmatch.Models.HATEOAS;

import org.springframework.hateoas.RepresentationModel;

import com.stacko.capmatch.Security.UserPermission;

import lombok.Getter;

public class UserPermissionModel extends RepresentationModel<UserPermissionModel>  
								implements Comparable<UserPermissionModel>{
	@Getter
	private int id;
	
	@Getter
	private String name;
	
	
	public UserPermissionModel(UserPermission permission) {
		if (permission == null)
			throw new IllegalArgumentException("Cannot create UserPermisionModel from 'null'");
		
		this.id = permission.getPermissionId();
		this.name = permission.getName();
	}


	@Override
	public int compareTo(UserPermissionModel o) {
		if (this.getName() == null || o == null)
			return -21;
		return this.name.compareTo(o.getName());
	}
	
	
}
