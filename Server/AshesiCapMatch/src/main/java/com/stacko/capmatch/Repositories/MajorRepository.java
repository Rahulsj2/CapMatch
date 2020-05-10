package com.stacko.capmatch.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.stacko.capmatch.Models.Major;

public interface MajorRepository extends CrudRepository<Major, Integer> {
	
	public Major findByMajorCodeIgnoringCase(String majorCode);

}

