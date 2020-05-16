package com.stacko.capmatch.Controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Models.HATEOAS.UserModel;
import com.stacko.capmatch.Models.HATEOAS.Assemblers.UserModelAssembler;
import com.stacko.capmatch.Repositories.FacultyRepository;
import com.stacko.capmatch.Repositories.StudentRepository;
import com.stacko.capmatch.Repositories.UserRepository;
import com.stacko.capmatch.Security.Login.LoginDetails;
import com.stacko.capmatch.Security.Login.LoginProfile;
import com.stacko.capmatch.Security.Login.LoginProfileRepository;
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
	
	
	@GetMapping(path="/student", consumes={"application/json", "text/xml"})
	public ResponseEntity<UserModel> loginStudent(@RequestBody LoginDetails loginDetails) {
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
//		else if( user.getAccountStatus().equals(User.AccountStatus.BLOCKED))				// Leave blocking out to client
//			return new ResponseEntity<>(null, HttpStatus.LOCKED);
		
		// Lastly, make sure this is a student
		Student student = studentRepo.findById(user.getUserId()).get();
		if (student == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		else {
			UserModel model = (new UserModelAssembler()).toModel(student);
			hateoasService.addUserInteractionLinks(model);
			hateoasService.addStudentInteractionLinks(model);
			if (model.getAccountStatus().equals(User.AccountStatus.BLOCKED))
				return new ResponseEntity<>(model, HttpStatus.LOCKED);
			else
				return new ResponseEntity<>(model, HttpStatus.FOUND);
		}
	}
	
	
	
	@GetMapping(path="/faculty", consumes={"application/json", "text/xml"})
	public ResponseEntity<UserModel> loginFaculty(@RequestBody LoginDetails loginDetails) {
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
//		else if( user.getAccountStatus().equals(User.AccountStatus.BLOCKED))				// Leave blocking out to client
//			return new ResponseEntity<>(null, HttpStatus.LOCKED);
		
		// Lastly, make sure this is a faculty
		Faculty faculty = facultyRepo.findById(user.getUserId()).get();
		if (faculty == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		else {
			UserModel model = (new UserModelAssembler()).toModel(faculty);
			hateoasService.addUserInteractionLinks(model);
			hateoasService.addFacultyInteractionLinks(model);
			
			if (model.getAccountStatus().equals(User.AccountStatus.BLOCKED))
				return new ResponseEntity<>(model, HttpStatus.LOCKED);
			else
				return new ResponseEntity<>(model, HttpStatus.FOUND);
		}
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
