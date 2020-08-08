package com.stacko.capmatch.Models.HATEOAS;

import lombok.Getter;

public class LoggedInUserModel extends UserModel {
	
	@Getter
	private long sessionTimeout;

	public LoggedInUserModel(UserModel model, long sessionTimeout) {
		super(null);			// Do nothing here		
		if (model == null) return;
		
		// Reassign all the variables
		this.userId = model.getUserId();
		this.firstname = model.getFirstname();
		this.lastname = model.getLastname();
		this.email = model.getEmail();
		this.bio = model.getBio();
		this.accountStatus = model.getAccountStatus();
		this.registrationDate = model.getRegistrationDate();
		this.roles = model.getRoles();
		this.department = model.getDepartment();
		this.major = model.getMajor();
		
		this.add(model.getLinks());
		
		this.sessionTimeout = sessionTimeout;
	}
}
