package com.stacko.capmatch.Controllers;


import java.nio.charset.Charset;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stacko.capmatch.Models.Department;
import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.Major;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Services.DataValidationService;
import com.stacko.capmatch.Services.EmailService;
import com.stacko.capmatch.Repositories.DepartmentRepository;
import com.stacko.capmatch.Repositories.FacultyRepository;
import com.stacko.capmatch.Repositories.MajorRepository;
import com.stacko.capmatch.Repositories.StudentRepository;
import com.stacko.capmatch.Repositories.UserRepository;
import com.stacko.capmatch.Security.SecurityConfig;
import com.stacko.capmatch.Security.UserPermission;
import com.stacko.capmatch.Security.UserPermissionRepository;
import com.stacko.capmatch.Security.Login.LoginProfile;
import com.stacko.capmatch.Security.Login.LoginProfileRepository;
import com.stacko.capmatch.Security.Signup.AccountConfirmation;
import com.stacko.capmatch.Security.Signup.AccountConfirmationRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path="/signup",produces={"application/json", "text/xml"})
@CrossOrigin(origins="*")
public class SignUpController {
	
	private final int DEFAULT_NUM_OF_STUDENTS_PER_SUPERVISOR = 7;
	
	@Autowired
	private DataValidationService dataValidation;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private DepartmentRepository departmentRepo;
	
	@Autowired
	private UserPermissionRepository permissionRepo;
	
	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private FacultyRepository facultyRepo;
	
	@Autowired
	private MajorRepository majorRepo;
	
	@Autowired
	private SecurityConfig securityConfig;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	AccountConfirmationRepository accountConfirmationRepo;
	
	@Autowired
	LoginProfileRepository loginProfileRepo;
	
	/**
	 * 
	 * @param student
	 * @return
	 */
	@PostMapping(path="/student", consumes={"application/json", "text/xml"})
	public ResponseEntity<?> signUpStudent(@RequestBody Student student){
		if (!dataValidation.isUserDetailsValid(student)) {
			System.err.println("Student tried to signup with invalid details");
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
		}
		
		// Make email lowercase
		student.setEmail(student.getEmail().toLowerCase());
		
		// Encrypt Password
		student.setPassword(securityConfig
								.encoder()
									.encode(student.getPassword()));
		
		// Student must have a major
		if (student.getMajor() == null) {
			log.info("Student tried to signup without a major");
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
		}		
		// To Deal with hateos not including Ids, use major code to find major and reassign
		Major major = majorRepo.findByMajorCodeIgnoringCase(student.getMajor().getMajorCode());
		if (major != null) {
			student.setMajor(major);
		}else {
			log.warn("Cound not find major with majorCode " + 
					student.getMajor().getMajorCode() + " while trying to register student with email '" +
					student.getEmail() + "'");
			return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
		}
		
		// Make sure user doesn't already exisit
		if (userRepo.findByEmailIgnoringCase(student.getEmail()) != null) {
			log.info("Student [" + student.getEmail() + "] tried to create duplicate account");
			return new ResponseEntity<>(null, HttpStatus.IM_USED);
		}
		
		// If all checks out grant STUDENT permission and save student
		UserPermission permission = permissionRepo.findByName("STUDENT");				// get student permission
		if (permission != null) {
			student.grantPermission(permission);
		}else {
			log.warn("Could not find STUDENT permission when trying to register student with email " + student.getEmail() + "!");
			return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
		}
		
		studentRepo.save(student);
		
		// Generate Account Confirmation Code
		generateAccountConfirmationDetails(student);
		
		// Email User to confirm account
		emailService.sendAccountConfirmationEmail(student);
		
		return new ResponseEntity<>(null, HttpStatus.CREATED);		
	}



	/**
	 * 
	 * @param faculty
	 * @return
	 */
	@PostMapping(path="/faculty", consumes={"application/json", "text/xml"})
	public ResponseEntity<?> signUpStudent(@RequestBody Faculty faculty){
		if (!dataValidation.isUserDetailsValid(faculty)) {
			System.err.println("Faculty tried to signup with invalid details");
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
		}
		
		// Make email lowercase
		faculty.setEmail(faculty.getEmail().toLowerCase());
		
		// Encrypt Password
				faculty.setPassword(securityConfig
										.encoder()
											.encode(faculty.getPassword()));
		
		// Faculty must have a department
		if (faculty.getDepartment() == null) {
			log.info("Faculty with email '" + faculty.getEmail() + "' tried to signup without a department");
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
		}		
		// To Deal with hateos not including Ids, use department code to find department and reassign
		Department department = departmentRepo.findByDepartmentCodeIgnoringCase(faculty.getDepartment().getDepartmentCode());
		if (department != null) {
			faculty.setDepartment(department);
		}else {
			log.warn("Could not find department with department code '" + 
					faculty.getDepartment().getDepartmentCode() + "' while trying to register faculty with email '" +
					faculty.getEmail() + "'");
			return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
		}
		
		// Make sure user doesn't already exisit
		if (userRepo.findByEmailIgnoringCase(faculty.getEmail()) != null) {
			log.info("Faculty [" + faculty.getEmail() + "] tried to create duplicate account");
			return new ResponseEntity<>(null, HttpStatus.IM_USED);
		}
		
		// If all checks out grant FACULTY permission and save student
		UserPermission permission = permissionRepo.findByName("FACULTY");				// get student permission
		if (permission != null) {
			faculty.grantPermission(permission);
		}else {
			log.warn("Could not find 'FACULTY' permission when registering faculty with email " + faculty.getEmail() + "!");
			return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);

		}
		
		faculty.setMenteeLimit(DEFAULT_NUM_OF_STUDENTS_PER_SUPERVISOR);
		
		facultyRepo.save(faculty);
		
		// Generate Account Confirmation Code
		generateAccountConfirmationDetails(faculty);
		
		// Email User to confirm account
		emailService.sendAccountConfirmationEmail(faculty);
		
		return new ResponseEntity<>(null, HttpStatus.CREATED);		
	}
	
	
	
	@PostMapping("/confirm")
	public ResponseEntity<?> confirmAccount(@RequestParam("confirmCode") String confirmationCode){
		// Get user with confirmation code
		AccountConfirmation confirmationObject = this.accountConfirmationRepo.findByConfirmCode(confirmationCode);
		if (confirmationObject == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);			// Could not find confirmation object
		
		User user = confirmationObject.getUser();			
		if (user == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);	//if User is not found, then return not found response
		
		// Activate User Account
		user.setAccountStatus(User.AccountStatus.ACTIVE);		
		try{
			userRepo.save(user);
		}catch (Exception e) {
			log.error("Could not authenticate user with email '" + user.getEmail() + "' because it could not be saved during confirmation ");
		}
		
		// Remove authentication object
		this.accountConfirmationRepo.deleteById(confirmationObject.getConfirmationId());
		
		// Create Login Profile Required for future logins
		createLoginProfile(user);
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	
	
	
	// -------------------------------------- Class Private Helper Methods -------------------------------------------
	
	
	private void generateAccountConfirmationDetails(User user) {
		// Generate Random 20 character String and encode it
		byte[] array = new byte[20]; // length is bounded by 7
	    new Random().nextBytes(array);
	    String generatedString = new String(array, Charset.forName("UTF-8"));
	    
	    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    
	    String encodedConfirmationString = passwordEncoder.encode(generatedString);			// Double Secure.... Encoded Randomness. AWESOME !!
	    
	    AccountConfirmation confirmationDetails = new AccountConfirmation(encodedConfirmationString, user);
	    
	    try {
		    confirmationDetails = accountConfirmationRepo.save(confirmationDetails);
	    }catch(Exception e) {
	    	log.error("Generating confirmation code for user '" + user.getEmail() + "' failed. This may be because"
	    			+ " a confirmation code that already exists was generated. Becuase this is unlikely, code "
	    			+ " base does not fully protect against it. Resolve immediatetely to enable user signup.");
	    	return;
	    }
	    
	    if (confirmationDetails == null) {
	    	log.error("Unable to store Account Confirmation details for user '" + user.getEmail() 
	    				+	"' during signup");
	    }
	}
	
	
	
	private void createLoginProfile(User user) {
		if (user == null)
			return;
		LoginProfile profile = new LoginProfile(user);
		profile = loginProfileRepo.save(profile);
		
		if (profile == null) {
			log.error("Creating a login profile for user '" + user.getEmail() + "' failed after account had been"
					+ "confirmed . This user will not be able to sign in.");
		}
	}

}
