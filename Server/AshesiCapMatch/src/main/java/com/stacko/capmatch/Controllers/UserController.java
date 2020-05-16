package com.stacko.capmatch.Controllers;


import java.util.Iterator;

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
			log.info("Resending confirmation email failed because the user with id '" + userId + ""
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

}
