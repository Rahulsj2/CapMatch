package com.stacko.capmatch.Models.HATEOAS.Assemblers;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.stacko.capmatch.Controllers.FacultyController;
import com.stacko.capmatch.Controllers.StudentController;
import com.stacko.capmatch.Controllers.UserController;
import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Models.HATEOAS.UserModel;

public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {
	
	public UserModelAssembler() {
		super(UserController.class, UserModel.class);
	}
	
	@Override
	public UserModel instantiateModel(User user) {
		return new UserModel(user);
	}
	
	@Override
	public UserModel toModel(User user) {
		UserModel model = this.createModelWithId(user.getUserId(), user);
		
		// Add all required links
		model.add(linkTo(UserController.class).slash(user.getUserId()).slash("interests").withRel("interests"));
		model.add(linkTo(UserController.class).slash(user.getUserId()).slash("SDGs").withRel("SDGs"));
		
		
		if (user instanceof Student)
			addStudentLinks((Student) user, model);
		else if (user instanceof Faculty)
			addFacultyLinks((Faculty) user, model);
		
		return model;
	}
	
	
	private void addStudentLinks(Student student, UserModel model) {
		model.add(linkTo(StudentController.class).slash(student.getUserId()).slash("major").withRel("major"));
		model.add(linkTo(StudentController.class).slash(student.getUserId()).slash("supervisor").withRel("supervisor"));
		model.add(linkTo(StudentController.class).slash(student.getUserId()).slash("favouriteSupervisors").withRel("favouriteSupervisors"));
	}
	
	
	private void addFacultyLinks(Faculty faculty, UserModel model) {
		model.add(linkTo(FacultyController.class).slash(faculty.getUserId()).slash("department").withRel("department"));
		model.add(linkTo(FacultyController.class).slash(faculty.getUserId()).slash("supervisedStudents").withRel("supervisedStudents"));
		model.add(linkTo(FacultyController.class).slash(faculty.getUserId()).slash("favouriteStudents").withRel("favouriteStudents"));
	}



}
