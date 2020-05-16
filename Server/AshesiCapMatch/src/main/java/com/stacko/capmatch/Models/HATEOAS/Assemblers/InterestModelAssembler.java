package com.stacko.capmatch.Models.HATEOAS.Assemblers;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.stacko.capmatch.Controllers.InterestController;
import com.stacko.capmatch.Models.Interest;
import com.stacko.capmatch.Models.HATEOAS.InterestModel;


public class InterestModelAssembler extends RepresentationModelAssemblerSupport<Interest, InterestModel> {
	
	public InterestModelAssembler() {
		super(InterestController.class, InterestModel.class);
	}
	
	@Override
	public InterestModel instantiateModel(Interest interest){
		return new InterestModel(interest);
	}
	
	
	@Override
	public InterestModel toModel(Interest interest) {
		InterestModel model = this.createModelWithId(interest.getInterestId(), interest);
		
		// Add required links
		model.add(linkTo(InterestController.class).slash(interest.getInterestId()).slash("category").withRel("category"));
		model.add(linkTo(InterestController.class).slash(interest.getInterestId()).slash("interestedUsers").withRel("interestedUsers"));

		return model;
	}
}
