package com.stacko.capmatch.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.stacko.capmatch.Models.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	User findByEmailIgnoringCase(String email);

}
