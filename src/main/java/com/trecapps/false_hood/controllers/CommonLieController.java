package com.trecapps.false_hood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trecapps.false_hood.model.CommonLieSubmission;
import com.trecapps.false_hood.model.FalsehoodUser;
import com.trecapps.false_hood.services.CommonLieService;
import com.trecapps.false_hood.services.FalsehoodUserService;

@RestController
@RequestMapping("/CommonLie")
public class CommonLieController {
	
	@Autowired
	CommonLieService clService;
	
	@Autowired
	FalsehoodUserService userService;
	
	public static final int MIN_CREDIT_PROPOSE_COMMON_LIE = 40;
	
	@PostMapping("/insert")
	ResponseEntity<String> insertCommonLie(RequestEntity<CommonLieSubmission> sub)
	{
		HttpHeaders headers = sub.getHeaders();
		
		String token = headers.getFirst("Authorization");
		
		FalsehoodUser user = userService.getUserFromToken(token);
		
		if(user == null)
		{
			return new ResponseEntity<String>("Could Not Authenticate User", HttpStatus.UNAUTHORIZED);
		}
		
		if( user.getCredit() < MIN_CREDIT_PROPOSE_COMMON_LIE)
		{
			return new ResponseEntity<String>("User did not have the Credibility needed", HttpStatus.FORBIDDEN);
		}
		
		String response = clService.submitCommonLie(sub.getBody());
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
}
