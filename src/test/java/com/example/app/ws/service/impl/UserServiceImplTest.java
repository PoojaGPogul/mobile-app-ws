package com.example.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.app.ws.io.entity.UserEntity;
import com.example.app.ws.io.repositories.UserRepository;
import com.example.app.ws.shared.Utils;
import com.example.app.ws.shared.dto.AddressDto;
import com.example.app.ws.shared.dto.UserDto;


class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;
	
	@Mock
	Utils utils;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	String userId = "qwerty";
	String encodedPassword = "kdfnwkerjskdfn";
	UserEntity userEntity;
 
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Pooja");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encodedPassword);
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("sdfsdfsdfsdfdsf");
		
	}

	@Test
	void testGetUser() {

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
	
	@Test
	final void testCreateUser() {
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn("asdsdfsdfsdfdsf");
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encodedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		
		AddressDto addressDto = new AddressDto();
		addressDto.setType("shipping");
		
		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(addressDto);
		
		UserDto userDto = new UserDto();
		userDto.setAddresses(addresses);
		
		UserDto storedUserDetails = userService.createUser(userDto);
		
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		
		
		
	}

}
