package com.stackO.capmatch.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.stackO.capmatch.Models.Major;

public interface MajorRepository extends CrudRepository<Major, Integer> {
	
	public Major findByMajorCodeIgnoringCase(String majorCode);

}
