package com.example.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
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
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.app.ws.io.entity.AddressEntity;
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
		userEntity.setFirstName("Pogul");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encodedPassword);
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("sdfsdfsdfsdfdsf");
		userEntity.setAddresses(getAddressEntities());

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
		assertThrows(UsernameNotFoundException.class, () -> {
			userService.getUser("test@test.com");
		});
	}

	@Test
	final void testCreateUser() {
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn("asdsdfsdfsdfdsf");
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encodedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressDtos());
		userDto.setFirstName("Pooja");
		userDto.setLastName("Pogul");
		userDto.setPassword("123");
		userDto.setEmail("test@test.com");
		
		UserDto storedUserDetails = userService.createUser(userDto);
		
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
		assertNotNull(storedUserDetails.getUserId());
		assertEquals(userEntity.getAddresses().size(), storedUserDetails.getAddresses().size());
		verify(utils, times(2)).generateAddressId(30);
		verify(bCryptPasswordEncoder, times(1)).encode("123");
	}

	private List<AddressDto> getAddressDtos() {
		AddressDto addressDto = new AddressDto();
		addressDto.setType("shipping");
		addressDto.setCity("abc");
		addressDto.setCountry("India");
		addressDto.setPostalCode("11111");
		addressDto.setStreetName("123 Street");

		AddressDto billingAddressDto = new AddressDto();
		billingAddressDto.setType("billing");
		billingAddressDto.setCity("abc");
		billingAddressDto.setCountry("India");
		billingAddressDto.setPostalCode("11111");
		billingAddressDto.setStreetName("123 Street");

		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(addressDto);
		addresses.add(billingAddressDto);

		return addresses;
	}
	
	private List<AddressEntity> getAddressEntities() {
		List<AddressDto> addresses = getAddressDtos();
		
		ModelMapper mapper = new ModelMapper();
		
		Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
		
		return new ModelMapper().map(addresses, listType);
				
		
	}

}
