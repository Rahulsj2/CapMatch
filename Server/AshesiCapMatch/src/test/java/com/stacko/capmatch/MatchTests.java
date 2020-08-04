package com.stacko.capmatch;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.stacko.capmatch.Models.Department;
import com.stacko.capmatch.Models.Faculty;
import com.stacko.capmatch.Models.Interest;
import com.stacko.capmatch.Models.Major;
import com.stacko.capmatch.Models.SDG;
import com.stacko.capmatch.Models.Student;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Repositories.DepartmentRepository;
import com.stacko.capmatch.Repositories.FacultyRepository;
import com.stacko.capmatch.Repositories.InterestRepository;
import com.stacko.capmatch.Repositories.MajorRepository;
import com.stacko.capmatch.Repositories.SDGRepository;
import com.stacko.capmatch.Repositories.StudentRepository;
import com.stacko.capmatch.Services.MatchService;


@RunWith(SpringRunner.class)
//@DataJpaTest(showSql= true)
@SpringBootTest
class MatchTests {
	
	@Autowired
	DepartmentRepository departmentRepo;
	
	@Autowired
	MatchService matchService;
	
	@Autowired
	MajorRepository majorRepo;
	
	@Autowired
	InterestRepository interestRepo;
	
	@Autowired
	SDGRepository sdgRepo;
	
	@Autowired
	StudentRepository studentRepo;

	@Autowired
	FacultyRepository facultyRepo;
	
	@Before
	public void init() {
		// Nothing here yet. Autowired beans used
	}

	@Test
	void test() {
		String name1 = "NLP";
		String description1 = "This is about languages";
		String name2 = "Big Data";
		String description2 = "Let's look at DATA";
		String name3 = "Machine Learing";
		String description3 = "Make computers not so dumb";
		String name4 = "Algorithms";
		String description4 = "Learn to be clever with code";
		
		Interest interest1 = new Interest(name1, description1);
		Interest interest2 = new Interest(name2, description2);
		Interest interest3 = new Interest(name3, description3);
		Interest interest4 = new Interest(name4, description4);
		
		String sdgName = "Freedom from poverty";
		String sdgDescription = "The goal is to make sure no human ever is imprisoned";
		SDG sdg1 = new SDG(1, sdgName, sdgDescription);
		
		
		// Create student
		String firstname = "Owusu-Banahene";
		String lastname = "Osei";
		String email = "bana@gmail.com";
		String password = "password98";		
		Student student = new Student(firstname, lastname, email, password);
		
		String ffirstname = "Isaac";
		String flastname = "Dogbe";
		String femail = "isaac@gmail.com";
		String fpassword = "password98";
		int fmenteeLimit = 5;
		
		Faculty faculty = new Faculty(ffirstname, flastname, femail, fpassword, fmenteeLimit);
		
//		student.addFacultyFavourite(faculty);
		faculty.addStudentFavourite(student);
		
		student.addInterest(interest1);
		student.addInterest(interest2);
		student.addInterest(interest3);
		
		student.addSDG(sdg1);
		
		faculty.addInterest(interest2);
		faculty.addInterest(interest3);
		faculty.addInterest(interest4);
		
		faculty.addSDG(sdg1);
		
		
		System.out.println("Similarity Index: " + this.matchService.getSimilarityIndex(faculty, student));
	}
	
	
	@Test
	void matchingTest() {
		// Add Interests
		String name1 = "NLP";
		String description1 = "This is about languages";
		String name2 = "Big Data";
		String description2 = "Let's look at DATA";
		String name3 = "Machine Learing";
		String description3 = "Make computers not so dumb";
		String name4 = "Algorithms";
		String description4 = "Learn to be clever with code";
		
		Interest interest1 = new Interest(name1, description1);
		Interest interest2 = new Interest(name2, description2);
		Interest interest3 = new Interest(name3, description3);
		Interest interest4 = new Interest(name4, description4);
		
//		interestRepo.save(interest1);
//		interestRepo.save(interest2);
//		interestRepo.save(interest3);
//		interestRepo.save(interest4);
		
		
		String majorCode1 = "CS";
		String mname1 = "Computer Science";
		String majorCode2 = "MIS";
		String mname2 = "Management Information Systems";		
		Major major1 = new Major(majorCode1, mname1);
		Major major2 = new Major(majorCode2, mname2);
		
		String dCode = "CSIS";
		String dname = "Computer Science and Management Systems";		
		Department department = new Department(dCode, dname);
		
		department.addMajor(major1);
		department.addMajor(major2);
		
//		departmentRepo.save(department);			// Save department, and majors indirectly
//		majorRepo.save(major1);
//		majorRepo.save(major2);
		
		// Add Faculty
		String firstname1 = "Micheal";
		String lastname1 = "Bennett";
		String email1 = "ugly@gmail.com";
		String password1 = "wtfiswrongWithyou";
		int menteeLimit = 1;		
		Faculty faculty1 = new Faculty(firstname1, lastname1, email1, password1, menteeLimit);
		faculty1.setAccountStatus(User.AccountStatus.ACTIVE);
		
		String firstname2 = "Owusu-Banahene";
		String lastname2 = "Osei";
		String email2 = "bana@gmail.com";
		String password2 = "password98";		
		Faculty faculty2 = new Faculty(firstname2, lastname2, email2, password2, menteeLimit);
		faculty2.setAccountStatus(User.AccountStatus.ACTIVE);
		
		department.addFaculty(faculty1);
		department.addFaculty(faculty2);
		
//		facultyRepo.save(faculty1);
//		facultyRepo.save(faculty2);
		
		faculty1.addInterest(interest1);
		faculty1.addInterest(interest2);
		
		faculty2.addInterest(interest1);
		faculty2.addInterest(interest3);
		faculty2.addInterest(interest4);
		
//		facultyRepo.save(faculty1);				// save both faculty
//		facultyRepo.save(faculty2);
		
		
		// Add Students
		String student_firstname1 = "Diego";
		String student_lastname1 = "Forlan";
		String student_email1 = "forlan@ashesi.edu.gh";
		String student_password1 = "password98";		
		Student student1 = new Student(student_firstname1, student_lastname1, student_email1, student_password1);		
		student1.bidirectionalSetMajor(major1);
		student1.setAccountStatus(User.AccountStatus.ACTIVE);
		
		String student_firstname2 = "Adeapena";
		String student_lastname2 = "Osei";
		String student_email2 = "abena.serwaa@ashesi.edu.gh";
		String student_password2 = "password98";		
		Student student2 = new Student(student_firstname2, student_lastname2, student_email2, student_password2);
		student2.bidirectionalSetMajor(major1);
		student2.setAccountStatus(User.AccountStatus.ACTIVE);

		
		student1.addInterest(interest1);
		student1.addInterest(interest3);
		student1.addInterest(interest4);
		
//		student1.addFacultyFavourite(faculty2); 	
		faculty2.addStudentFavourite(student2);				// Both students have same similarities as Faculty2 (Owusu-Banahene) but he prefers student2 (Adeapena)
															
		
		student2.addInterest(interest1);
		student2.addInterest(interest3);					// student 2 has more in common with faculty2 and has no favourites
		student2.addInterest(interest4);					// student 2 should be paired with faculty 2	
		
//		studentRepo.save(student1);
//		studentRepo.save(student2);	
		
		departmentRepo.save(department);
		
		
		this.matchService.MatchDepartment(department);
		
		
		
		student1 = studentRepo.findById(student1.getUserId()).get();
		student2 = studentRepo.findById(student2.getUserId()).get();
		
		assertNotNull(student1);
		assertNotNull(student2);
		assertNotNull(student1.getSupervisor());
		assertNotNull(student1.getSupervisor());
		
		faculty1 = facultyRepo.findById(faculty1.getUserId()).get();
		faculty2 = facultyRepo.findById(faculty2.getUserId()).get();
		
		assertNotNull(faculty1);
		assertNotNull(faculty2);
		
		
		assertEquals(faculty1, student1.getSupervisor());
		assertEquals(faculty2, student2.getSupervisor());		
	}

}
