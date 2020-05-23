package com.stacko.capmatch;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.stacko.capmatch.Models.Interest;
import com.stacko.capmatch.Models.InterestCategory;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Models.User.AccountStatus;
import com.stacko.capmatch.Repositories.UserRepository;
import com.stacko.capmatch.Security.UserPermission;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql= true)
class UserTests {
	
	@Autowired
	UserRepository userRepo;
	
//	@Autowired
//	UserPermissionRepository permissionRepo;
	
	@Before
	void init() {
		// Nothing here
	}

	@Test
	void testPermissions() {
		// Create permissions
		String pname1 = "student";		
		UserPermission permission1 = new UserPermission(pname1);		
		String pname2 = "admin";		
		UserPermission permission2 = new UserPermission(pname2);
		
		
		
		String firstname = "Owusu-Banahene";
		String lastname = "Osei";
		String email = "bana@gmail.com";
		String password = "password98";
		String bio = "Banahene is a beautiful young soul and He has learned to build a career out of being simply amazing";
		
		User user = new User(firstname, lastname, email, password);
		
		user.grantPermission(permission1);
		user.grantPermission(permission2);
		
		user.setBio(bio);
		
		userRepo.save(user);
		
		user = userRepo.findById(user.getUserId()).get();
		
		assertNotNull(user);
		assertEquals(firstname, user.getFirstname());
		assertEquals(lastname, user.getLastname());
		assertEquals(email, user.getEmail());
		assertEquals(password, user.getPassword());
		assertEquals(bio, user.getBio());
		
		assertNotNull(user.getPermissions());
		assertEquals(2, user.getPermissions().size());
				
		assertTrue(user.getPermissions().contains(permission1));
		assertTrue(user.getPermissions().contains(permission2));		
	}
	
	
	void checkUserStatus() {
		String firstname = "Owusu-Banahene";
		String lastname = "Osei";
		String email = "bansone@ashesi.edu.gh";
		String password = "password98";
		
		User user = new User(firstname, lastname, email, password);
		
		System.err.println("Acount Status pre-save: " + user.getAccountStatus());		
		userRepo.save(user);
		
		// Make sure default status is UNVERIFIED
		user = userRepo.findById(user.getUserId()).get();
		assertNotNull(user);		
		assertEquals(User.AccountStatus.UNVERIFIED, user.getAccountStatus());	
		
		// Make sure once status is changed subsequent loads reflect that
		user.setAccountStatus(AccountStatus.ACTIVE);		
		userRepo.save(user);		
		user = userRepo.findById(user.getUserId()).get();
		assertNotNull(user);
		assertEquals(User.AccountStatus.ACTIVE, user.getAccountStatus());
		assertNotEquals(User.AccountStatus.UNVERIFIED, user.getAccountStatus());		
	}
	
	
	void interestTests() {
		String firstname = "Owusu-Banahene";
		String lastname = "Osei";
		String email = "bansone@ashesi.edu.gh";
		String password = "password98";		
		User user = new User(firstname, lastname, email, password);
		
		// Create interest and set corresponding category
		String name1 = "NLP";
		String description1 = "This is about languages";		
		Interest interest1 = new Interest(name1, description1);
		
		String name2 = "NLP";
		String description2 = "This is about languages";		
		Interest interest2 = new Interest(name2, description2);
		
		String cname = "Technology";
		String cdescription = "Everything from Robotics to Artificial Intelligence";
		InterestCategory category = new InterestCategory(cname, cdescription);
		
		interest1.bidirectionalSetCategory(category);
		interest2.bidirectionalSetCategory(category);

		
		// Add interest to user
		user.addInterest(interest1);
		user.addInterest(interest2);
		user.addInterest(interest1); 			// add again to check for duplicates
		user.addInterest(null);
		
		userRepo.save(user);
		
		user = userRepo.findById(user.getUserId()).get();
		assertNotNull(user);
		
		assertEquals(2, user.getInterests().size());
		assertTrue(user.getInterests().contains(interest1));
		assertTrue(user.getInterests().contains(interest2));
		assertTrue(interest1.getInterestedUsers().size() == 1);
		
		user.removeAllInterests();
		user = userRepo.findById(user.getUserId()).get();
		assertEquals(0, user.getInterests().size());
		
		assertTrue(interest1.getInterestedUsers().size() == 0);
		
	}

}
