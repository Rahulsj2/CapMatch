package com.stackO.capmatch;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.stackO.capmatch.Models.SDG;
import com.stackO.capmatch.Models.User;
import com.stackO.capmatch.Repositories.SDGRepository;
import com.stackO.capmatch.Repositories.UserRepository;


@RunWith(SpringRunner.class)
@DataJpaTest(showSql= true)
class SDGTests {
	
	@Autowired
	SDGRepository sdgRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Before
	public void init() {
		// Nothing here yet. Autowired beans used
	}

	@Test
	void saveSDGTest() {
		String firstname1 = "Owusu-Banahene";
		String lastname1 = "Osei";
		String email1 = "bansone@ashesi.edu.gh";
		String password1 = "password98";		
		User user1 = new User(firstname1, lastname1, email1, password1);
		
		String firstname2 = "Ama";
		String lastname2 = "Osei";
		String email2 = "animah@gtuc.edu.gh";
		String password2 = "foreverYoung";		
		User user2 = new User(firstname2, lastname2, email2, password2);
		
		String sdgName = "Freedom from poverty";
		String sdgDescription = "The goal is to make sure no human ever is imprisoned";
		SDG sdg1 = new SDG(1, sdgName, sdgDescription);
		
		sdg1.addInterestedUser(user1);
		sdg1.addInterestedUser(user2);
		sdg1.addInterestedUser(user1);				// test duplicate avoidance
		sdg1.addInterestedUser(null);				// test null entry

		sdgRepo.save(sdg1);
		
		System.err.println("SDG id: " + sdg1.getId());
		System.err.println("User1 id: " + user1.getUserId());
		System.err.println("User2 id: " + user2.getUserId());
		
		sdg1 = sdgRepo.findById(sdg1.getId()).get();
		
		assertNotNull(sdg1);
		assertEquals(sdgName, sdg1.getName());
		assertEquals(sdgDescription, sdg1.getDescription());
		
		
		assertEquals(2, sdg1.getInterestedUsers().size());
		assertTrue(sdg1.getInterestedUsers().contains(user1));
		assertTrue(sdg1.getInterestedUsers().contains(user2));




	}
	
	@Test
	void saveUserTest() {
		String firstname = "Owusu-Banahene";
		String lastname = "Osei";
		String email = "bansone@ashesi.edu.gh";
		String password = "password98";		
		User user = new User(firstname, lastname, email, password);
		
		SDG sdg1 = new SDG(1, "Freedom from poverty", "The goal is to make sure no human ever is imprisoned");
		SDG sdg2 = new SDG(2, "Universal Healthcare", "Everyone should go to the hospital for free");
		
		
		user.addSDG(sdg1);
		user.addSDG(sdg2);
		user.addSDG(sdg1); 			// test for duplicate entries
		user.addSDG(null);			// test null entry
		
		
		userRepo.save(user);
		
		user = userRepo.findById(user.getUserId()).get();
		assertNotNull(user);
		
		
		assertEquals(2, user.getSDGs().size());
		assertTrue(user.getSDGs().contains(sdg1));
		assertTrue(user.getSDGs().contains(sdg2));
	}

}
