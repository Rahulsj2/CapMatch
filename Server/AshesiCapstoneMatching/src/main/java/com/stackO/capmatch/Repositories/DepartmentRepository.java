package com.stackO.capmatch.Repositories;

import org.springframework.data.repository.CrudRepository;
import com.stackO.capmatch.Models.Department;

public interface DepartmentRepository extends CrudRepository<Department, Integer> {
	public Department findByDepartmentCodeIgnoringCase(String departmentCode);

}
