package com.trecapps.false_hood.controllers;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trecapps.false_hood.appeals.FalsehoodAppeal;
import com.trecapps.false_hood.appeals.FalsehoodAppealEntry;
import com.trecapps.false_hood.appeals.FalsehoodAppealService;
import com.trecapps.false_hood.appeals.FalsehoodAppealSignature;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

@RestController
@RequestMapping("/Appeal")
public class FalsehoodAppealController extends AuthenticationControllerBase
{

	FalsehoodAppealService appealService;
	@Autowired
	public FalsehoodAppealController(@Autowired FalsehoodUserService service,
									 @Autowired FalsehoodAppealService appealService)
	{
		super(service);
		this.appealService = appealService;
	}
	
	private static final int MIN_CREDIT_SUBMIT_APPEAL = 25;
	
	private static final int MIN_CREDIT_PETITION_APPEAL = 15;
	
	
	@GetMapping("/List")
	public ResponseEntity<List<FalsehoodAppeal>> getAppeals()
	{
		return new ResponseEntity<List<FalsehoodAppeal>>(appealService.getAppeals() ,HttpStatus.OK);
	}
	
	@GetMapping("/entry")
	public ResponseEntity<FalsehoodAppealEntry> getAppeal(@RequestParam(required = true) BigInteger id)
	{
		FalsehoodAppealEntry retEntry = appealService.getAppeal(id);
		
		if(retEntry.getAppeal() == null)
		{
			if("Error! Appeal Not found!".equals(retEntry.getReason()))
				return new ResponseEntity<FalsehoodAppealEntry>(retEntry, HttpStatus.NOT_FOUND);
			return new ResponseEntity<FalsehoodAppealEntry>(retEntry, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<FalsehoodAppealEntry>(retEntry, HttpStatus.OK);
	}
	
	@PostMapping(value = "/Add")
	public ResponseEntity<String> addAppeal(RequestEntity<FalsehoodAppealEntry> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_SUBMIT_APPEAL);
		
		if(ret != null)
			return ret;
		
		FalsehoodAppealEntry aEntry = entry.getBody();
		
		if(aEntry == null || aEntry.getAppeal() == null || aEntry.getReason() == null)
			return new ResponseEntity<String>("Found Null", HttpStatus.BAD_REQUEST);
		
		return appealService.addAppeal(aEntry, user);
	}
	
	
	
	
	@PostMapping("/Petition")
	public ResponseEntity<String> signPetition(RequestEntity<FalsehoodAppealSignature> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_PETITION_APPEAL);
		if(ret != null)
			return ret;
		
		FalsehoodAppealSignature sign = entry.getBody();
		
		if(sign == null)
			return new ResponseEntity<String>("Null Data provided!", HttpStatus.BAD_REQUEST);
		
		sign.setUser(user);
		
		String retString = appealService.signAppeal(sign, user.getEmail());
		
		if("".equals(retString))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(retString, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping(value= "/Petition", consumes="application/x-www-form-urlencoded")
	public ResponseEntity<String> verifyPetitionSignature(RequestEntity<MultiValueMap<String, String>> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_PETITION_APPEAL);
		if(ret != null)
			return ret;
		
		MultiValueMap<String, String> map = entry.getBody();
		
		if(map == null)
			return new ResponseEntity<String>("Null Body Provided!", HttpStatus.BAD_REQUEST);
		
		String idStr = map.getFirst("appealId");
		String token = map.getFirst("validation");
		
		String result;
		
		try
		{
			BigInteger id = new BigInteger(idStr);
			
			result = appealService.verifySignature(user, id, token);
			
		}
		catch(NumberFormatException e)
		{
			return new ResponseEntity<String>("The 'appealId' needs to be a number!", HttpStatus.BAD_REQUEST);
		}
		
		if("".equals(result))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return  new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
	}
}
