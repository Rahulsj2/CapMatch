package com.stacko.capmatch;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.stacko.capmatch.Models.Interest;
import com.stacko.capmatch.Models.InterestCategory;
import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Repositories.InterestCategoryRepository;
import com.stacko.capmatch.Repositories.InterestRepository;

@RunWith(SpringRunner.class)
//@SpringBootTest
@DataJpaTest(showSql= true)
class InterestAndInterestCategoryTests {
	
	@Autowired
	private InterestCategoryRepository categoryRepo;
	
	@Autowired
	private InterestRepository interestRepo;
	
	@Before
	public void init() {
		// Nothing here yet. Autowired beans used
	}

	@Test
	void categoryTest() {
		String name = "Technology";
		String description = "Everything from Robotics to Artificial Intelligence";
		InterestCategory category = new InterestCategory(name, description);
		
		// Create and add specific interests
		String name1 = "NLP";
		String description1 = "This is about languages";
		String name2 = "Big Data";
		String description2 = "Let's look at DATA";
		
		Interest interest1 = new Interest(name1, description1);
		Interest interest2 = new Interest(name2, description2);
		

		category.addInterest(interest1);
		category.addInterest(interest2);
		category.addInterest(interest1);			// Line added to test duplicate handling
		
		System.err.println("Category Id pre-save: " + category.getCategoryId());
		System.err.println("Interest1 Id pre-save: " + interest1.getInterestId());
		System.err.println("Interest2 Id pre-save: " + interest2.getInterestId());
		categoryRepo.save(category);
		System.err.println("Category Id post-save: " + category.getCategoryId());
		System.err.println("Interest1 Id post-save: " + interest1.getInterestId());
		System.err.println("Interest2 Id post-save: " + interest2.getInterestId());
		
		category = categoryRepo.findById(category.getCategoryId()).get();
		assertNotNull(category);
		assertEquals(name, category.getName());
		assertEquals(description, category.getDescription());
		assertEquals(2, category.getInterests().size());
		assertTrue(category.getInterests().contains(interest1));
		assertTrue(category.getInterests().contains(interest1));
		
		interest1 = interestRepo.findById(interest1.getInterestId()).get();
		assertNotNull(interest1);
	}
	
	
	@Test
	void interestTest() {
		String name = "NLP";
		String description = "This is about languages";		
		Interest interest = new Interest(name, description);
		
		String name1 = "Technology";
		String description1 = "Everything from Robotics to Artificial Intelligence";
		InterestCategory category = new InterestCategory(name1, description1);
		
		interest.bidirectionalSetCategory(category);
		
		System.err.println("Category Id pre-save: " + category.getCategoryId());
		System.err.println("Interest Id pre-save: " + interest.getInterestId());
		interestRepo.save(interest);
		System.err.println("Category Id post-save: " + category.getCategoryId());
		System.err.println("Interest Id post-save: " + interest.getInterestId());
		
		interest= interestRepo.findById(interest.getInterestId()).get();
		assertNotNull(interest);
		assertEquals(name, interest.getName());
		assertEquals(description, interest.getDescription());
		assertNotNull(interest.getCategory());
		assertTrue(interest.getCategory().equals(category));
		
		category = categoryRepo.findById(category.getCategoryId()).get();
		assertNotNull(category);
		assertEquals(name1, category.getName());
		assertEquals(description1, category.getDescription());
		assertEquals(1, category.getInterests().size());
		assertTrue(category.getInterests().contains(interest));

	}
	
	
	@Test
	void consistencyTests() {
		System.out.println("\n\n\n\n Last-ish Test Coming right up \n\n");
		String name = "Technology";
		String description = "Everything from Robotics to Artificial Intelligence";
		InterestCategory category = new InterestCategory(name, description);
		
		// Create and add specific interests
		String name1 = "NLP";
		String description1 = "This is about languages";
		String name2 = "Big Data";
		String description2 = "Let's look at DATA";
		
		Interest interest1 = new Interest(name1, description1);
		Interest interest2 = new Interest(name2, description2);
		

		category.addInterest(interest1);
		category.addInterest(interest2);
		category.addInterest(interest1);			// test duplication
		category.addInterest(null);  				// test null argument
		
		System.err.println("Before: " + category.getCategoryId());
		categoryRepo.save(category);
		System.err.println("After: " + category.getCategoryId());
		
		category = categoryRepo.findById(category.getCategoryId()).get();
		System.err.println(interest1.getInterestId());
		System.err.println(interest2.getInterestId());

		
		
		assertNotNull(category);
		assertEquals(category.getName(), name);
		assertEquals(category.getDescription(), description);
		assertNotNull(category.getInterests());
		assertEquals(category.getInterests().size(), 2);
		
		assertTrue(category.getInterests().contains(interest1));
		assertTrue(category.getInterests().contains(interest2));
	}
	
	
	@Test
	void consistencyTests2() {
		System.out.println("\n\n\n\n Another Last Test Coming right up \n\n");
		String name = "Technology";
		String description = "Everything from Robotics to Artificial Intelligence";
		InterestCategory category = new InterestCategory(name, description);
		
		System.err.println("Initial category Id: " + category.getCategoryId());
				
		// Create and add specific interests
		String name1 = "NLP";
		String description1 = "This is about languages";
		String name2 = "Big Data";
		String description2 = "Let's look at DATA";
		
		Interest interest1 = new Interest(name1, description1);
		Interest interest2 = new Interest(name2, description2);
		
		interest1.bidirectionalSetCategory(category);
		interest2.bidirectionalSetCategory(category);
		
		interestRepo.save(interest1);
		System.err.println("Category Id afte Save 1: " + category.getCategoryId());
		interestRepo.save(interest2);
		System.err.println("Category Id afte Save 2: " + category.getCategoryId());

		
		
		
		category = categoryRepo.findById(category.getCategoryId()).get();
		assertNotNull(category);
		
		assertEquals(category.getName(), name);
		assertEquals(category.getDescription(), description);
		assertNotNull(category.getInterests());
		assertEquals(category.getInterests().size(), 2);
		
		assertTrue(category.getInterests().contains(interest1));
		assertTrue(category.getInterests().contains(interest2));
	}
	
	
	@Test
	void testAddingInterestedUsers() {
		String name = "NLP";
		String description = "This is about languages";		
		Interest interest = new Interest(name, description);
		
		String name1 = "Technology";
		String description1 = "Everything from Robotics to Artificial Intelligence";
		InterestCategory category = new InterestCategory(name1, description1);
		
		interest.bidirectionalSetCategory(category);
		
		// Create users to add
		String firstname1 = "Owusu-Banahene";
		String lastname1 = "Osei";
		String email1 = "bana@gmail.com";
		String password1 = "password98";		
		User user1 = new User(firstname1, lastname1, email1, password1);
		
		String firstname2 = "Emmanuel";
		String lastname2 = "Johnson";
		String email2 = "emmaj@gmail.com";
		String password2 = "password98";		
		User user2 = new User(firstname2, lastname2, email2, password2);
		
		
		interest.addInterestedUser(user1);
		interest.addInterestedUser(user2);
		interest.addInterestedUser(null);			// test attempted null insertion
		interest.addInterestedUser(user1);			// test duplicate entry
		
		
		interestRepo.save(interest);
		
		System.err.println("User1 id: " + user1.getUserId());
		System.err.println("User2 id: " + user2.getUserId());
		System.err.println("CategoryId: " + interest.getCategory().getCategoryId());
		
		interest = interestRepo.findById(interest.getInterestId()).get();
		assertNotNull(interest);
		
		assertEquals(interest.getInterestedUsers().size(), 2);
		assertTrue(interest.getInterestedUsers().contains(user1));
		assertTrue(interest.getInterestedUsers().contains(user1));		
	}

}
