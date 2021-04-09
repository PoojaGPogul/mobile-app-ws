package com.example.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.app.ws.shared.dto.UserDto;


public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto userDto);
	UserDto getUser(String email);
	UserDto getUserByUserId(String id);
	UserDto updateUser(String userId, UserDto userDto);
	void deleteUser(String userId);
	List<UserDto> getUsers(int page, int limit);
	boolean verifyEmailToken(String token);
}
