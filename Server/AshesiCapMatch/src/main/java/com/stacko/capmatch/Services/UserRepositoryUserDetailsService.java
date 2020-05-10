package com.stacko.capmatch.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Repositories.UserRepository;

public class UserRepositoryUserDetailsService
	implements UserDetailsService{
	
	private UserRepository userRepo;
	
	@Autowired
	public UserRepositoryUserDetailsService(UserRepository userRepo) {
	this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username == null)
			throw new UsernameNotFoundException("A null value was passed as username");
		
		username = username.toLowerCase();
		User user = userRepo.findByEmailIgnoringCase(username);				// In this system, emails are used as usernames
		if (user != null) {
		return user;
		}
		throw new UsernameNotFoundException(
		"User '" + username + "' not found");
	}

}
