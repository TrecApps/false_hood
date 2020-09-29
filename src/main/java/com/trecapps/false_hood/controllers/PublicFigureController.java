package com.trecapps.false_hood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trecapps.false_hood.model.FalsehoodUser;
import com.trecapps.false_hood.model.PublicFigureEntry;
import com.trecapps.false_hood.services.FalsehoodUserService;
import com.trecapps.false_hood.services.PublicFigureService;

@RestController
@RequestMapping("/PublicFigure")
public class PublicFigureController
{

	@Autowired
	FalsehoodUserService userService;
	
	@Autowired
	PublicFigureService pfService;
	

	public static final int MIN_CREDIT_ADD_FIGURE = 40;
	
	@PostMapping("/Add")
	ResponseEntity<String> addPublicFigure(RequestEntity<PublicFigureEntry> entry)
	{
		HttpHeaders headers = entry.getHeaders();
		
		String token = headers.getFirst("Authorization");
		
		FalsehoodUser user = userService.getUserFromToken(token);
		
		if(user == null)
		{
			return new ResponseEntity<String>("Could Not Authenticate User", HttpStatus.UNAUTHORIZED);
		}
		
		if( user.getCredit() < MIN_CREDIT_ADD_FIGURE)
		{
			return new ResponseEntity<String>("User did not have the Credibility needed", HttpStatus.FORBIDDEN);
		}
		
		String response = pfService.submitPublicFigure(entry.getBody());
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
}
