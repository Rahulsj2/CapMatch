package com.stacko.capmatch.Controllers;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stacko.capmatch.Models.Interest;
import com.stacko.capmatch.Models.RequestError;
import com.stacko.capmatch.Models.SDG;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Repositories.InterestRepository;
import com.stacko.capmatch.Repositories.SDGRepository;
import com.stacko.capmatch.Repositories.UserRepository;
import com.stacko.capmatch.Security.Login.LoginDetails;
import com.stacko.capmatch.Security.Signup.Interests;
import com.stacko.capmatch.Security.Signup.SDGs;
import com.stacko.capmatch.Services.DataValidationService;
import com.stacko.capmatch.Services.EmailService;
import com.stacko.capmatch.Services.StorageService;

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
	
	@Autowired
	StorageService storageService;
	
	// -----------------------------------------------Interest Stuff ------------------------------------------------------
	
	@PostMapping(path="/{userId}/addInterests")	
	@PutMapping(path="/{userId}/addInterests")
	public ResponseEntity<?> addInterests(@RequestBody Interests body , @PathVariable int userId){
		Iterable<Interest> interests = body.getInterests();
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
		
		return new ResponseEntity<>(null, HttpStatus.OK);
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
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	
	@PostMapping(path="/{userId}/setInterests")	
	@PutMapping(path="/{userId}/setInterests")
	public ResponseEntity<?> setInterests(@RequestBody Interests body, @PathVariable int userId){
		Iterable<Interest> interests = body.getInterests();
		User user = userRepo.findById(userId).get();
		if (user == null) {
			log.error("Could not find user with id '" + userId + "' while trying to set intrests");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		user.removeAllInterests();
		userRepo.save(user);
		return addInterests(new Interests(interests), userId);
	}
	
	
	
	// --------------------------------------- SDG Stuff ------------------------------------------------
	
	@PostMapping(path="/{userId}/addSDGs")	
	@PutMapping(path="/{userId}/addSDGs")
	public ResponseEntity<?> addSDGs(@RequestBody SDGs body , @PathVariable int userId){
		Iterable<SDG> sdgs = body.getSdgs();
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
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	
	
	@PostMapping(path="/{userId}/setSDGs")	
	@PutMapping(path="/{userId}/setSDGs")
	public ResponseEntity<?> setSDGs(@RequestBody SDGs body, @PathVariable int userId){
		Iterable<SDG> sdgs = body.getSdgs();
		User user = userRepo.findById(userId).get();		
		if (user == null) {
			log.error("Could not find user with id '" + userId + "' while trying to set SDGs");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		user.removeAllSDGs();				// Remove all SDGs
		
		userRepo.save(user);		
		return addSDGs(new SDGs(sdgs), userId);
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
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	
	// --------------------------------- File/Document End-points --------------------------
	/**
	 * 
	 * @param file
	 * @param req
	 * @return
	 */
	@PostMapping(path="/profile/photo")
	@ResponseBody
	public ResponseEntity<?> setProfilePhoto(@RequestBody MultipartFile file,
			HttpServletRequest req) {		
		if (file == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		
		RequestError error = new RequestError();
		
		if (!this.validationService.isValidPhoto(file, error)) 			// Validate photo file
			return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
			
		
		User user = userRepo.findByEmailIgnoringCase(req.getUserPrincipal().getName());
		if (user == null) {
			log.error("Could not set Profile photo for user '" + req.getUserPrincipal().getName() + 
						"' because getting corresponding user object from repo failed");
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);		
		}
		
		if (user.getProfilePhoto() != null)				// If photo already set then delete old photo
			storageService.removeProfilePhoto(user);
			
		storageService.storeProfilePhoto(file, user);
//		storageService.store(file);

		return new ResponseEntity<>(null, HttpStatus.OK);
	}
		
	
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(path="profile/remove/photo")
	//@DeleteMapping(path="profile/remove/photo")
	public ResponseEntity<?> removeProfilePhoto(HttpServletRequest req){				
		
		User user = userRepo.findByEmailIgnoringCase(req.getUserPrincipal().getName());
		if (user == null) {
			log.error("Could not remove Photo for user '" + req.getUserPrincipal().getName() + 
						"' because getting corresponding user object from repo failed");
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);		
		}
			
		storageService.removeProfilePhoto(user);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	
	
	/**
	 * 
	 * @param cv
	 * @param req
	 * @return
	 */
	@PostMapping(path="/profile/cv")
	public ResponseEntity<?> setCV(@RequestBody MultipartFile file,
			HttpServletRequest req) {		
		if (file == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		
		RequestError error = new RequestError();
		
		if (!this.validationService.isValidCV(file, error)) 			// Validate photo file
			return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
			
		User user = userRepo.findByEmailIgnoringCase(req.getUserPrincipal().getName());
		if (user == null) {
			log.error("Could not set Profile photo for user '" + req.getUserPrincipal().getName() + 
						"' because getting corresponding user object from repo failed");
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);		
		}
		
		if (user.getProfilePhoto() != null)				// If photo already set then delete old photo
			storageService.removeCV(user);
			
		storageService.storeCV(file, user);

		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(path="profile/remove/cv")
	//@DeleteMapping(path="profile/cv")
	public ResponseEntity<?> removeCV(HttpServletRequest req){				
		
		User user = userRepo.findByEmailIgnoringCase(req.getUserPrincipal().getName());
		if (user == null) {
			log.error("Could not remove CV for user '" + req.getUserPrincipal().getName() + 
						"' because getting corresponding user object from repo failed");
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);		
		}
			
		storageService.removeCV(user);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	

	
	// ------------------------------End of File/Document End-points --------------------------------------------------
	
	
	
	
	
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
			return new ResponseEntity<>(null, HttpStatus.OK);
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
		
		// Make sure the new email isn't already in use by another account
		if (userRepo.findByEmailIgnoringCase(details.getEmail()) != null)
			return new ResponseEntity<>(null, HttpStatus.IM_USED);
		
		user.setEmail(details.getEmail());
		
		userRepo.save(user);
		this.resendConfirmationMail(user.getUserId());				// Resend Confirmation mail
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	
	
	/**
	 * 
	 * @param details
	 * @return
	 */
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
				return new ResponseEntity<>(null, HttpStatus.OK);
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
			return new ResponseEntity<>(null, HttpStatus.OK);
		else
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
