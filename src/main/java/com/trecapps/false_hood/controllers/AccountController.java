package com.trecapps.false_hood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.trecapps.false_hood.account.ReturnObj;
import com.trecapps.false_hood.account.LogIn;
import com.trecapps.false_hood.account.NewUser;

@RestController("/account")
public class AccountController 
{
	@Autowired
	RestTemplate userServiceCaller;
	
	@Value("${trec.apps.client.id}")
	String clientId;
	
	@Value("${trec.apps.user.service.url}")
	String userServiceUrl;
	
	@PostMapping("/CreateUser")
	ResponseEntity<ReturnObj> createUser(RequestEntity<NewUser> entity)
	{
		NewUser nu = entity.getBody();
		
		nu.setClientId(clientId);
		
		return userServiceCaller.postForEntity(userServiceUrl + "/CreateUser", nu, ReturnObj.class);
	}
	
	@PostMapping("/LogIn")
	ResponseEntity<ReturnObj> logIn(RequestEntity<LogIn> entity)
	{
		LogIn login = entity.getBody();
		
		if(login == null)
			return new ResponseEntity<ReturnObj>(HttpStatus.BAD_REQUEST);
		
		login.setClientId(clientId);
		
		return userServiceCaller.postForEntity(userServiceUrl + "/LogIn", login, ReturnObj.class);
	}
}
