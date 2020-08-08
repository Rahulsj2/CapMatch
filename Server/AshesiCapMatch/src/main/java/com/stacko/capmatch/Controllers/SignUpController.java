package com.stacko.capmatch.Controllers;


import java.nio.charset.Charset;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.stacko.capmatch.Configuration.AppConfig;
import com.stacko.capmatch.Models.Department;
import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.Major;
import com.stacko.capmatch.Models.RequestError;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Models.HATEOAS.UserModel;
import com.stacko.capmatch.Models.HATEOAS.Assemblers.UserModelAssembler;
import com.stacko.capmatch.Services.DataValidationService;
import com.stacko.capmatch.Services.HATEOASService;
import com.stacko.capmatch.Repositories.DepartmentRepository;
import com.stacko.capmatch.Repositories.FacultyRepository;
import com.stacko.capmatch.Repositories.MajorRepository;
import com.stacko.capmatch.Repositories.StudentRepository;
import com.stacko.capmatch.Repositories.UserRepository;
import com.stacko.capmatch.Security.SecurityConfig;
import com.stacko.capmatch.Security.UserPermission;
import com.stacko.capmatch.Security.UserPermissionRepository;
import com.stacko.capmatch.Security.Login.LoginDetails;
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
		
	@Autowired
	AppConfig configuration;
	
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
	AccountConfirmationRepository accountConfirmationRepo;
	
	@Autowired
	LoginProfileRepository loginProfileRepo;
	
	@Autowired
	HATEOASService hateoasService;
	
	@Autowired
	LoginController loginController;
	
	
	/**
	 * 
	 * @param student
	 * @return
	 */
	@PostMapping(path="/student", consumes={"application/json", "text/xml"})
	public ResponseEntity<?> signUpStudent(@RequestBody Student student, HttpServletRequest req, HttpServletResponse res){
		RequestError error = new RequestError();
		if (!dataValidation.isUserDetailsValid(student, error)) {
			System.err.println("Student tried to signup with invalid details");
			return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
		}
		
		String clientEncodedPass = student.getPassword();					// store for later use
		prepUserForStorage(student);
		
		// Students must have a major
		if (student.getMajor() == null) {
			log.info("Student tried to signup without a major");
			error.setMessage("Students must choose a major to signup");
			return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
		}
		
		// To Deal with hateoas not including Ids, use major code to find major and reassign
		Major major = majorRepo.findByMajorCodeIgnoringCase(student.getMajor().getMajorCode());
		if (major != null) {
			student.setMajor(major);
		}else {
			log.warn("Cound not find major with majorCode " + 
					student.getMajor().getMajorCode() + " while trying to register student with email '" +
					student.getEmail() + "'");
			error.setMessage("Student major not found");
			return new ResponseEntity<>(error, HttpStatus.FAILED_DEPENDENCY);
		}
		
		// Make sure user doesn't already exist
		if (userRepo.findByEmailIgnoringCase(student.getEmail()) != null) {
			log.info("Student [" + student.getEmail() + "] tried to create duplicate account");
			error.setMessage("User with email '" + student.getEmail() + "' already exists. Login instead.");
			return new ResponseEntity<>(error, HttpStatus.IM_USED);
		}
		
		// If all checks out grant STUDENT permission and save student
		UserPermission permission = permissionRepo.findByName("STUDENT");				// get student permission
		if (permission != null) {
			student.grantPermission(permission);
		}else {
			log.warn("Could not find STUDENT permission when trying to register student with email " + student.getEmail() + "!");
			error.setMessage("It's not you. It's us. We'll fix this. Try again later");
			return new ResponseEntity<>(error, HttpStatus.FAILED_DEPENDENCY);
		}
		
		studentRepo.save(student);
		
		// Generate Account Confirmation Code
		generateAccountConfirmationDetails(student);		
		
		// Create Login Profile Required for future logins
		createLoginProfile(student);
		
		// Add Authorization Header to Response
		res.setHeader("Authorization", this.dataValidation
												.composeAuthenticationHeaderValue
													(student.getEmail(), clientEncodedPass));
		
		//Manually authenticate signed up in user
		try {
			loginController.manuallyAuthenticate(new LoginDetails(student.getEmail(), clientEncodedPass), req.getSession(true));
			loginController.startUserSession(student, req.getSession(true));
		} catch (Exception e) {
			error.setMessage("It's not you. It's us. We'll fix this. Try again later");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Prepare Return Object
//		UserModel model = (new UserModelAssembler()).toModel(student);		
//		hateoasService.addUserInteractionLinks(model);
//		hateoasService.addStudentInteractionLinks(model);
		
		// prepareLoggedInUser includes all needed setting such as sessionTimeout needed by client
		return new ResponseEntity<>(loginController.prepareLoggedInUserForResponse(student),
										HttpStatus.CREATED);		
	}



	/**
	 * 
	 * @param faculty
	 * @return
	 */
	@PostMapping(path="/faculty", consumes={"application/json", "text/xml"})
	public ResponseEntity<?> signupFaculty(@RequestBody Faculty faculty, HttpServletRequest req, HttpServletResponse res){
		RequestError error = new RequestError();
		if (!dataValidation.isUserDetailsValid(faculty, error)) {
			System.err.println("Faculty tried to signup with invalid details");
			return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
		}
		
		String clientEncodedPass = faculty.getPassword();			// kept for later reuse
		prepUserForStorage(faculty);
		
		
		// Faculty must have a department
		if (faculty.getDepartment() == null) {
			log.info("Faculty with email '" + faculty.getEmail() + "' tried to signup without a department");
			error.setMessage("Faculty must choose a department to signup");
			return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
		}		
		// To Deal with hateoas not including Ids, use department code to find department and reassign
		Department department = departmentRepo.findByDepartmentCodeIgnoringCase(faculty.getDepartment().getDepartmentCode());
		if (department != null) {
			faculty.setDepartment(department);
		}else {
			log.warn("Could not find department with department code '" + 
					faculty.getDepartment().getDepartmentCode() + "' while trying to register faculty with email '" +
					faculty.getEmail() + "'");
			error.setMessage("Invalid department provided");
			return new ResponseEntity<>(error, HttpStatus.FAILED_DEPENDENCY);
		}
		
		// Make sure user doesn't already exist
		if (userRepo.findByEmailIgnoringCase(faculty.getEmail()) != null) {
			log.info("Faculty [" + faculty.getEmail() + "] tried to create duplicate account");
			error.setMessage("User with email '" + faculty.getEmail() + "' already exists. Login instead.");
			return new ResponseEntity<>(error, HttpStatus.IM_USED);
		}
		
		// If all checks out grant FACULTY permission and save student
		UserPermission permission = permissionRepo.findByName("FACULTY");				// get student permission
		if (permission != null) {
			faculty.grantPermission(permission);
		}else {
			log.warn("Could not find 'FACULTY' permission when registering faculty with email " + faculty.getEmail() + "!");
			error.setMessage("It's not you. It's us. We'll fix this. Try again later");
			return new ResponseEntity<>(error, HttpStatus.FAILED_DEPENDENCY);
		}
		
		faculty.setMenteeLimit(configuration.getDefaultFacultyMenteeLimit());
		
		facultyRepo.save(faculty);
		
		// Generate Account Confirmation Code
		generateAccountConfirmationDetails(faculty);
		
		// Create Login Profile Required for future logins
		createLoginProfile(faculty);
		
		// Add Authorization Header to Response
		res.setHeader("Authorization", this.dataValidation
												.composeAuthenticationHeaderValue
													(faculty.getEmail(), clientEncodedPass));
		
		//Manually authenticate signed up in user
		try {
			loginController.manuallyAuthenticate(new LoginDetails(faculty.getEmail(), clientEncodedPass), req.getSession(true));
			loginController.startUserSession(faculty, req.getSession(true));
		} catch (Exception e) {
			error.setMessage("It's not you. It's us. We'll fix this. Try again later");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Prep Return Object
		UserModel model = (new UserModelAssembler()).toModel(faculty);
		
		hateoasService.addUserInteractionLinks(model);
		hateoasService.addFacultyInteractionLinks(model);
		
		return new ResponseEntity<>(model , HttpStatus.CREATED);		
	}
	
	
	
	@PostMapping("/confirm")
	public ResponseEntity<?> confirmAccount(@RequestParam("confirmCode") String confirmationCode){
		RequestError error = new RequestError();
		// Get user with confirmation code
		AccountConfirmation confirmationObject = this.accountConfirmationRepo.findByConfirmCode(confirmationCode);
		if (confirmationObject == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);			// Could not find confirmation object
		
		User user = confirmationObject.getUser();			
		if (user == null) {
			error.setMessage("Confirmation code not found");
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);	//if User is not found, then return not found response
		}
		
		// Activate User Account
		user.setAccountStatus(User.AccountStatus.ACTIVE);		
		try{
			userRepo.save(user);
		}catch (Exception e) {
			log.error("Could not authenticate user with email '" + user.getEmail() + "' because it could not be saved during confirmation ");
			error.setMessage("It's not you. It's us. We'll fix this. Try again later");
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Remove authentication object
		this.accountConfirmationRepo.deleteById(confirmationObject.getConfirmationId());		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	
	
	
	// -------------------------------------- Class Private Helper Methods -------------------------------------------		
	
	/**
	 * 
	 * @param user
	 */
	private void prepUserForStorage(User user) {
		if (user == null) return;
		// Make email lower case
		user.setEmail(user.getEmail().toLowerCase());
		// Encrypt password
		user.setPassword(securityConfig
								.encoder()
									.encode(user.getPassword()));
		// Make names title case										// NOT DONE YET		
	}

	/**
	 * 
	 * @param user
	 */
	public void generateAccountConfirmationDetails(User user) {
	    AccountConfirmation confirmationDetails = generateConfirmationDetails(user);	    
	    try {
		    confirmationDetails = accountConfirmationRepo.save(confirmationDetails);
	    }catch(Exception e) {
	    	// if saving fails, try a second time
	    	try {
	    		confirmationDetails = generateConfirmationDetails(user);	    		
			    confirmationDetails = accountConfirmationRepo.save(confirmationDetails);
		    }catch(Exception exc) {
		    	log.error("Generating confirmation code for user '" + user.getEmail() + "' failed. This may be because"
		    			+ " a confirmation code that already exists was generated. Becuase this is unlikely, code "
		    			+ " base does not fully protect against it. Resolve immediatetely to enable user signup.");
		    	return;
		    }
	    }
	    
	    if (confirmationDetails == null) {
	    	log.error("Unable to store Account Confirmation details for user '" + user.getEmail() 
	    				+	"' during signup or 'forgotPassword'");
	    }
	}
	
	
	private AccountConfirmation generateConfirmationDetails(User user) {
		// Generate Random 20 character String and encode it
		byte[] array = new byte[20]; // length is bounded by 7
	    new Random().nextBytes(array);
	    String generatedString = new String(array, Charset.forName("UTF-8"));
	    
	    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    
	    String encodedConfirmationString = passwordEncoder.encode(generatedString);			// Double Secure.... Encoded Randomness. AWESOME !!
	    
	    AccountConfirmation confirmationDetails = new AccountConfirmation(encodedConfirmationString, user);	    
	    return confirmationDetails;
	}
	
	
	/**
	 * 
	 * @param user
	 */
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
