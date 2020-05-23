package com.stacko.capmatch.Controllers;

import java.security.Principal;
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
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.HATEOAS.UserModel;
import com.stacko.capmatch.Models.HATEOAS.Assemblers.UserModelAssembler;
import com.stacko.capmatch.Repositories.FacultyRepository;
import com.stacko.capmatch.Repositories.StudentRepository;
import com.stacko.capmatch.Services.HATEOASService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/faculties")
@CrossOrigin(origins="*")
public class FacultyController {
	
	
	@Autowired
	StudentRepository studentRepo;
	
	@Autowired
	FacultyRepository facultyRepo;
	
	@Autowired
	HATEOASService hateoasService;
	
	@Autowired
	AppConfig appConfig;
	
	@GetMapping(path="browseStudents")
	public ResponseEntity<?> browseStudents(Principal principal){
		Faculty faculty = facultyRepo.findByEmailIgnoringCase(principal.getName());
		if (faculty == null)
			return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
		
		Iterable<Student> allStudents = studentRepo.findAll();		
		Iterable<UserModel> modeledStudents = new UserModelAssembler().toCollectionModel(allStudents);
		for (UserModel model: modeledStudents) {
			hateoasService.addBrowsedStudentLinks(model, faculty);
		}
		
		return new ResponseEntity<>(modeledStudents, HttpStatus.OK);	
	}
	
	
	@PostMapping("/addFavouriteStudent/{studentId}")
	public ResponseEntity<?> addFavouriteStudent(@PathVariable("studentId") int studentId, Principal principal){
		Faculty faculty = facultyRepo.findByEmailIgnoringCase(principal.getName());
		if (faculty == null)
			return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
		
		Student studentFav;
		try{
			studentFav = studentRepo.findById(studentId).get();
		}catch (NoSuchElementException nse) {
			return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY);
		}
		
		if (faculty.getFavouriteStudents().contains(studentFav)) {
			return new ResponseEntity<>(null, HttpStatus.OK);			// Already contained
		}else {
			if (faculty.getFavouriteStudents().size() >= this.appConfig.getFacultyMaxFavouriteCount()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);			// favs full
			}else {
				faculty.addStudentFavourite(studentFav);
				return new ResponseEntity<>(null, HttpStatus.OK);
			}				
		}
	}

}
