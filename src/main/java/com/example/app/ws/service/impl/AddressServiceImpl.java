package com.example.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.ws.io.entity.AddressEntity;
import com.example.app.ws.io.entity.UserEntity;
import com.example.app.ws.io.repositories.AddressRepository;
import com.example.app.ws.io.repositories.UserRepository;
import com.example.app.ws.service.AddressService;
import com.example.app.ws.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;

	
	@Override
	public List<AddressDto> getAddresses(String userId) {
		
		List<AddressDto> returnValue = new ArrayList<>();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null)
			return returnValue;
		
		ModelMapper modelMapper = new ModelMapper();
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		for(AddressEntity addressEntity: addresses) {
			returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
		}
		
		return returnValue;
	}


	@Override
	public AddressDto getAddress(String addressId) {
		AddressDto addressDto = new AddressDto();
		
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if(addressEntity != null) {
			ModelMapper modelMapper = new ModelMapper();
			addressDto = modelMapper.map(addressEntity, AddressDto.class);
		}
		
		return addressDto;
	}

}
