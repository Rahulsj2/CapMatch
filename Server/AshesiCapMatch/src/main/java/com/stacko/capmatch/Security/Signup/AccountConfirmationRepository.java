package com.stacko.capmatch.Security.Signup;

import org.springframework.data.repository.CrudRepository;

import com.stacko.capmatch.Models.User;

public interface AccountConfirmationRepository extends CrudRepository<AccountConfirmation, Integer> {
	
	public AccountConfirmation findByUser(User user);
	
	public AccountConfirmation findByConfirmCode(String confirmCode);

}
