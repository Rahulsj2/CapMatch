package com.stackO.capmatch.Security.login;

import org.springframework.data.repository.CrudRepository;

import com.stackO.capmatch.Models.User;

public interface LoginProfileRepository extends CrudRepository<LoginProfile, Integer> {
	
	public LoginProfile findByUser(User user);

}
