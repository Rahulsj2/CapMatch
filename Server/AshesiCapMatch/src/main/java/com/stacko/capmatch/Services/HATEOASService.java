package com.stacko.capmatch.Services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Service;

import com.stacko.capmatch.Controllers.FacultyController;
import com.stacko.capmatch.Controllers.StudentController;
import com.stacko.capmatch.Controllers.UserController;
import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Models.HATEOAS.UserModel;


@Service
//@Slf4j
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
		model.add(linkTo(methodOn(UserController.class).setInterests(null, model.getUserId())).withRel("setInterests"));
		
		model.add(linkTo(methodOn(UserController.class).addSDGs(null, model.getUserId())).withRel("addSDGs"));
		model.add(linkTo(methodOn(UserController.class).addSDG(null, model.getUserId())).withRel("addSDG"));		
		model.add(linkTo(methodOn(UserController.class).setSDGs(null, model.getUserId())).withRel("setSDGs"));


		
		if (model.getAccountStatus().equals(User.AccountStatus.UNVERIFIED)) {
			model.add(linkTo(methodOn(UserController.class).resendConfirmationMail(model.getUserId())).withRel("sendConfirmation"));
			model.add(linkTo(methodOn(UserController.class).changeEmail(null, null)).withRel("changeEmail"));
		}
	}
	
	
	public void addStudentInteractionLinks(UserModel model) {
		if (model == null)
			return;
		model.add(linkTo(methodOn(StudentController.class).browseFaculty(null)).withRel("browseFaculty"));
	}
	
	
	public void addFacultyInteractionLinks(UserModel model) {
		if (model == null)
			return;
		model.add(linkTo(methodOn(FacultyController.class).browseStudents(null)).withRel("browseStudents"));
	}


	public void addBrowsedFacultyLinks(UserModel model, Student student) {
		
		model.add(linkTo(methodOn(StudentController.class).addFavouriteFaculty(model.getUserId(), null))
					.withRel("addAsFavourite"));			
	}
	

	public void addBrowsedStudentLinks(UserModel model, Faculty faculty) {
		model.add(linkTo(methodOn(FacultyController.class).addFavouriteStudent(model.getUserId(), null))
					.withRel("addAsFavourite"));				
	}
}
