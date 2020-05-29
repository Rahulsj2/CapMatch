package com.stackO.capmatch.Controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stackO.capmatch.Models.Faculty;
import com.stackO.capmatch.Models.Student;
import com.stackO.capmatch.Models.User;
import com.stackO.capmatch.Repositories.FacultyRepository;
import com.stackO.capmatch.Repositories.StudentRepository;
import com.stackO.capmatch.Repositories.UserRepository;
import com.stackO.capmatch.Security.login.LoginDetails;
import com.stackO.capmatch.Security.login.LoginProfile;
import com.stackO.capmatch.Security.login.LoginProfileRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/login")
@CrossOrigin(origins="*")
public class LoginController {
	
	private static final int MAX_LOGIN_ATTEMPTS = 4;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	StudentRepository studentRepo;
	
	@Autowired
	FacultyRepository facultyRepo;
	
	@Autowired
	LoginProfileRepository loginProfileRepo;
	
	@Autowired
	@Lazy
	PasswordEncoder passwordEncoder;
	
	
	@GetMapping(path="/student", consumes={"application/json", "text/xml"})
	public ResponseEntity<?> loginStudent(@RequestBody LoginDetails loginDetails) {
		if (loginDetails.getEmail() == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		loginDetails.setEmail(loginDetails.getEmail().toLowerCase());
		
		User user;
		try{
			user = loginUser(loginDetails);
		}catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (user == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		else if( user.getAccountStatus().equals(User.AccountStatus.BLOCKED))
			return new ResponseEntity<>(null, HttpStatus.LOCKED);
		
		// Lastly, make sure this is a student
		Student student = studentRepo.findById(user.getUserId()).get();
		if (student == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(student, HttpStatus.FOUND);
	}
		
		
	
	@GetMapping(path="/faculty", consumes={"application/json", "text/xml"})
	public ResponseEntity<EntityModel<Faculty>> loginFaculty(@RequestBody LoginDetails loginDetails) {
		if (loginDetails.getEmail() == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		loginDetails.setEmail(loginDetails
								.getEmail()
									.toLowerCase());
		
		User user;
		try{
			user = loginUser(loginDetails);
		}catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		System.err.println("Reached 0");
		
		if (user == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		else if( user.getAccountStatus().equals(User.AccountStatus.BLOCKED))
			return new ResponseEntity<>(null, HttpStatus.LOCKED);
		
		System.err.println("Reached 1");
		
		// Lastly, make sure this is a faculty
		Faculty faculty = facultyRepo.findById(user.getUserId()).get();
		System.err.println("Reached 2");

		if (faculty == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(new EntityModel<>(faculty), HttpStatus.FOUND);
	}
	
	
	
	
	private User loginUser(LoginDetails loginDetails) throws IllegalStateException {
		User user = userRepo.findByEmailIgnoringCase(loginDetails.getEmail());		
		if (user == null) {
			System.err.println("User is null");
			return null;
		}
		
		LoginProfile loginProfile = loginProfileRepo.findByUser(user);
		if (loginProfile == null) {
			log.error("Login attempt for user '" + user.getEmail() + "'  failed because the login profile for"
					+ " this user could not be found");
			throw new IllegalStateException("Login attempt for user '" + user.getEmail() + "'  failed because the login profile for"
					+ " this user could not be found");
		}

		
		if (this.passwordEncoder.matches(loginDetails.getPassword(), user.getPassword())){					// Passwords match
			loginProfile.setLastLogin(new Date());				// record successful login
			loginProfile.setFailedAttempts(0);					// reset count of failed login attempts
			loginProfileRepo.save(loginProfile);
			
			return user;
		}else {					// login failed
			loginProfile.setFailedAttempts(loginProfile.getFailedAttempts() + 1);			// increment failed attempts by one
			loginProfile.setLastFailedLogin(new Date()); 										// record last failed attempt
			
			if (loginProfile.getFailedAttempts() >= MAX_LOGIN_ATTEMPTS) {
				user.setAccountStatus(User.AccountStatus.BLOCKED);
				loginProfile.setFailedAttempts(0);			// reset failed attempts count
				loginProfile.setLastFailedLogin(new Date());
				
				userRepo.save(user);
				
				log.info("User '" + user.getEmail() + "'  has been blocked after failed login attempts");
				
				return user;
			}			
			loginProfileRepo.save(loginProfile);
			
			System.err.println("This is why....");
			return null;			
		}
	}

}