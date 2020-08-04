
package com.stacko.capmatch.Controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
import com.stacko.capmatch.Models.RequestError;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Models.HATEOAS.FacultyModel;
import com.stacko.capmatch.Models.HATEOAS.UserModel;
import com.stacko.capmatch.Models.HATEOAS.Assemblers.FacultyModelAssembler;
import com.stacko.capmatch.Repositories.FacultyRepository;
import com.stacko.capmatch.Repositories.StudentRepository;
import com.stacko.capmatch.Services.HATEOASService;

//@Slf4j
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
		List<Faculty> allActiveFaculty = new ArrayList<>();
		for (Faculty faculty : allFaculty){			// Only browse active faculty
			if (faculty != null && faculty.getAccountStatus().equals(User.AccountStatus.ACTIVE))
				allActiveFaculty.add(faculty);
		}
		
		Iterable<FacultyModel> modeledFaculty = new FacultyModelAssembler().toCollectionModel(allActiveFaculty);
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
				return new ResponseEntity<>(new RequestError("Students cannot have more than " + appConfig.getStudentMaxFavouriteCount()
																	+ " faculty they prefer. Remove some preferences first"), HttpStatus.NOT_ACCEPTABLE);			// favs full
			}else {
				student.addFacultyFavourite(favFaculty);
				studentRepo.save(student);
				return new ResponseEntity<>(null, HttpStatus.OK);
			}				
		}
	}
	
	
	@GetMapping("/{id}/supervisor")
	public ResponseEntity<?> getStudentSupervisor(@PathVariable("id") int studentId, Principal principal){
		Student student = studentRepo.findByEmailIgnoringCase(principal.getName());
		
		if (student == null)
			return new ResponseEntity<>(new RequestError("Could not find student account with email '" + principal.getName() + "'"), HttpStatus.NOT_ACCEPTABLE);
		
//		List<Faculty> faculties = new ArrayList<>();
		
//		if (student.getSupervisor() != null) {
//			faculties.add(student.getSupervisor());	
//		}	
		
		List<FacultyModel> modeledFaculty = new ArrayList<>();
		if (student.getSupervisor() != null)
			modeledFaculty.add(new FacultyModelAssembler().toModel(student.getSupervisor()));
		return new ResponseEntity<>(modeledFaculty, HttpStatus.OK);	
//		return new ResponseEntity<>(new FacultyModelAssembler().toCollectionModel(faculties), HttpStatus.OK);
	}
	

}
