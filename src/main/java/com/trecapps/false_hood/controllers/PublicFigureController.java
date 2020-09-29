package com.trecapps.false_hood.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trecapps.false_hood.model.FalsehoodUser;
import com.trecapps.false_hood.model.PublicFigure;
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
	
	public static final int MIN_CREDIT_APPROVE_FIGURE = 60;
	
	public static final int MIN_CREDIT_VIEW_NON_APPROVE = 20;
	
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
		
		String response = pfService.submitPublicFigure(entry.getBody(), user);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/Approve")
	ResponseEntity<String> approvePublicFigure(RequestEntity<Long> entry)
	{
		HttpHeaders headers = entry.getHeaders();
		
		String token = headers.getFirst("Authorization");
		
		FalsehoodUser user = userService.getUserFromToken(token);
		
		if(user == null)
		{
			return new ResponseEntity<String>("Could Not Authenticate User", HttpStatus.UNAUTHORIZED);
		}
		
		if( user.getCredit() < MIN_CREDIT_APPROVE_FIGURE)
		{
			return new ResponseEntity<String>("User did not have the Credibility needed", HttpStatus.FORBIDDEN);
		}
		
		String response = pfService.approveRejectPublicFigure(entry.getBody(), true);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/Reject")
	ResponseEntity<String> rejectPublicFigure(RequestEntity<Long> entry)
	{
		HttpHeaders headers = entry.getHeaders();
		
		String token = headers.getFirst("Authorization");
		
		FalsehoodUser user = userService.getUserFromToken(token);
		
		if(user == null)
		{
			return new ResponseEntity<String>("Could Not Authenticate User", HttpStatus.UNAUTHORIZED);
		}
		
		if( user.getCredit() < MIN_CREDIT_APPROVE_FIGURE)
		{
			return new ResponseEntity<String>("User did not have the Credibility needed", HttpStatus.FORBIDDEN);
		}
		
		String response = pfService.approveRejectPublicFigure(entry.getBody(), false);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/list")
	ResponseEntity<List<PublicFigure>> getFigures(@SuppressWarnings("rawtypes") RequestEntity entry, @RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "20")int pageSize)
	{
		HttpHeaders headers = entry.getHeaders();
		
		String token = headers.getFirst("Authorization");
		
		FalsehoodUser user = userService.getUserFromToken(token);
		
		boolean viewAll = (user != null && user.getCredit() > MIN_CREDIT_VIEW_NON_APPROVE);
		
		return new ResponseEntity<List<PublicFigure>>(pfService.getPublicFigures(viewAll, page, pageSize), HttpStatus.OK);
	}
}
