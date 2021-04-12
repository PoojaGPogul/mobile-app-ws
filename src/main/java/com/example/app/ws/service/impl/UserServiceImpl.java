package com.example.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app.ws.exceptions.UserServiceException;
import com.example.app.ws.io.entity.UserEntity;
import com.example.app.ws.io.repositories.UserRepository;
import com.example.app.ws.service.UserService;
import com.example.app.ws.shared.AmazonSES;
import com.example.app.ws.shared.Utils;
import com.example.app.ws.shared.dto.AddressDto;
import com.example.app.ws.shared.dto.UserDto;
import com.example.app.ws.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto userDto) {

		if (userRepository.findByEmail(userDto.getEmail()) != null) {
			throw new RuntimeException("Record already exists!");
		}
		
		for(int i=0;i<userDto.getAddresses().size();i++) {
			AddressDto addressDto = userDto.getAddresses().get(i);
			addressDto.setUserDetails(userDto);
			addressDto.setAddressId(utils.generateAddressId(30));
			userDto.getAddresses().set(i, addressDto);
		}

		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
		userEntity.setEmailVerificationStatus(false);
		
		UserEntity storedUserDetails = userRepository.save(userEntity);
		UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);
		
		//Send email message to user to verify email address 
		new AmazonSES().verifyEmail(returnValue);

		
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), 
				userEntity.getEmailVerificationStatus(), true, true, true, new ArrayList<>());
		
		//return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());

		
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public UserDto getUserByUserId(String id) {
		UserDto returnValue = new UserDto();

		UserEntity userEntity = userRepository.findByUserId(id);
		if (userEntity == null) {
			throw new UserServiceException("User with ID: "+id+" not found");
		}
		
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(userEntity, UserDto.class);
		
		return returnValue;
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		UserDto returnValue = new UserDto();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		
		UserEntity updatedUserDetails = userRepository.save(userEntity);
		
		BeanUtils.copyProperties(updatedUserDetails, returnValue);
		
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValues = new ArrayList<UserDto>();
		
		Pageable pageable = PageRequest.of(page, limit); 
		Page<UserEntity> pageOfEntities = userRepository.findAll(pageable);
		List<UserEntity> userEntities = pageOfEntities.getContent();
		
		for(UserEntity userEntity: userEntities) {
			UserDto userModel = new UserDto();
			BeanUtils.copyProperties(userEntity, userModel);
			returnValues.add(userModel);
		}
		
		return returnValues;
	}

	@Override
	public boolean verifyEmailToken(String token) {
		
		boolean returnValue = false;
		
		UserEntity userEntity = userRepository.findByEmailVerificationToken(token);
		
		if(userEntity != null) {
			boolean hasTokenExpired = Utils.hasTokenExpired(token);
			if(!hasTokenExpired) {
				userEntity.setEmailVerificationToken(null);
				userEntity.setEmailVerificationStatus(Boolean.TRUE);
				userRepository.save(userEntity);
				returnValue = true;
			}
		}
		return returnValue;
	}

	@Override
	public boolean requestPasswordReset(String email) {
		// TODO Auto-generated method stub
		return false;
	}

}
