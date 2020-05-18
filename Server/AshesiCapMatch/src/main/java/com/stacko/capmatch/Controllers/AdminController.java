package com.stacko.capmatch.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stacko.capmatch.Models.Department;
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
	public ResponseEntity<?> initiateDepartmentMatching(Department department){		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PostMapping(path="/match/department/{departmentCode}")
	public ResponseEntity<?> initiateDepartmentMatchingWithDepartmentCode
									(@PathVariable("departmentCode") String departmentCode){
		// Try to find department
		
		return null;		
	}
	
	
	@PostMapping(path="match/all")
	public ResponseEntity<?> intitiateAllDepartmentMatching(){
		return null;		
	}

}
