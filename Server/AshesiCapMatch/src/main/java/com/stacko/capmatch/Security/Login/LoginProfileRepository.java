package com.stacko.capmatch.Security.Login;

import org.springframework.data.repository.CrudRepository;

import com.stacko.capmatch.Models.User;

public interface LoginProfileRepository extends CrudRepository<LoginProfile, Integer> {
	
	public LoginProfile findByUser(User user);

}
