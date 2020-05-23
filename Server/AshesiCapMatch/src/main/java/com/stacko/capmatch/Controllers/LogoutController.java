package com.stacko.capmatch.Controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j
@Controller
@RequestMapping("/logout")
@CrossOrigin(origins="*")
public class LogoutController {
	
	@PostMapping
	@PutMapping
	@GetMapping
	public ResponseEntity<?> logout(HttpServletRequest req){		
		HttpSession currentSession = req.getSession(false);		
		if (currentSession == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		
		currentSession.invalidate();
		return new ResponseEntity<>(null, HttpStatus.OK);		
	}

}
