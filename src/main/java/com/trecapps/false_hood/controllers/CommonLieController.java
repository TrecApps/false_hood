package com.trecapps.false_hood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trecapps.false_hood.commonLie.CommonLieService;
import com.trecapps.false_hood.commonLie.CommonLieSubmission;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

@RestController
@RequestMapping("/CommonLie")
public class CommonLieController extends AuthenticationControllerBase
{
	
	@Autowired
	CommonLieService clService;
	
	public static final int MIN_CREDIT_PROPOSE_COMMON_LIE = 40;
	
	public CommonLieController(@Autowired FalsehoodUserService service)
	{
		super(service);
	}
	
	@PostMapping("/insert")
	ResponseEntity<String> insertCommonLie(RequestEntity<CommonLieSubmission> sub)
	{
		FalsehoodUser user = super.getUser(sub);
		
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
