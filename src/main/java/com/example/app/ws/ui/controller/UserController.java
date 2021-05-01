package com.example.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.ws.exceptions.UserServiceException;
import com.example.app.ws.service.AddressService;
import com.example.app.ws.service.UserService;
import com.example.app.ws.shared.dto.AddressDto;
import com.example.app.ws.shared.dto.UserDto;
import com.example.app.ws.ui.model.request.PasswordResetModel;
import com.example.app.ws.ui.model.request.PasswordResetRequestModel;
import com.example.app.ws.ui.model.request.UserDetailsRequestModel;
import com.example.app.ws.ui.model.response.AddressRest;
import com.example.app.ws.ui.model.response.ErrorMessages;
import com.example.app.ws.ui.model.response.OperationStatusModel;
import com.example.app.ws.ui.model.response.RequestOperationName;
import com.example.app.ws.ui.model.response.RequestOperationStatus;
import com.example.app.ws.ui.model.response.UserRest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
//@CrossOrigin(origins = {"http://localhost:8083", "http://localhost:8084"})
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressesService;
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@ApiOperation(value="Get User Details Web Service Endpoint",
	notes="${userController.GetUser.ApiOperation.Notes}")
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();

		UserDto userDto = userService.getUserByUserId(id);

		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(userDto, UserRest.class);

		return returnValue;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

		if (userDetails.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}

		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		UserRest returnValue = modelMapper.map(createdUser, UserRest.class);

		return returnValue;
	}

	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {

		UserRest returnValue = new UserRest();

		if (userDetails.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}

		UserDto userDto = new UserDto();

		ModelMapper modelMapper = new ModelMapper();
		userDto = modelMapper.map(userDetails, UserDto.class);
		
		UserDto updatedUser = userService.updateUser(id, userDto);
		returnValue = modelMapper.map(updatedUser, UserRest.class);
		
		return returnValue;

	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
	//@PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
	//@Secured("ROLE_ADMIN")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(id);

		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

		return returnValue;
	}

	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<UserRest> returnValue = new ArrayList<UserRest>();

		List<UserDto> users = userService.getUsers(page, limit);
		ModelMapper modelMapper = new ModelMapper();
		
		for (UserDto userDto : users) {
			UserRest userModel = modelMapper.map(userDto, UserRest.class);
			returnValue.add(userModel);
		}

		return returnValue;
	}

	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	// localhost:8080/mobile-app-ws/users/{id}/addresses
	@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public CollectionModel<AddressRest> getUserAddresses(@PathVariable String id) {
		List<AddressRest> returnValue = new ArrayList<>();

		List<AddressDto> addressDtos = addressesService.getAddresses(id);

		if (addressDtos != null && !addressDtos.isEmpty()) {
			Type listType = new TypeToken<List<AddressRest>>() {
			}.getType();
			ModelMapper modelMapper = new ModelMapper();
			returnValue = modelMapper.map(addressDtos, listType);

			for (AddressRest addressRest : returnValue) {
				Link selfLinkToAddress = WebMvcLinkBuilder.linkTo(
						WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId()))
						.withSelfRel();
				addressRest.add(selfLinkToAddress);
			}

		}

		// http://localhost:8080/users/<userId>
		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(id).withRel("user");

		// http://localhost:8080/users/<userId>/addresses
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(id))
				.withSelfRel();

		return CollectionModel.of(returnValue, userLink, selfLink);
	}

	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	// localhost:8080/mobile-app-ws/users/{id}/addresses/{addressId}
	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public EntityModel<AddressRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
		AddressRest returnValue = new AddressRest();

		AddressDto addressDto = addressesService.getAddress(addressId);

		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(addressDto, AddressRest.class);

		// http://localhost:8080/users/<userId>
		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");

		// http://localhost:8080/users/<userId>/addresses
		Link userAddressesLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");

		// http://localhost:8080/users/<userId>/addresses/<addressId>
		Link selfLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(userId, addressId))
				.withSelfRel();

		return EntityModel.of(returnValue, Arrays.asList(userLink, userAddressesLink, selfLink));

	}

	// localhost:8080/mobile-app-ws/users/email-verification?token=2321323423
	@GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

		boolean isVerified = userService.verifyEmailToken(token);

		if (isVerified)
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		else
			returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

		return returnValue;
	}

	// localhost:8080/mobile-app-ws/users/password-reset-request
	@PostMapping(path = "/password-reset-request", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordRequestResetModel) {
		OperationStatusModel returnValue = new OperationStatusModel();

		boolean operationResult = userService.requestPasswordRequest(passwordRequestResetModel.getEmail());

		returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
		returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

		if (operationResult) {
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}

		return returnValue;
	}

	// localhost:8080/mobile-app-ws/users/password-reset
	@PostMapping(path = "/password-reset", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
		OperationStatusModel returnValue = new OperationStatusModel();
		
		boolean operationResult = userService.resetPassword(
				passwordResetModel.getToken(),
				passwordResetModel.getPassword());
		
		returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
		returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		
		if(operationResult) {
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}
		
		return returnValue;
		
	}

}
