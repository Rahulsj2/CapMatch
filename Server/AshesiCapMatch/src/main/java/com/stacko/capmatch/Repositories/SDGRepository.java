package com.stacko.capmatch.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.stacko.capmatch.Models.SDG;

public interface SDGRepository extends CrudRepository<SDG, Integer> {
	
	public SDG findByNumber(int sdgNumber);

}
