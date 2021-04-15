package com.example.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.app.ws.io.entity.UserEntity;
import com.example.app.ws.io.repositories.UserRepository;
import com.example.app.ws.shared.dto.UserDto;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetUser() {

		UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Pooja");
		userEntity.setUserId("qwerty");
		userEntity.setEncryptedPassword("kdfnwkerjskdfn");

		// Mocking - providing fake result
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		// Test getUser method
		UserDto userDto = userService.getUser("test@test.com");

		// Assertions
		assertNotNull(userDto);
		assertEquals("Pooja", userDto.getFirstName());
		assertEquals("qwerty", userDto.getUserId());

	}

	@Test
	final void testGetUser_UsernameNotFoundException() {
		// Mocking - providing fake result
		when(userRepository.findByEmail(anyString())).thenReturn(null);

		// Assertions
		assertThrows(UsernameNotFoundException.class, 
		
				() -> {
					userService.getUser("test@test.com");
				}
		);

	}

}
