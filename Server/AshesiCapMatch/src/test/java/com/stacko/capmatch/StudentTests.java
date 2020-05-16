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
import com.stacko.capmatch.Models.Major;
import com.stacko.capmatch.Models.SDG;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Repositories.StudentRepository;
import com.stacko.capmatch.Security.UserPermission;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql= true)
class StudentTests {
	
	@Autowired
	StudentRepository studentRepo;
	
	@Before
	public void init() {
		// Nothing here yet. Autowired beans used
	}

	@Test
	void createAndLoadTest() {
		// Create Major and corresponding department
		String majorCode1 = "CS";
		String majorName = "Computer Science";		
		Major major1 = new Major(majorCode1, majorName);
		
		String dCode = "CSIS";
		String dname = "Computer Science and Management Systems";		
		Department department = new Department(dCode, dname);
		
		major1.bidirectionalSetDepartement(department);

		
		
		// Create permissions
		String pname1 = "student";		
		UserPermission permission1 = new UserPermission(pname1);		
		String pname2 = "admin";		
		UserPermission permission2 = new UserPermission(pname2);
		
		
		// Create student
		String firstname = "Owusu-Banahene";
		String lastname = "Osei";
		String email = "bana@gmail.com";
		String password = "password98";
		
		Student student = new Student(firstname, lastname, email, password);
		student.setMajor(major1);
		
		student.grantPermission(permission1);
		student.grantPermission(permission2);
		
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
		student.addInterest(interest1);
		student.addInterest(interest2);
		student.addInterest(interest1); 			// add again to check for duplicates
		student.addInterest(null);
		
		
		
		// Add SDGs
		String sdgName1 = "Freedom from poverty";
		String sdgDescription1 = "The goal is to make sure no human ever is imprisoned";
		SDG sdg1 = new SDG(1, sdgName1, sdgDescription1);
		
		String sdgName2 = "Universal Healthcare";
		String sdgDescription2 = "Everyone goes to hospital for free";
		SDG sdg2 = new SDG(2, sdgName2, sdgDescription2);
		
		student.addSDG(sdg1);
		student.addSDG(sdg2);
		student.addSDG(sdg2);					// test duplicate handling
		student.addSDG(null);					// test null entry

		
		// Create faculty and make faculty supervise student
		String f_firstname = "Owusu-Banahene";
		String f_lastname = "Osei";
		String f_email = "fool@gmail.com";
		String f_password = "password98";
		int f_menteeLimit = 5;
		
		Faculty faculty = new Faculty(f_firstname, f_lastname, f_email, f_password, f_menteeLimit);
		
		student.bidirectionalSetSupervisor(faculty);			// make faculty supervise student
		
		studentRepo.save(student);
		
		System.err.println("Student id: " + student.getUserId());
		
		student = studentRepo.findById(student.getUserId()).get();
		
		assertNotNull(student);		
		assertEquals(firstname, student.getFirstname());
		assertEquals(lastname, student.getLastname());
		assertEquals(email, student.getEmail());
		assertEquals(password, student.getPassword());
		
		assertNotNull(student.getMajor());
		System.err.println("Student's major id is: " + student.getMajor().getMajorId());
		assertEquals(major1, student.getMajor());
		
		assertEquals(2, student.getInterests().size());
		System.err.println("Number of interests: " + student.getInterests().size());
		assertTrue(student.getInterests().contains(interest1));
		assertTrue(student.getInterests().contains(interest2));

		assertEquals(2, student.getPermissions().size());
		assertTrue(student.getPermissions().contains(permission1));
		assertTrue(student.getPermissions().contains(permission2));
		
		assertEquals(2, student.getSDGs().size());
		assertTrue(student.getSDGs().contains(sdg1));
		assertTrue(student.getSDGs().contains(sdg2));
		
		assertNotNull(student.getSupervisor());
		assertEquals(faculty, student.getSupervisor());
		assertEquals(1, faculty.getSupervisedStudents().size());
		
		// Remove supervisor
		student.removeSupervisor();
		
		studentRepo.save(student);
		student = studentRepo.findById(student.getUserId()).get();
		assertNotNull(student);
		assertTrue(student.getSupervisor() == null);
		assertEquals(0, faculty.getSupervisedStudents().size());		
	}
	
	
	@Test
	void constraintsTest() {
		String firstname = "Owusu-Banahene";
		String lastname = "Osei";
		String email = "bana@gmail.com";
		String password = "password98";
		
		Student student = new Student(firstname, lastname, email, password);
		
		
		studentRepo.save(student);
		
		student = studentRepo.findById(student.getUserId()).get();
		
		assertNotNull(student);
		assertNull(student.getMajor());
	}
	
	
	@Test
	void testFavouriteSelection() {
		// Create student
		String firstname = "Owusu-Banahene";
		String lastname = "Osei";
		String email = "bana@gmail.com";
		String password = "password98";		
		Student student = new Student(firstname, lastname, email, password);
		
		String f_firstname1 = "Owusu-Banahene";
		String f_lastname1 = "Osei";
		String f_email1 = "fool@gmail.com";
		String f_password1 = "password98";
		int f_menteeLimit1 = 5;		
		Faculty faculty1 = new Faculty(f_firstname1, f_lastname1, f_email1, f_password1, f_menteeLimit1);
		
		String f_firstname2 = "Abena";
		String f_lastname2 = "Boakye";
		String f_email2 = "abena@gmail.com";
		String f_password2 = "abn34stt";
		int f_menteeLimit2 = 6;		
		Faculty faculty2 = new Faculty(f_firstname2, f_lastname2, f_email2, f_password2, f_menteeLimit2);
		
		
		student.addFacultyFavourite(faculty1);
		student.addFacultyFavourite(faculty1);						// test duplication
		student.addFacultyFavourite(faculty2);
		student.addFacultyFavourite(null);							// test null entry
		
		
		studentRepo.save(student);
		
		student = studentRepo.findById(student.getUserId()).get();
		assertNotNull(student);		
		
		assertEquals(2, student.getFavouriteSupervisors().size());
		assertTrue(student.getFavouriteSupervisors().contains(faculty2));
		assertTrue(student.getFavouriteSupervisors().contains(faculty1));
		
		
		// Remove faculty 2 from favorites
		student.removeFacultyFavourite(faculty1);
		student.removeFacultyFavourite(faculty1);				// test value not in favorites
		student.removeFacultyFavourite(null);					// null test
		
		studentRepo.save(student);		
		student = studentRepo.findById(student.getUserId()).get();
		assertNotNull(student);

		assertEquals(1, student.getFavouriteSupervisors().size());
		assertTrue(student.getFavouriteSupervisors().contains(faculty2));
		assertTrue(!student.getFavouriteSupervisors().contains(faculty1));			// faculty1 should not be a favourite
	}

}
