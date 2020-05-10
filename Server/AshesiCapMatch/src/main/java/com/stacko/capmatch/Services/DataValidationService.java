package com.stacko.capmatch.Services;


import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.stacko.capmatch.Models.User;

@Service
public class DataValidationService {
	
	private final int PASSWORD_MIN_LENGTH = 8;
	private final int NAME_MAX_LENGTH = 25;
	
	public boolean isUserDetailsValid(User user) {
		// Firstly, none of the essential detials should be absent
		if (user.getFirstname() == null ||
				user.getLastname() == null ||
				user.getEmail() == null ||
				user.getPassword() == null)
			return false;
		
		// Next, make sure none of those fields represent and SQL injection attack
		
		
		// Make sure a valid email address is provided
		if (!isValidEmail(user.getEmail()))
			return false;
		
		if (!isValidPassword(user.getPassword()))
			return false;
		
		
		return true;		// Everything checks out if bottom is reached
	}
	
	
	public boolean isNameValid(String name) {
		// Names should be less than 25 characters
		return name.length() <= NAME_MAX_LENGTH;
	}
	
	public boolean isValidEmail(String email) {
		// Check that this is a valid general email
		Boolean emailValid = Pattern
								.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
										Pattern.CASE_INSENSITIVE)
								.matcher(email)
								.find();
		
		// Only ashesi emails are allowed. Must end with "@ashesi.edu.gh"
		emailValid = Pattern
						.compile("@ashesi.edu.gh$",
								Pattern.CASE_INSENSITIVE)
						.matcher(email)
						.find()
					&& emailValid;
		
		return emailValid;		
	}
	
	
	public boolean isValidPassword(String password) {
		// Password rules go here
		return password.length() >= PASSWORD_MIN_LENGTH;
	}

}

