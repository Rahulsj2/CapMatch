package com.stackO.capmatch.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.stackO.capmatch.Models.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	User findByEmailIgnoringCase(String email);

}
