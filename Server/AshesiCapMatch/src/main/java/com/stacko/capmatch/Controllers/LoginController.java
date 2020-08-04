package com.stacko.capmatch.Controllers;

import java.security.Principal;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stacko.capmatch.Configuration.AppConfig;
import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Models.HATEOAS.UserModel;
import com.stacko.capmatch.Models.HATEOAS.Assemblers.UserModelAssembler;
import com.stacko.capmatch.Repositories.FacultyRepository;
import com.stacko.capmatch.Repositories.StudentRepository;
import com.stacko.capmatch.Repositories.UserRepository;
import com.stacko.capmatch.Security.UserPermission;
import com.stacko.capmatch.Security.Login.LoginDetails;
import com.stacko.capmatch.Security.Login.LoginProfile;
import com.stacko.capmatch.Security.Login.LoginProfileRepository;
import com.stacko.capmatch.Services.DataValidationService;
import com.stacko.capmatch.Services.HATEOASService;


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
	
	@Autowired
	HATEOASService hateoasService;
	
	@Autowired
	DataValidationService validationService;
	
	@Autowired
	AppConfig appConfig;
		
	
//	@GetMapping(consumes={"application/json", "text/xml", MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.ALL_VALUE})
	@PostMapping(consumes={"application/json", "text/xml", MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.ALL_VALUE})
	public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails, HttpServletRequest req, HttpServletResponse res){
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
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
		
		if (user.getAccountStatus().equals(User.AccountStatus.BLOCKED))
			return new ResponseEntity<>(null, HttpStatus.LOCKED);					// If user account is blocked, return a response immediately
		
		// Add Authorization Header to Response
		res.setHeader("Authorization", validationService
											.composeAuthenticationHeaderValue
												(user.getEmail(), loginDetails.getPassword()));
		
		if (studentRepo.findById(user.getUserId()).isPresent()) {			// If user is a student
			Student student = studentRepo.findById(user.getUserId()).get();
			
			UserModel model = (new UserModelAssembler()).toModel(student);
			hateoasService.addUserInteractionLinks(model);
			hateoasService.addStudentInteractionLinks(model);
			if (model.getAccountStatus().equals(User.AccountStatus.BLOCKED)) 
				return new ResponseEntity<>(null, HttpStatus.LOCKED);
			else 
				return new ResponseEntity<>(model, HttpStatus.OK);
		}else {									// If user is not a student, must be faculty or in a more unlikely case neither. Just Admin
			
			UserModel model;
			if (facultyRepo.findById(user.getUserId()).isPresent()) {				// If this is a faculty, then treat login as faculty
				Faculty faculty = facultyRepo.findById(user.getUserId()).get();
				model = (new UserModelAssembler()).toModel(faculty);
				hateoasService.addFacultyInteractionLinks(model);
			} else {																// Otherwise, treat as generic user
				model = (new UserModelAssembler()).toModel(user);
			}
			
			hateoasService.addUserInteractionLinks(model);
			

			// Check for admin login
			for (UserPermission permission : user.getPermissions()) {
				if (permission.getName().equalsIgnoreCase("ADMIN")) {
					hateoasService.addAdminInterractionLinks(model); 
					break;
				}
			}
			
			return new ResponseEntity<>(model, HttpStatus.OK);
		}
	}
	
	
	
	/**
	 * 
	 * @param principal
	 * @param req
	 * @return
	 */
	@PostMapping(path="/startsession")
	@PutMapping(path="/startsession")
	public ResponseEntity<?> startSession(Principal principal, HttpServletRequest req){
		User user = this.userRepo.findByEmailIgnoringCase(principal.getName());		
		if (user == null) {
			return new ResponseEntity<>(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
		
		HttpSession session = req.getSession(true);
		session.setMaxInactiveInterval(this.appConfig.getSessionIdleLifetime());
		session.setAttribute("user", user);
		
		log.info("Starting new session for user '" + user.getName() + "'");
		
		return new ResponseEntity<>(principal.getName(), HttpStatus.OK);
	}	
	
	// ------------------------------------------------- Private Helper Methods ----------------------------------------------------	
	
	/**
	 * 
	 * @param loginDetails
	 * @return
	 * @throws IllegalStateException
	 */
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
			
			return null;			
		}
	}

}
