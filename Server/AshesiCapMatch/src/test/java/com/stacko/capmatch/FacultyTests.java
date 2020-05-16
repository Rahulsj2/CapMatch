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
import com.stacko.capmatch.Models.Interest;
import com.stacko.capmatch.Models.InterestCategory;
import com.stacko.capmatch.Models.SDG;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Repositories.FacultyRepository;
import com.stacko.capmatch.Security.UserPermission;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql= true)
class FacultyTests {
	
	@Autowired
	private FacultyRepository facultyRepo;
	
	@Before
	public void init() {
		// Nothing here yet. Autowired beans used
	}
	

	@Test
	void storeAndLoadTest() {
		// Create permissions
		String pname1 = "student";		
		UserPermission permission1 = new UserPermission(pname1);		
		String pname2 = "admin";		
		UserPermission permission2 = new UserPermission(pname2);
		
		String firstname = "Owusu-Banahene";
		String lastname = "Osei";
		String email = "bana@gmail.com";
		String password = "password98";
		int menteeLimit = 5;
		
		Faculty faculty = new Faculty(firstname, lastname, email, password, menteeLimit);
		
		faculty.grantPermission(permission1);
		faculty.grantPermission(permission2);
		
		// Create interest and set corresponding category
		String name1 = "NLP";
		String description1 = "This is about languages";		
		Interest interest1 = new Interest(name1, description1);
		
		String name2 = "Early Childhood Education";
		String description2 = "Help children learn an play. Build tomorrow today";		
		Interest interest2 = new Interest(name2, description2);
		
		String cname = "Technology";
		String cdescription = "Everything from Robotics to Artificial Intelligence";
		InterestCategory category = new InterestCategory(cname, cdescription);
		
		interest1.bidirectionalSetCategory(category);
		interest2.bidirectionalSetCategory(category);

		
		// Add interest to user
		faculty.addInterest(interest1);
		faculty.addInterest(interest2);
		faculty.addInterest(interest1); 			// add again to check for duplicates
		faculty.addInterest(null);
		
		
		
		// Add SDGs
		String sdgName1 = "Freedom from poverty";
		String sdgDescription1 = "The goal is to make sure no human ever is imprisoned";
		SDG sdg1 = new SDG(1, sdgName1, sdgDescription1);
		
		String sdgName2 = "Universal Healthcare";
		String sdgDescription2 = "Everyone goes to hospital for free";
		SDG sdg2 = new SDG(2, sdgName2, sdgDescription2);
		
		faculty.addSDG(sdg1);
		faculty.addSDG(sdg2);
		faculty.addSDG(sdg2);					// test duplicate handling
		faculty.addSDG(null);					// test null entry
		
		
		// Make faculty supervise students
		String student_firstname1 = "Owusu-Banahene";
		String student_lastname1 = "Osei";
		String student_email1 = "forlan@ashesi.edu.gh";
		String student_password1 = "password98";		
		Student student1 = new Student(student_firstname1, student_lastname1, student_email1, student_password1);
		
		String student_firstname2 = "Adeapena";
		String student_lastname2 = "Osei";
		String student_email2 = "abena.serwaa@ashesi.edu.gh";
		String student_password2 = "password98";		
		Student student2 = new Student(student_firstname2, student_lastname2, student_email2, student_password2);
		
		faculty.superviseStudent(student1);
		faculty.superviseStudent(student2);
		faculty.superviseStudent(student2);				// test duplication
		faculty.superviseStudent(null);					// test null entry
		
		
		// faculty selects favorite students
		faculty.addStudentFavourite(student2);
		faculty.addStudentFavourite(student2);			// test duplication
		faculty.addStudentFavourite(student1);
		faculty.addStudentFavourite(null);				// test null entry
		
		
		// Create and set department
		String dCode = "CSIS";
		String dname = "Computer Science and Management Systems";		
		Department department = new Department(dCode, dname);
		
		faculty.joinDepartment(department);		
		facultyRepo.save(faculty);
		
		System.err.println("Faculty id: " + faculty.getUserId());
		
		faculty = facultyRepo.findById(faculty.getUserId()).get();
		
		assertNotNull(faculty);		
		assertEquals(firstname, faculty.getFirstname());
		assertEquals(lastname, faculty.getLastname());
		assertEquals(email, faculty.getEmail());
		assertEquals(password, faculty.getPassword());
		assertEquals(menteeLimit, faculty.getMenteeLimit());
		
		assertEquals(2, faculty.getInterests().size());
		System.err.println("Number of interests: " + faculty.getInterests().size());
		assertTrue(faculty.getInterests().contains(interest1));
		assertTrue(faculty.getInterests().contains(interest2));

		assertEquals(2, faculty.getPermissions().size());
		assertTrue(faculty.getPermissions().contains(permission1));
		assertTrue(faculty.getPermissions().contains(permission2));
		
		assertEquals(2, faculty.getSDGs().size());
		assertTrue(faculty.getSDGs().contains(sdg1));
		assertTrue(faculty.getSDGs().contains(sdg2));
		
		// check supervision status
		assertEquals(2, faculty.getSupervisedStudents().size());
		assertTrue(faculty.getSupervisedStudents().contains(student1));
		assertTrue(faculty.getSupervisedStudents().contains(student2));
		assertNotNull(student1.getSupervisor());
		assertEquals(faculty, student1.getSupervisor());
		assertNotNull(student2.getSupervisor());
		assertEquals(faculty, student2.getSupervisor());
		
		// check department
		assertNotNull(faculty.getDepartment());
		assertEquals(faculty.getDepartment(), department);
		assertTrue(department.getFaculty().contains(faculty));
		assertEquals(1, department.getFaculty().size());
		
		// check favorite students
		assertEquals(2, faculty.getFavouriteStudents().size());
		assertTrue(faculty.getFavouriteStudents().contains(student1));
		assertTrue(faculty.getFavouriteStudents().contains(student2));
		
		
		// Stop supervising student1
		faculty.stopSupervisingStudent(student1);
		// Leave department
		faculty.leaveDepartment();
		//remove student 1 from being a favourite
		faculty.removeStudentFavourite(student1);
		
		facultyRepo.save(faculty);
		faculty = facultyRepo.findById(faculty.getUserId()).get();
		assertNotNull(faculty);
		
		assertEquals(1, faculty.getSupervisedStudents().size());
		assertTrue(!faculty.getSupervisedStudents().contains(student1));
		assertTrue(student1.getSupervisor() == null);
		
		//Check department
		assertNull(faculty.getDepartment());
		assertEquals(0, department.getFaculty().size());
		
		// Check that student1 was removed from favorites
		assertEquals(1, faculty.getFavouriteStudents().size());
		assertTrue(!faculty.getFavouriteStudents().contains(student1));				// make sure favorites don't include student1
		assertTrue(faculty.getFavouriteStudents().contains(student2));				// make sure student2 is still a favorite
	}

}
