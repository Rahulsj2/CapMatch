package com.stacko.capmatch.Models.HATEOAS.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import com.stacko.capmatch.Controllers.FacultyController;
import com.stacko.capmatch.Controllers.UserController;
import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.HATEOAS.FacultyModel;

public class FacultyModelAssembler
		extends RepresentationModelAssemblerSupport<Faculty, FacultyModel>{
	
	public FacultyModelAssembler() {
		super(FacultyController.class, FacultyModel.class);
	}
	
	@Override
	public FacultyModel instantiateModel(Faculty faculty) {
		return new FacultyModel(faculty);
	}
	
	@Override
	public FacultyModel toModel(Faculty faculty) {
		FacultyModel model = this.createModelWithId(faculty.getUserId(), faculty);
		addLinks(model);
		return model;
	}
	
	private void addLinks(FacultyModel model) {
		// Add all required links
		model.add(linkTo(UserController.class).slash(model.getUserId()).slash("interests").withRel("interests"));
		model.add(linkTo(UserController.class).slash(model.getUserId()).slash("SDGs").withRel("SDGs"));
		
		model.add(linkTo(FacultyController.class).slash(model.getUserId()).slash("department").withRel("department"));
		model.add(linkTo(FacultyController.class).slash(model.getUserId()).slash("supervisedStudents").withRel("supervisedStudents"));
		model.add(linkTo(FacultyController.class).slash(model.getUserId()).slash("favouriteStudents").withRel("favouriteStudents"));
	}
}
