package com.stacko.capmatch.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.stacko.capmatch.Models.Student;

public interface StudentRepository extends CrudRepository<Student, Integer> {
	
	public Student findByEmailIgnoringCase(String email);

}
