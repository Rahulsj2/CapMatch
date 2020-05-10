package com.stacko.capmatch.Repositories;

import org.springframework.data.repository.CrudRepository;
import com.stacko.capmatch.Models.Department;

public interface DepartmentRepository extends CrudRepository<Department, Integer> {
	public Department findByDepartmentCodeIgnoringCase(String departmentCode);

}
