package com.stacko.capmatch.Models.HATEOAS.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import com.stacko.capmatch.Controllers.StudentController;
import com.stacko.capmatch.Controllers.UserController;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.HATEOAS.StudentModel;

public class StudentModelAssembler extends RepresentationModelAssemblerSupport<Student, StudentModel> {

	public StudentModelAssembler() {
		super(StudentController.class, StudentModel.class);
	}
	
	
	@Override
	public StudentModel instantiateModel(Student student) {
		return new StudentModel(student);
	}
	
	
	@Override
	public StudentModel toModel(Student student) {
		StudentModel model = this.createModelWithId(student.getUserId(), student);
		addLinks(model);
		return model;
	}
	
	private void addLinks(StudentModel model) {
		// Add all required links
		model.add(linkTo(UserController.class).slash(model.getUserId()).slash("interests").withRel("interests"));
		model.add(linkTo(UserController.class).slash(model.getUserId()).slash("SDGs").withRel("SDGs"));
		
		model.add(linkTo(StudentController.class).slash(model.getUserId()).slash("major").withRel("major"));
		model.add(linkTo(StudentController.class).slash(model.getUserId()).slash("supervisor").withRel("supervisor"));
		model.add(linkTo(StudentController.class).slash(model.getUserId()).slash("favouriteSupervisors").withRel("favouriteSupervisors"));
	}
}
