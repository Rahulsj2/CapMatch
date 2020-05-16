package com.stacko.capmatch.Services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Service;

import com.stacko.capmatch.Controllers.UserController;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Models.HATEOAS.UserModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HATEOASService {
	
	/**
	 * 
	 * @param userModel
	 * @return
	 */
	public  void addUserInteractionLinks(UserModel model) {
		// Users should be able to add to their interests
		model.add(linkTo(methodOn(UserController.class).addInterests(null, model.getUserId())).withRel("addInterests"));
		model.add(linkTo(methodOn(UserController.class).addInterest(null, model.getUserId())).withRel("addInterest"));
		
		model.add(linkTo(methodOn(UserController.class).addSDGs(null, model.getUserId())).withRel("addSDGs"));
		model.add(linkTo(methodOn(UserController.class).addSDG(null, model.getUserId())).withRel("addSDG"));


		
		if (model.getAccountStatus().equals(User.AccountStatus.UNVERIFIED))
			model.add(linkTo(methodOn(UserController.class).resendConfirmationMail(model.getUserId())).withRel("sendConfirmation"));
	}
	
	
	public void addStudentInteractionLinks(UserModel model) {
		
	}
	
	
	public void addFacultyInteractionLinks(UserModel model) {
		// TODO Auto-generated method stub
				
	}

}
