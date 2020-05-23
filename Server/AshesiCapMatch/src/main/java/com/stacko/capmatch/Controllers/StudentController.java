
package com.stacko.capmatch.Controllers;

import java.security.Principal;
import java.util.NoSuchElementException;

//import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stacko.capmatch.Configuration.AppConfig;
import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.HATEOAS.UserModel;
import com.stacko.capmatch.Models.HATEOAS.Assemblers.UserModelAssembler;
import com.stacko.capmatch.Repositories.FacultyRepository;
import com.stacko.capmatch.Repositories.StudentRepository;
import com.stacko.capmatch.Services.HATEOASService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/students")
@CrossOrigin(origins="*")
public class StudentController {
	
	@Autowired
	HATEOASService hateoasService;
	
	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private FacultyRepository facultyRepo;
	
	@Autowired
	private AppConfig appConfig;
	
	
	@GetMapping(path="browseFaculty")
	public ResponseEntity<?> browseFaculty(Principal principal){
		Student student = studentRepo.findByEmailIgnoringCase(principal.getName());
		if (student == null)
			return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
		
		Iterable<Faculty> allFaculty = facultyRepo.findAll();
		Iterable<UserModel> modeledFaculty = new UserModelAssembler().toCollectionModel(allFaculty);
		for (UserModel model: modeledFaculty) {
			hateoasService.addBrowsedFacultyLinks(model, student);
		}
		
		return new ResponseEntity<>(modeledFaculty, HttpStatus.OK);	
	}	
	
	
	@PostMapping("/addFavouriteFaculty/{facultyId}")
	public ResponseEntity<?> addFavouriteFaculty(@PathVariable("facultyId") int facultyId, Principal principal){
		Student student = studentRepo.findByEmailIgnoringCase(principal.getName());
		if (student == null)
			return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
		
		Faculty favFaculty;
		try{
			favFaculty = facultyRepo.findById(facultyId).get();
		}catch (NoSuchElementException nse) {
			return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
		}
		
		if (student.getFavouriteSupervisors().contains(favFaculty)) {
			return new ResponseEntity<>(null, HttpStatus.OK);			// Already contained
		}else {
			if (student.getFavouriteSupervisors().size() >= appConfig.getStudentMaxFavouriteCount()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);			// favs full
			}else {
				student.addFacultyFavourite(favFaculty);
				return new ResponseEntity<>(null, HttpStatus.OK);
			}				
		}
	}
	

}
