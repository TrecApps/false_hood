package com.trecapps.false_hood.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

public class AuthenticationControllerBase {
	
	FalsehoodUserService userService;
	
	@SuppressWarnings("unused")
	private AuthenticationControllerBase()
	{
		
	}
	
	public AuthenticationControllerBase(FalsehoodUserService userService)
	{
		this.userService = userService;
	}
	
	

	FalsehoodUser getUser(@SuppressWarnings("rawtypes") RequestEntity entry)
	{
		HttpHeaders headers = entry.getHeaders();
		
		String token = headers.getFirst("Authorization");
		
		return userService.getUserFromToken(token);
	}
	
	ResponseEntity<String> validateUser(FalsehoodUser user, int minCredit)
	{
		if(user == null)
		{
			return new ResponseEntity<String>("Could Not Authenticate User", HttpStatus.UNAUTHORIZED);
		}
		
		if( user.getCredit() < minCredit)
		{
			return new ResponseEntity<String>("Could Not Authenticate User", HttpStatus.FORBIDDEN);
		}
		
		
		return null;
	}
}
