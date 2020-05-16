package com.stacko.capmatch;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.stacko.capmatch.Models.Department;
import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.Major;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Repositories.DepartmentRepository;
import com.stacko.capmatch.Repositories.MajorRepository;


@RunWith(SpringRunner.class)
@DataJpaTest(showSql= true)
class MajorAndDepartmentTests {
	
	@Autowired
	private MajorRepository majorRepo;
	
	@Autowired
	private DepartmentRepository departmentRepo;
	
	@Before
	public void init() {
		// Nothing here yet. Autowired beans used
	}

	@Test
	void majorTest() {
		String majorCode1 = "CS";
		String name1 = "Computer Science";
		String majorCode2 = "BA";
		String name2 = "Business Administration";		
		Major major1 = new Major(majorCode1, name1);
		Major major2 = new Major(majorCode2, name2);
		
		String dCode = "CSIS";
		String dname = "Computer Science and Management Systems";		
		Department department = new Department(dCode, dname);
		
		major1.bidirectionalSetDepartement(department);
		major2.bidirectionalSetDepartement(department);
		
		String firstname = "Owusu-Banahene";
		String lastname = "Osei";
		String email = "bana@gmail.com";
		String password = "password98";
		
		Student student = new Student(firstname, lastname, email, password);
		
		major1.addStudent(student);
		
		
		majorRepo.save(major1);
		majorRepo.save(major2);

		major1 = majorRepo.findById(major1.getMajorId()).get();
		major2 = majorRepo.findById(major2.getMajorId()).get();
		
//		department = departmentRepo.findById(department.getDepartmentId()).get();
		
		assertNotNull(major1);
		assertEquals(major1.getMajorCode(), majorCode1);
		assertEquals(major1.getName(), name1);	
		assertNotNull(major1.getDepartment());
		assertEquals(department, major1.getDepartment());
		assertEquals(1, major1.getStudents().size());
		assertTrue(major1.getStudents().contains(student));
		
		assertNotNull(major2);
		assertEquals(major2.getMajorCode(), majorCode2);
		assertEquals(major2.getName(), name2);	
		assertNotNull(major2.getDepartment());
		assertEquals(department, major2.getDepartment());
		assertEquals(0, major2.getStudents().size());

		
		assertNotNull(department);
		assertTrue(department.getMajors().contains(major1));
		assertTrue(department.getMajors().contains(major2));
		assertEquals(2, department.getMajors().size());
	}
	
	
	@Test
	void departmentTest() {
		
		
		String majorCode1 = "CS";
		String name1 = "Computer Science";
		String majorCode2 = "BA";
		String name2 = "Business Administration";		
		Major major1 = new Major(majorCode1, name1);
		Major major2 = new Major(majorCode2, name2);
		
		String dCode = "CSIS";
		String name = "Computer Science and Management Systems";		
		Department department = new Department(dCode, name);
		
		department.addMajor(major1);
		department.addMajor(major2);
		
		department.addMajor(major1);		// Added to test duplicate handling
		
		departmentRepo.save(department);
		
		System.err.println("Major1 id: " + major1.getMajorId());
		System.err.println("Major2 id: " + major2.getMajorId());

		
		
		department = departmentRepo.findById(department.getDepartmentId()).get();
		
		assertNotNull(department);
		assertEquals(dCode, department.getDepartmentCode());
		assertEquals(name, department.getName());
		assertNotNull(department.getMajors());
		assertEquals(2, department.getMajors().size());
				
		assertTrue(department.getMajors().contains(major1));
		assertTrue(department.getMajors().contains(major2));
				
		assertNotNull(majorRepo.findById(major1.getMajorId()).get());
		assertNotNull(majorRepo.findById(major2.getMajorId()).get());
	}
	
	
	@Test
	void departmentAddRemoveFaculty() {
		String dCode = "CSIS";
		String name = "Computer Science and Management Systems";		
		Department department = new Department(dCode, name);
		
		String firstname1 = "Micheal";
		String lastname1 = "Bennett";
		String email1 = "ugly@gmail.com";
		String password1 = "wtfiswrongWithyou";
		int menteeLimit = 5;		
		Faculty faculty1 = new Faculty(firstname1, lastname1, email1, password1, menteeLimit);
		
		
		String firstname2 = "Owusu-Banahene";
		String lastname2 = "Osei";
		String email2 = "bana@gmail.com";
		String password2 = "password98";		
		Faculty faculty2 = new Faculty(firstname2, lastname2, email2, password2, menteeLimit);
				
		department.addFaculty(faculty1);
		department.addFaculty(faculty2);
		department.addFaculty(faculty1);			//test duplicate
		department.addFaculty(null);				// test null entry

		departmentRepo.save(department);		
		department = departmentRepo.findById(department.getDepartmentId()).get();
		
		assertNotNull(department);
		assertEquals(2, department.getFaculty().size());
		assertTrue(department.getFaculty().contains(faculty1));
		assertTrue(department.getFaculty().contains(faculty1));
		
		assertEquals(department, faculty1.getDepartment());
		assertEquals(department, faculty2.getDepartment());
		
		
		department.removeFaculty(faculty2);
		
		departmentRepo.save(department);		
		department = departmentRepo.findById(department.getDepartmentId()).get();
		
		assertEquals(1, department.getFaculty().size());	
		
	}

}
