package com.stacko.capmatch.Services;


import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stacko.capmatch.Models.RequestError;
import com.stacko.capmatch.Models.User;
/***
 * 
 * @author Owusu-Banahene Osei
 * 
 * This class contains methods that help validate user input sent to the server
 *
 */
@Service
public class DataValidationService {
	
	private final int PASSWORD_MIN_LENGTH = 8;
	private final int NAME_MAX_LENGTH = 25;
	
	
	public boolean isUserDetailsValid(User user, RequestError error) {
		// Firstly, none of the essential details should be absent
		if (!isNameValid(user.getFirstname()) || !isNameValid(user.getLastname())) {
			if (error != null) error.setMessage("Provide both 'firstname' and 'lastname'");
			return false;
		} else if (user.getPassword() == null) {
			if (error != null) error.setMessage("Enter a password");
			return false;
		}
		
		
		// Next, make sure none of those fields represent an SQL injection attack
		
		
		// Make sure a valid email address is provided
		if (!isValidEmail(user.getEmail())) {
			if (error != null)
				error.setMessage("Provide a valid Ashesi email");
			return false;
		}
		
		if (!isValidPassword(user.getPassword())) {
			if (error != null)
				error.setMessage("Provide a password that is at least 8 characters long");
			return false;
		}		
		
		return true;		// Everything checks out if bottom is reached
	}
	
	
	public boolean isNameValid(String name) {
		// Names should be less than 25 characters
		if (name == null) return false;
		return name.length() <= NAME_MAX_LENGTH;
	}
	
	public boolean isValidEmail(String email) {
		if (email == null) return false;
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
	
	
	public String composeAuthenticationHeaderValue(String username, String password) {
		String composedValue = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(composedValue.getBytes());
	}
	
	
	/**
	 * 
	 * @param user
	 * @param fileType
	 * @param fileFormat
	 * @return
	 */
	public String generateFileName(User user, String fileType, String fileFormat) {
		if (user == null || fileType == null || fileFormat == null) 
			throw new IllegalArgumentException();
		
		return String.format("%s_%s_%s.%s", new Date().getTime(), user.getName(), fileType, fileFormat)
					.replaceAll("\\s+", "");
	}

	

	/**
	 * 
	 * @param file
	 * @param error
	 * @return
	 */
	public boolean isValidPhoto(MultipartFile file, RequestError error) {
		if (file == null) return false;
		
		// check file type
		Tika tika = new Tika();
		String detectedType;
		try {
			detectedType = tika.detect(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			if (error != null) error.setMessage("We could not validate your photo's format");
			return false;
		}
				
		if (!detectedType.equals("image/jpeg")) {					// Make sure file is in JPEG format
			if (error != null) error.setMessage("Upload photo in JPEG format");
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param cv
	 * @param error
	 * @return
	 */
	public boolean isValidCV(MultipartFile cv, RequestError error) {
		if (cv == null) return false;
		
		// check file type
		Tika tika = new Tika();
		String detectedType;
		try {
			detectedType = tika.detect(cv.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			if (error != null) error.setMessage("We could not validate the your CV format");
			return false;
		}
		
		if (!"application/pdf".equals(detectedType)) {			// If file is not pdf format
			if (error != null) error.setMessage("CV must be in PDF format");
			return false;
		}
		return true;
	}


}

