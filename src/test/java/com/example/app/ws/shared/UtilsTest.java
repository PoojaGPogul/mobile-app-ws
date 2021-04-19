package com.example.app.ws.shared;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {
	
	@Autowired
	Utils utils;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGenerateUserId() {
		String userID1 = utils.generateUserId(30);
		String userID2 = utils.generateUserId(30);
		
		assertNotNull(userID1);
		assertTrue(userID1.length() == 30);
		
		assertNotNull(userID2);
		assertTrue(userID2.length() == 30);
		
		assertTrue(!userID1.equalsIgnoreCase(userID2));
	}

	@Test
	@Disabled
	void testHasTokenExpired() {
		fail("Not yet implemented");
	}

}
