package com.stackO.capmatch.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.stackO.capmatch.Models.Student;

public interface StudentRepository extends CrudRepository<Student, Integer> {
	
	public Student findByEmailIgnoringCase(String email);

}
