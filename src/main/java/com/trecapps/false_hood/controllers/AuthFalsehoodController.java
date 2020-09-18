package com.trecapps.false_hood.controllers;

import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trecapps.false_hood.model.Falsehood;
import com.trecapps.false_hood.model.FalsehoodUser;
import com.trecapps.false_hood.model.FullFalsehood;
import com.trecapps.false_hood.services.FalsehoodService;
import com.trecapps.false_hood.services.FalsehoodUserService;
import com.trecapps.false_hood.services.KeywordService;
import com.trecapps.false_hood.services.MediaOutletService;

@RestController
@RequestMapping("/Update/Falsehood")
public class AuthFalsehoodController {

	@Autowired
	FalsehoodService service;
	
	@Autowired
	MediaOutletService mediaService;
	
	@Autowired
	FalsehoodUserService userService;
	
	@Autowired
	KeywordService keyService;
	
	public static final int MIN_CREDIT_SUBMIT_NEW = 5;
	
	public static final int MIN_CREDIT_APPROVE_REJECT = 60;
	
	@GetMapping("/GetUser")
	ResponseEntity<FalsehoodUser> getUser(@RequestParam("userId")BigInteger userId)
	{
		return new ResponseEntity<FalsehoodUser>((FalsehoodUser)null, HttpStatus.OK);
	}
	
	@PostMapping("/Insert")
	ResponseEntity<String> insertFalsehood(RequestEntity<FullFalsehood> entity)
	{
		HttpHeaders headers = entity.getHeaders();
		
		String token = headers.getFirst("Authorization");
		
		FalsehoodUser user = userService.getUserFromToken(token);
		
		if(user == null)
		{
			return new ResponseEntity<String>("Could Not Authenticate User", HttpStatus.UNAUTHORIZED);
		}
		
		if( user.getCredit() < MIN_CREDIT_SUBMIT_NEW)
		{
			return new ResponseEntity<String>("User did not have the Credibility needed", HttpStatus.FORBIDDEN);
		}
		
		FullFalsehood falsehood = entity.getBody();
		
		if(falsehood == null || falsehood.getContents() == null || falsehood.getMetadata() == null)
		{
			return new ResponseEntity<String>("Bad Data", HttpStatus.BAD_REQUEST);
		}
		
		Falsehood meta = falsehood.getMetadata();
		
		meta.setStatus(Falsehood.SUBMITTED);
		
		meta.setDateMade(new Date(Calendar.getInstance().getTime().getTime()));
		
		
		
		meta = service.insertNewFalsehood(meta);
		
		
		
		if(!service.insertEntryToStorage(falsehood.getContents(), meta))
		{
			return new ResponseEntity<String>("Failed to Write Falsehood to Storage!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		String keys = falsehood.getKeywords();
		
		if(keys != null)
		{
			keyService.addKeywords(keys, meta);
		}
		
		
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/Update")
	ResponseEntity<String> approveFalsehood(RequestEntity<FullFalsehood> entity)
	{
		HttpHeaders headers = entity.getHeaders();
		
		String token = headers.getFirst("Authorization");
		
		FalsehoodUser user = userService.getUserFromToken(token);
		
		if(user == null)
		{
			return new ResponseEntity<String>("Could Not Authenticate User", HttpStatus.UNAUTHORIZED);
		}
		
		if( user.getCredit() < MIN_CREDIT_APPROVE_REJECT)
		{
			return new ResponseEntity<String>("Could Not Authenticate User", HttpStatus.FORBIDDEN);
		}
		
		FullFalsehood falsehood = entity.getBody();
		
		if(falsehood == null || falsehood.getContents() == null || falsehood.getMetadata() == null)
		{
			return new ResponseEntity<String>("Bad Data", HttpStatus.BAD_REQUEST);
		}
		
		if(!service.appendEntryToStorage(falsehood.getContents(), falsehood.getMetadata()))
		{
			return new ResponseEntity<String>("Failed to Write Falsehood to Storage!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		service.updateNewFalsehood(falsehood.getMetadata());
		
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
}
