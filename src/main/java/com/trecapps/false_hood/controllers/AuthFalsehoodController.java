package com.trecapps.false_hood.controllers;

import java.math.BigInteger;

import com.trecapps.false_hood.miscellanous.FalsehoodStatus;
import com.trecapps.false_hood.miscellanous.VerdictSubmission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.FalsehoodService;
import com.trecapps.false_hood.falsehoods.FullFalsehood;
import com.trecapps.false_hood.falsehoods.MediaOutletEntry;
import com.trecapps.false_hood.falsehoods.MediaOutletService;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Update/Falsehood")
public class AuthFalsehoodController extends AuthenticationControllerBase 
{
	FalsehoodService service;

	MediaOutletService mediaService;

	
	public static final int MIN_CREDIT_SUBMIT_NEW = 5;
	
	public static final int MIN_CREDIT_APPROVE_REJECT = 60;
	
	public static final int MIN_CREDIT_APPROVE_REJECT_RESOURCE = 200;
	
	public static final int MIN_CREDIT_ADD_OUTLET = 40;

	@Autowired
	public AuthFalsehoodController(@Autowired FalsehoodUserService userService,
								   @Autowired FalsehoodService service,
								   @Autowired MediaOutletService mediaService)
	{
		super(userService);
		this.service = service;
		this.mediaService = mediaService;
	}
	
	@GetMapping("/GetUser")
	ResponseEntity<FalsehoodUser> getUser(@RequestParam("userId")BigInteger userId)
	{
		return new ResponseEntity<FalsehoodUser>((FalsehoodUser)null, HttpStatus.OK);
	}
	
	@PostMapping("/Insert")
	public ResponseEntity<String> insertFalsehood(RequestEntity<FullFalsehood> entity, HttpServletRequest req)
	{		
		FalsehoodUser user = super.getUser(entity);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_SUBMIT_NEW);
		
		if(ret != null)
			return ret;
		
		FullFalsehood falsehood = entity.getBody();
		
		
		if(falsehood == null || falsehood.getContents() == null || falsehood.getMetadata() == null)
		{
			return new ResponseEntity<String>("Bad Data", HttpStatus.BAD_REQUEST);
		}
		

		
		Falsehood meta = falsehood.getMetadata();
		if(meta.getTags() != null && meta.getTags().length() > 400)
			return new ResponseEntity<String>(String.format("Falsehood keys field was %d characters (400 max)", meta.getTags().length()), HttpStatus.BAD_REQUEST);
		
		meta.setStatus(FalsehoodStatus.SUBMITTED.GetValue());
		
		// meta.setDateMade(new Date(Calendar.getInstance().getTime().getTime()));
		
		
		
		meta = service.insertNewFalsehood(meta);
		
		
		
		if(!service.insertEntryToStorage(falsehood.getContents(), meta, user,req))
		{
			return new ResponseEntity<String>("Failed to Write Falsehood to Storage!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/Approve")
	public ResponseEntity<String> approveFalsehood(RequestEntity<VerdictSubmission> entity, HttpServletRequest req)
	{
		FalsehoodUser user = super.getUser(entity);

		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT);

		if(ret != null)
			return ret;

		VerdictSubmission falsehood = entity.getBody();

		String result = service.addVerdict(falsehood.getFalsehood(), 1, falsehood.getComment(), user, req);

		if("".equals(result))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/Reject")
	public ResponseEntity<String> rejectFalsehood(RequestEntity<VerdictSubmission> entity, HttpServletRequest req)
	{
		FalsehoodUser user = super.getUser(entity);

		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT);

		if(ret != null)
			return ret;

		VerdictSubmission falsehood = entity.getBody();

		String result = service.addVerdict(falsehood.getFalsehood(), falsehood.isShouldStrike() ? -1 : 0, falsehood.getComment(), user, req);

		if("".equals(result))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
	}

	
	@PutMapping("/Update")
	public ResponseEntity<String> updateFalsehood(RequestEntity<FullFalsehood> entity,HttpServletRequest ip)
	{
		FalsehoodUser user = super.getUser(entity);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT);
		
		if(ret != null)
			return ret;
		
		FullFalsehood falsehood = entity.getBody();
		
		if(falsehood == null || falsehood.getContents() == null || falsehood.getMetadata() == null)
		{
			return new ResponseEntity<String>("Bad Data", HttpStatus.BAD_REQUEST);
		}
		
		if(!service.appendEntryToStorage(falsehood.getContents(), falsehood.getMetadata(), user, ip))
		{
			return new ResponseEntity<String>("Failed to Write Falsehood to Storage!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		service.updateNewFalsehood(falsehood.getMetadata());
		
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/AddOutlet")
	public ResponseEntity<String> addMediaOutlet(RequestEntity<MediaOutletEntry> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_ADD_OUTLET);
		
		if(ret != null)
			return ret;
		
		String response = this.mediaService.submitMediaOutlet(entry.getBody(), user);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/ApproveOutlet")
	public ResponseEntity<String> approveMediaOutlet(RequestEntity<Integer> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT_RESOURCE);
		
		if(ret != null)
			return ret;
		
		String response = mediaService.approveRejectMediaOutlet(entry.getBody(), true);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/RejectOutlet")
	public ResponseEntity<String> rejectMediaOutlet(RequestEntity<Integer> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT_RESOURCE);
		
		if(ret != null)
			return ret;
		
		String response = mediaService.approveRejectMediaOutlet(entry.getBody(), false);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
}
