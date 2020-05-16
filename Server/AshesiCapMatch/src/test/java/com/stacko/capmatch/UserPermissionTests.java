package com.stacko.capmatch;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.stacko.capmatch.Models.User;
import com.stacko.capmatch.Repositories.UserRepository;
import com.stacko.capmatch.Security.UserPermission;
import com.stacko.capmatch.Security.UserPermissionRepository;
import com.stacko.capmatch.Security.Signup.AccountConfirmation;
import com.stacko.capmatch.Security.Signup.AccountConfirmationRepository;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql= true)
class UserPermissionTests {
	
	@Autowired
	private UserPermissionRepository permissionRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired AccountConfirmationRepository confirmationRepo;
	
	@Before
	public void init() {
		// Nothing here yet. Autowired beans used
	}

	@Test
	void permissiontest() {
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
		
		
		
		String name = "student";		
		UserPermission permission = new UserPermission(name);
		
		permission.addUser(user1);
		permission.addUser(user2);
		
		System.err.println("User1: " + user1.getUserId() + "\t User2: " + user2.getUserId());
				
		
		System.err.println("Init id: " + permission.getPermissionId());		
		permissionRepo.save(permission);
		System.err.println("Post-save id: " + permission.getPermissionId());
		
		System.err.println("User1: " + user1.getUserId() + "\t User2: " + user2.getUserId());

				
		permission = permissionRepo.findById(permission.getPermissionId()).get();
		
		assertNotNull(permission);		
		assertEquals(name.toUpperCase(), permission.getName());				// Permission name is always uppercase
		assertNotNull(permission.getUsers());
		assertEquals(2, permission.getUsers().size());
		
		
		Iterator<User> iter = permission.getUsers().iterator();
		
		while(iter.hasNext()) {
			User temp = iter.next();
			System.out.println("This permissions has user1 [hashcode]: " + ((Integer) user1.hashCode()).equals(temp.hashCode()));
			System.out.println("This permissions has user1 [equals]: " + user1.equals(temp) + "\n");
			System.out.println("This permissions has user1 [equals]: " + user2.equals(temp) + "\n");
		}
		
		
		assertTrue(permission.getUsers().contains(user1));
		assertTrue(permission.getUsers().contains(user2));
	}
	
	@Test
	void saveUser() {
		
		String firstname1 = "Owusu-Banahene";
		String lastname1 = "Osei";
		String email1 = "bana@gmail.com";
		String password1 = "password98";		
		User user1 = new User(firstname1, lastname1, email1, password1);
		
		String pname1 = "student";		
		UserPermission permission1 = new UserPermission(pname1);
		String pname2 = "admin";		
		UserPermission permission2 = new UserPermission(pname2);
		
		user1.grantPermission(permission1);
		user1.grantPermission(permission2);		
		user1.grantPermission(permission1);				// test duplicate avoidance
		
		userRepo.save(user1);		
		
		user1 = userRepo.findById(user1.getUserId()).get();
		
		assertNotNull(user1);
		System.err.println("User granted permissions: " + user1.getPermissions().size());
		assertEquals(2, user1.getPermissions().size());
		assertTrue(user1.getPermissions().contains(permission1));
		assertTrue(user1.getPermissions().contains(permission2));
		
	}
	
	
	@Test
	void accountConfirmationTest() {		
		String firstname1 = "Owusu-Banahene";
		String lastname1 = "Osei";
		String email1 = "bana@gmail.com";
		String password1 = "password98";		
		User user1 = new User(firstname1, lastname1, email1, password1);
		
		String confirmCode = "HELLOWorkje8auf";
		
		AccountConfirmation confirm = new AccountConfirmation(confirmCode, user1);
		
		confirmationRepo.save(confirm);
		
		confirm = confirmationRepo.findById(confirm.getConfirmationId()).get();
		
		assertNotNull(confirm);
		System.out.println("ConfirmationID: " + confirm.getConfirmationId());
		
		assertNotNull(confirm.getDateCreated());
		assertNotNull(confirm.getUser());
		assertEquals(confirm.getUser(), user1);
		assertEquals(confirm.getConfirmCode(), confirmCode);
		
	}

}
