package com.stacko.capmatch.Controllers;

import java.security.Principal;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stacko.capmatch.Configuration.AppConfig;
import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.RequestError;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Models.User.AccountStatus;
import com.stacko.capmatch.Models.HATEOAS.LoggedInUserModel;
import com.stacko.capmatch.Models.HATEOAS.UserModel;
import com.stacko.capmatch.Models.HATEOAS.Assemblers.UserModelAssembler;
import com.stacko.capmatch.Repositories.FacultyRepository;
import com.stacko.capmatch.Repositories.StudentRepository;
import com.stacko.capmatch.Repositories.UserRepository;
import com.stacko.capmatch.Security.SecurityConfig;
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
	
	@Autowired
	SecurityConfig securityConfig;
		
	
//	@GetMapping(consumes={"application/json", "text/xml", MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.ALL_VALUE})
	@PostMapping(consumes={"application/json", "text/xml", MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.ALL_VALUE})
	public ResponseEntity<?> login(@RequestBody LoginDetails loginDetails, HttpServletRequest req, HttpServletResponse res){
		RequestError error = new RequestError();
		if (loginDetails.getEmail() == null || loginDetails.getPassword() == null) {
			if (loginDetails.getEmail()== null)
				error.setMessage("No Email Provided");
			else
				error.setMessage("No Password Provided");
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
		
		loginDetails.setEmail(loginDetails.getEmail().toLowerCase());			// Make email lowercase
		
		User user = userRepo.findByEmailIgnoringCase(loginDetails.getEmail());		
		if (user == null) {
			error.setMessage("Email or Password Incorrect");
			return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
		}else if (user.getAccountStatus().equals(AccountStatus.BLOCKED)) {
			error.setMessage("Your account has been blocked. Contact admin");
			return new ResponseEntity<>(error, HttpStatus.LOCKED);
		}
		
		try{
			user = loginUser(loginDetails, user);
		}catch (Exception e) {
			error.setMessage("It's not you. It's us. We'll fix this. Try again later");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (user == null) {
			error.setMessage("Email or Password Incorrect");
			return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if (user.getAccountStatus().equals(User.AccountStatus.BLOCKED)) {
			error.setMessage("Your account has been blocked. Contact admin");
			return new ResponseEntity<>(error, HttpStatus.LOCKED);					// If user account is blocked, return a response immediately
		}
		
		// Add Authorization Header to Response
		res.setHeader("Authorization", validationService
											.composeAuthenticationHeaderValue
												(user.getEmail(), loginDetails.getPassword()));
		
		//Manually authenticate logged in user
		try {
			this.manuallyAuthenticate(loginDetails, req.getSession(true));
			this.startUserSession(user, req.getSession(true));
		} catch (Exception e) {
			error.setMessage("It's not you. It's us. We'll fix this. Try again later");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}	    
	    		
		LoggedInUserModel model = prepareLoggedInUserForResponse(user);
		return new ResponseEntity<>(model, HttpStatus.OK);
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
			log.error(String.format("Could not start session for user with Principal name %s because a matching user"
					+ "account could not be found", principal.getName()));
			RequestError error = new RequestError("It's not you. It's us. We'll fix this. Try again later");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		HttpSession session = req.getSession(true);
		session.setMaxInactiveInterval(this.appConfig.getSessionIdleLifetime());
		session.setAttribute("user", user);
		
		log.info("Starting new session for user '" + user.getName() + "'");		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}	
	
	// ------------------------------------------------- Private Helper Methods ----------------------------------------------------	
	
	
	/**
	 * 
	 * @param loginDetails
	 * @param session
	 * @throws Exception
	 */
	public void manuallyAuthenticate(@NotNull LoginDetails loginDetails, @NotNull HttpSession session) throws Exception {
		UsernamePasswordAuthenticationToken authReq
	      = new UsernamePasswordAuthenticationToken(loginDetails.getEmail(), loginDetails.getPassword());
		
		AuthenticationManager authManager;
		try {
			authManager = securityConfig.authenticationManagerBean();
		} catch (Exception e) {
			log.error(String.format("Manually authenticating user with email '%s' failed because there was a problem getting"
					+ "the 'authenticationManagerBean' from the app SecurityConfig. \n\t StackTrace>> %s", 
																	loginDetails.getEmail(), e.getStackTrace().toString()));
			
			e.printStackTrace();
			throw new Exception();
		}
		Authentication auth = authManager.authenticate(authReq);	    
	    SecurityContext sc = SecurityContextHolder.getContext();
	    sc.setAuthentication(auth);
	    session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
	}
	
	
	/**
	 * 
	 * @param user
	 * @param session
	 * @throws Exception
	 */
	public void startUserSession(@NotNull User user, @NotNull HttpSession session) throws Exception {
		if (user == null) {
			log.error(String.format("Could not start session because a null object was parsed as user"
					+ "whilst trying to start a session"));
			throw new Exception();
		}
		
		session.setMaxInactiveInterval(this.appConfig.getSessionIdleLifetime());
		session.setAttribute("user", user);
		
		log.info("Starting new session for user '" + user.getName() + "'");		
	}
	
	
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public LoggedInUserModel prepareLoggedInUserForResponse(User user) {
		UserModel model;
		if (studentRepo.findById(user.getUserId()).isPresent()) {			// If user is a student			
			Student student = studentRepo.findById(user.getUserId()).get();
						
			model = (new UserModelAssembler()).toModel(student);
			hateoasService.addStudentInteractionLinks(model);
			
		}else {									// If user is not a student, must be faculty or in a more unlikely case neither. Just Admin
			
			if (facultyRepo.findById(user.getUserId()).isPresent()) {				// If this is a faculty, then treat login as faculty
				Faculty faculty = facultyRepo.findById(user.getUserId()).get();
				model = (new UserModelAssembler()).toModel(faculty);
				hateoasService.addFacultyInteractionLinks(model);
			}else {																// Otherwise, treat as generic user
				model = (new UserModelAssembler()).toModel(user);
			}			

			// Check for admin login
			for (UserPermission permission : user.getPermissions()) {
				if (permission.getName().equalsIgnoreCase("ADMIN")) {
					hateoasService.addAdminInterractionLinks(model); 
					break;
				}
			}
		}
		
		hateoasService.addUserInteractionLinks(model);
		
		// LoggedInUserModel enables the addition of settings and all info needed by client to properly interface with server
		LoggedInUserModel loggedInUser = new LoggedInUserModel(model, appConfig.getSessionIdleLifetime());
		return loggedInUser;
	}
	
	
	
	/**
	 * 
	 * @param loginDetails
	 * @return
	 * @throws IllegalStateException
	 */
	private User loginUser(LoginDetails loginDetails, User user) throws IllegalStateException {
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
			
			if (loginProfile.getFailedAttempts() >= this.appConfig.getMaxFailedLoginAttempts()) {
				user.setAccountStatus(User.AccountStatus.BLOCKED);
				loginProfile.setFailedAttempts(0);			// reset failed attempts count	
				
				loginProfileRepo.save(loginProfile);
				userRepo.save(user);
				
				log.info("User '" + user.getEmail() + "'  has been blocked after failed login attempts");
				
				return user;
			}
			
			loginProfileRepo.save(loginProfile);			
			
			return null;			
		}
	}

}
