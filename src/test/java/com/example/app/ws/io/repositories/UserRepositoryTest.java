package com.example.app.ws.io.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.app.ws.io.entity.AddressEntity;
import com.example.app.ws.io.entity.UserEntity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	static boolean recordsCreated = false;
	
	@BeforeEach
	void setUp() throws Exception {
		if(!recordsCreated) {
			createRecords();
			recordsCreated = true;
		}
	}

	@Test
	final void testGetVerifiedUsers() {
		
		Pageable pageableRequest = PageRequest.of(0, 1);
		Page<UserEntity> pages =  userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
		assertNotNull(pages);
		
		List<UserEntity> userEntities = pages.getContent();
		assertNotNull(userEntities);
		assertTrue(userEntities.size() == 1);
		
	}
	
	@Test
	final void testFindUserByFirstName() {
		String firstName = "Pooja";
		List<UserEntity> usersEntities = userRepository.findUserByFirstName(firstName);
		
		assertNotNull(usersEntities);
		assertTrue(usersEntities.size() == 1);
		assertEquals(firstName, usersEntities.get(0).getFirstName());
		
	}
	
	@Test
	final void testFindUserByLastName() {
		String lastName = "Pogul";
		List<UserEntity> usersEntities = userRepository.findUserByLastName(lastName);
		
		assertNotNull(usersEntities);
		assertTrue(usersEntities.size() == 1);
		assertEquals(lastName, usersEntities.get(0).getLastName());
		
	}
	
	@Test
	final void testFindUserByKeyword() {
		String keyword = "Poo";
		List<UserEntity> usersEntities = userRepository.findUserByKeyword(keyword);
		
		assertNotNull(usersEntities);
		assertTrue(usersEntities.size() == 2);
		assertTrue(usersEntities.get(0).getFirstName().contains(keyword) ||
				usersEntities.get(0).getLastName().contains(keyword));
		
	}
	
	@Test
	final void testFindUserFirstnameAndLastnameByKeyword() {
		String keyword = "Poo";
		List<Object[]> usersEntities = userRepository.findUserFirstnameAndLastnameByKeyword(keyword);
		
		assertNotNull(usersEntities);
		assertTrue(usersEntities.size() == 2);
		
		Object[] user = usersEntities.get(0);
		
		assertTrue(user.length == 2);
		
		String userFirstName = String.valueOf(user[0]);
		String userLastName = String.valueOf(user[1]);
		
		assertNotNull(userFirstName);
		assertNotNull(userLastName);
		
		System.out.println(userFirstName+" "+userLastName); 
	}
	
	@Test
	final void testUpdateUserEmailVerificationStatus() {
		boolean newEmailVerificationStatus = false;
		
		userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "asdasdww");
		
		UserEntity storedDetails = userRepository.findByUserId("asdasdww");
		assertNotNull(storedDetails);
		
		boolean storedEmailVerificationStatus = storedDetails.getEmailVerificationStatus();
		assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
		
	}
	
	@Test
	final void testFindUserEntityByUserId() {
		String userId = "asdasdww";
		UserEntity storedUserEntity = userRepository.findUserEntityByUserId(userId);
		
		assertNotNull(storedUserEntity);
		assertTrue(storedUserEntity.getUserId().equals(userId));
	}
	@Test
	final void testFindUserEntityFullNameByUserId() {
		String userId = "asdasdww";
		List<Object[]> records = userRepository.findUserEntityFullNameByUserId(userId);
		
		assertNotNull(records);
		assertTrue(records.size() == 1);
		
		Object[] userDetails = records.get(0);
		assertNotNull(userDetails);
		
		String firstName = String.valueOf(userDetails[0]);
		String lastName = String.valueOf(userDetails[1]);
		
		assertNotNull(firstName);
		assertNotNull(lastName);
	}
	@Test
	final void testUpdateUserEntityEmailVerificationStatus() {
		boolean newEmailVerificationStatus = false;
		
		userRepository.updateUserEntityEmailVerificationStatus(newEmailVerificationStatus, "asdasdww");
		
		UserEntity storedDetails = userRepository.findByUserId("asdasdww");
		assertNotNull(storedDetails);
		
		boolean storedEmailVerificationStatus = storedDetails.getEmailVerificationStatus();
		assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
		
	}
	
	 
	private void createRecords() {
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName("Pooja");
		userEntity.setLastName("Pogul");
		userEntity.setUserId("asdasdww");
		userEntity.setEncryptedPassword("aaa");
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationStatus(true);
		
		AddressEntity addressEntity = new AddressEntity();
		addressEntity.setType("shipping");
		addressEntity.setAddressId("aadsa23d");
		addressEntity.setCity("Solapur");
		addressEntity.setCountry("India");
		addressEntity.setPostalCode("123123");
		addressEntity.setStreetName("Street A");
		
		List<AddressEntity> listAddresses = new ArrayList<AddressEntity>();
		listAddresses.add(addressEntity);
		
		userEntity.setAddresses(listAddresses);
		
		userRepository.save(userEntity);
		
		UserEntity userEntity2 = new UserEntity();
		userEntity2.setFirstName("Pooja1");
		userEntity2.setLastName("Pogul1");
		userEntity2.setUserId("asdasdsdsdwwf");
		userEntity2.setEncryptedPassword("aaaas");
		userEntity2.setEmail("test_test@test.com");
		userEntity2.setEmailVerificationStatus(true);
		
		AddressEntity addressEntity2 = new AddressEntity();
		addressEntity2.setType("shipping");
		addressEntity2.setAddressId("aadsas2dsdd");
		addressEntity2.setCity("Solapur");
		addressEntity2.setCountry("India");
		addressEntity2.setPostalCode("123123");
		addressEntity2.setStreetName("Street A");
		
		List<AddressEntity> listAddresses2 = new ArrayList<AddressEntity>();
		listAddresses2.add(addressEntity2);
		
		userEntity2.setAddresses(listAddresses2);
		
		userRepository.save(userEntity2);
	}

}
