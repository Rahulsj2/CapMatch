package com.stacko.capmatch.Controllers;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stacko.capmatch.Models.Interest;
import com.stacko.capmatch.Models.SDG;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Repositories.InterestRepository;
import com.stacko.capmatch.Repositories.SDGRepository;
import com.stacko.capmatch.Repositories.UserRepository;
import com.stacko.capmatch.Security.Login.LoginDetails;
import com.stacko.capmatch.Services.DataValidationService;
import com.stacko.capmatch.Services.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/users")
@CrossOrigin(origins="*")
public class UserController {
	
	@Autowired
	private InterestRepository interestRepo;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepo;
	
	
	@Autowired
	private SDGRepository sdgRepo;
	
	@Autowired
	private SignUpController signupController;
	
	@Autowired
	DataValidationService validationService;
	
	// -----------------------------------------------Interest Stuff ------------------------------------------------------
	
	@PostMapping(path="/{userId}/addInterests")	
	@PutMapping(path="/{userId}/addInterests")
	public ResponseEntity<?> addInterests(@RequestBody Iterable<Interest> interests, @PathVariable int userId){
		Iterator<Interest> iter = interests.iterator();
		
		User user = userRepo.findById(userId).get();
		
		if (user == null) {
			log.error("Could not find user with id '" + userId + "' while trying to add intrests");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		while(iter.hasNext()) {
			user.addInterest(iter.next());
		}
		
		userRepo.save(user);
		
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}
	
	
	@PostMapping(path="/{userId}/setInterests")	
	@PutMapping(path="/{userId}/setInterests")
	public ResponseEntity<?> setInterests(@RequestBody Iterable<Interest> interests, @PathVariable int userId){		
		User user = userRepo.findById(userId).get();
		if (user == null) {
			log.error("Could not find user with id '" + userId + "' while trying to set intrests");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		user.removeAllInterests();
		userRepo.save(user);
		return addInterests(interests, userId);
	}
	
	
	@PostMapping(path="/{userId}/addInterest")	
	@PutMapping(path="/{userId}/addInterest")
	public ResponseEntity<?> addInterest(@RequestBody Interest interest, @PathVariable int userId){		
		User user = userRepo.findById(userId).get();
		
		if (user == null) {
			log.error("Could not find user with id '" + userId + "' while trying to add intrest");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Interest retreivedInterest = interestRepo.findById(interest.getInterestId()).get();
		
		if (retreivedInterest == null) {
			log.error("Could not find Intrest with id '" + interest.getInterestId() + "' while trying to add intrest");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}			
					
		user.addInterest(retreivedInterest);
		
		userRepo.save(user);
		
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}
	
	
	
	// --------------------------------------- SDG Stuff ------------------------------------------------
	
	@PostMapping(path="/{userId}/addSDGs")	
	@PutMapping(path="/{userId}/addSDGs")
	public ResponseEntity<?> addSDGs(@RequestBody Iterable<SDG> sdgs, @PathVariable int userId){
		Iterator<SDG> iter = sdgs.iterator();		
		User user = userRepo.findById(userId).get();
		
		if (user == null) {
			log.error("Could not find user with id '" + userId + "' while trying to add SDGs");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		SDG currentSDG;
		while(iter.hasNext()) {		
			currentSDG = sdgRepo.findByNumber(iter.next().getNumber());
			if (currentSDG != null)
				user.addSDG(currentSDG);
		}
		
		userRepo.save(user);		
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}
	
	
	
	@PostMapping(path="/{userId}/setSDGs")	
	@PutMapping(path="/{userId}/setSDGs")
	public ResponseEntity<?> setSDGs(@RequestBody Iterable<SDG> sdgs, @PathVariable int userId){
		User user = userRepo.findById(userId).get();		
		if (user == null) {
			log.error("Could not find user with id '" + userId + "' while trying to set SDGs");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		user.removeAllSDGs();				// Remove all SDGs
		
		userRepo.save(user);		
		return addSDGs(sdgs, userId);
	}
	
	
	
	
	@PostMapping(path="/{userId}/addSDG")	
	@PutMapping(path="/{userId}/addSDG")
	public ResponseEntity<?> addSDG(@RequestBody SDG sdg, @PathVariable int userId){
		User user = userRepo.findById(userId).get();		
		if (user == null) {
			log.error("Could not find user with id '" + userId + "' while trying to add SDG");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (sdg != null) {		
			sdg = sdgRepo.findByNumber(sdg.getNumber());
			user.addSDG(sdg);
		} else {
			log.error("Could not add a null SDG for user with ID '" + user.getUserId() + "'" );
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		userRepo.save(user);		
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}
	
	
	
	// ------------------------------------------- Account Confirmation Stuff -------------------------------------------------------	
	
	/**
	 * This method sends a confirmation mail to users to enable them verify and account
	 * @param userId
	 * @return
	 */
	@PostMapping(path="{userId}/sendConfirmation")
	public ResponseEntity<?> resendConfirmationMail(@PathVariable int userId){
		User user = userRepo.findById(userId).get();
		if (user == null) {
			log.info("Sending confirmation email failed because the user with id '" + userId + ""
					+ "' could not be found");
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		
		// Make sure user isn't already confirmed
		if (!user.getAccountStatus().equals(User.AccountStatus.UNVERIFIED))
			return new ResponseEntity<>(null, HttpStatus.OK);
		
		boolean isSent = this.emailService.sendAccountConfirmationEmail(user);
		
		if (isSent)
			return new ResponseEntity<>(null, HttpStatus.CREATED);
		else
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@PostMapping(path="/changeEmail")
	@PutMapping(path="/changeEmail")
	public ResponseEntity<?> changeEmail(@RequestBody LoginDetails details, HttpServletRequest req){
		HttpSession session = req.getSession();
		
		User user = (User) session.getAttribute("user");		
		if (user == null || details.getEmail() == null)
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		// Make sure email provided is a valid Ashesi email
		if (!this.validationService.isValidEmail(details.getEmail()))
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
			
		user = userRepo.findByEmailIgnoringCase(user.getEmail());		
		// Only unverified accounts should be able to change email
		if (!user.getAccountStatus().equals(User.AccountStatus.UNVERIFIED))
			return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
		
		user.setEmail(details.getEmail());
		
		userRepo.save(user);
		this.resendConfirmationMail(user.getUserId());				// Resend Confirmation mail
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	
	
	@PostMapping(path="/sendConfirmation")
	public ResponseEntity<?> sendConfirmationGivenEmail(@RequestBody LoginDetails details){
		if (details.getEmail() == null) {				// If no email is provided
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}		
		// Find user with given email
		User user = userRepo.findByEmailIgnoringCase(details.getEmail());
		
		if (user == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}else {
			// Make sure user isn't already confirmed
			if (!user.getAccountStatus().equals(User.AccountStatus.UNVERIFIED))
				return new ResponseEntity<>(null, HttpStatus.OK);
			
			boolean isSent = this.emailService.sendAccountConfirmationEmail(user);			
			if (isSent)
				return new ResponseEntity<>(null, HttpStatus.CREATED);
			else
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	
	@PostMapping(path="/forgotPassword")
	public ResponseEntity<?> sendPasswordResetEmail(@RequestBody LoginDetails details){
		if (details.getEmail() == null) 				// If no email is provided
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		
		// Find user with given email
		User user = userRepo.findByEmailIgnoringCase(details.getEmail());
		if (user == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
			
		//Generate confirmationDetails
		this.signupController.generateAccountConfirmationDetails(user);
		
		boolean sent = this.emailService.sendPasswordResetEmail(user);
		
		if (sent)
			return new ResponseEntity<>(null, HttpStatus.CREATED);
		else
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
