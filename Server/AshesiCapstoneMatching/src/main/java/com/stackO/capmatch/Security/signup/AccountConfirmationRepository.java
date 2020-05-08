package com.stackO.capmatch.Security.signup;

import org.springframework.data.repository.CrudRepository;

import com.stackO.capmatch.Models.User;

public interface AccountConfirmationRepository extends CrudRepository<AccountConfirmation, Integer> {
	
	public AccountConfirmation findByUser(User user);
	
	public AccountConfirmation findByConfirmCode(String confirmCode);

}
