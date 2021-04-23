package com.example.app.ws.shared;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
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
	final void testGenerateUserId() {
		String userID1 = utils.generateUserId(30);
		String userID2 = utils.generateUserId(30);
		
		assertNotNull(userID1);
		assertTrue(userID1.length() == 30);
		
		assertNotNull(userID2);
		assertTrue(userID2.length() == 30);
		
		assertTrue(!userID1.equalsIgnoreCase(userID2));
	}

	@Test
	final void testHasTokenNotExpired() {
		String token = utils.generateEmailVerificationToken("asdasfaf");
		assertNotNull(token);
		
		boolean hasTokenExpired = Utils.hasTokenExpired(token);
		assertFalse(hasTokenExpired);
	}
	
	@Test
	final void testHasTokenExpired() {
		String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJUemdNb3BLczhJaHJyU1J4dm9GcXZkaGdrZTBLUUkiLCJleHAiOjE2MTg0MDQyNTR9.HDHvwmy9ZNsd1Z3DS2I_MBq0RDRSRuZjFdm2nZRxMudxoA3qkCbd8gvcDeo9D2nWwZsB2Tawz39HtKewGGYkPA";
		
		boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);
		assertTrue(hasTokenExpired);
		
	}
	

}
