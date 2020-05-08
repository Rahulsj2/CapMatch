package com.stackO.capmatch;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql= true)
class TestBase {
	
	@Before
	public void init() {
		// Nothing here yet. Autowired beans used
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
