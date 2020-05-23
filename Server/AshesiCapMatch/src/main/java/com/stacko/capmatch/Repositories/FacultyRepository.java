package com.stacko.capmatch.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.stacko.capmatch.Models.Faculty;

public interface FacultyRepository extends CrudRepository<Faculty, Integer> {

	Faculty findByEmailIgnoringCase(String email);

}
