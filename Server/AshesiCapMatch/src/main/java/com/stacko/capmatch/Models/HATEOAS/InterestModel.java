package com.stacko.capmatch.Models.HATEOAS;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.stacko.capmatch.Models.Interest;

import lombok.Getter;


@Relation(collectionRelation = "interests")
public class InterestModel extends RepresentationModel<InterestModel> {
	
	@Getter
	private int interestId;
	
	@Getter
	private String name;
	
	@Getter
	private String description;
	
	
	public InterestModel(Interest interest) {
		if (interest == null)
			throw new IllegalArgumentException("Cannot create instance of InterestModel from 'null'");
		
		this.interestId = interest.getInterestId();
		this.name = interest.getName();
		this.description = interest.getDescription();
	}
}
