package com.example.app.ws.ui.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.ws.ui.model.request.LoginRequestModel;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class AuthenticationController {

	@ApiOperation("User Login")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Response Headers", 
					responseHeaders = {
							@ResponseHeader(name = "authorization", 
									description = "<Bearer JWT Token here>", 
									response = String.class),
							@ResponseHeader(name = "userID", 
							description = "Public User Id value here>", 
							response = String.class) 
			}) 
	})
	@PostMapping("/users/login")
	public void theFakeLogin(@RequestBody LoginRequestModel loginRequestModel) {
		throw new IllegalStateException(
				"This method should not be called. This method is implemented by Spring Security.");
	}
}
