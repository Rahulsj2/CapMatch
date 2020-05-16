package com.stacko.capmatch.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stacko.capmatch.Models.Interest;
import com.stacko.capmatch.Models.HATEOAS.InterestModel;
import com.stacko.capmatch.Models.HATEOAS.Assemblers.InterestModelAssembler;
import com.stacko.capmatch.Repositories.InterestRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/interests")
@CrossOrigin(origins="*")
public class InterestController {
	
	@Autowired
	InterestRepository interestRepo;
	
	@GetMapping
	public Iterable<InterestModel> getAllInterests(){
		Iterable<Interest> interests = interestRepo.findAll();
		
		CollectionModel<InterestModel> models = (new InterestModelAssembler()).toCollectionModel(interests);
		return models;
	}
	
	

}
