package com.stacko.capmatch.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stacko.capmatch.Models.Department;
import com.stacko.capmatch.Models.RequestError;
import com.stacko.capmatch.Repositories.DepartmentRepository;
import com.stacko.capmatch.Services.MatchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
@CrossOrigin(origins="*")
public class AdminController {
	
	@Autowired
	MatchService matchService;
	
	@Autowired
	DepartmentRepository departmentRepo;	
	
	@PostMapping(path="/match/department")
	public ResponseEntity<?> initiateDepartmentMatching(@RequestBody Department department){
		if (department == null)
			return new ResponseEntity<>(new RequestError("A department must be specified in the request body"), HttpStatus.NOT_ACCEPTABLE);

		String dCode = department.getDepartmentCode();		
		department = departmentRepo.findByDepartmentCodeIgnoringCase(department.getDepartmentCode());
		if (department == null)
			return new ResponseEntity<>(new RequestError("Could not find a deparment with code '" + dCode + "'"), HttpStatus.NOT_FOUND);
		
		matchService.MatchDepartment(department);
		log.info("Students and Faculty in the " + department.getName() + " have been matched.");
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PostMapping(path="/match/department/{departmentCode}")
	public ResponseEntity<?> initiateDepartmentMatchingWithDepartmentCode
									(@PathVariable("departmentCode") String departmentCode){
		if (departmentCode != null) {
			Department department = departmentRepo.findByDepartmentCodeIgnoringCase(departmentCode);
			if (department == null)
				return new ResponseEntity<>(
										new RequestError("The specified department code '" + departmentCode + "' could not be found."),
										HttpStatus.NOT_ACCEPTABLE);
			initiateDepartmentMatching(department);
		}		
		return new ResponseEntity<>(null, HttpStatus.OK);		
	}
	
	
	@PostMapping(path="match/all")
	public ResponseEntity<?> intitiateAllDepartmentMatching(){
		for (Department department: departmentRepo.findAll())
			initiateDepartmentMatching(department);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}
