package com.trecapps.false_hood.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.publicFigure.PublicFigureEntry;
import com.trecapps.false_hood.publicFigure.PublicFigureService;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

@RestController
@RequestMapping("/PublicFigure")
public class PublicFigureController extends AuthenticationControllerBase
{

	PublicFigureService pfService;
	

	public static final int MIN_CREDIT_ADD_FIGURE = 40;
	
	public static final int MIN_CREDIT_APPROVE_FIGURE = 60;
	
	public static final int MIN_CREDIT_VIEW_NON_APPROVE = 20;

	@Autowired
	public PublicFigureController(@Autowired FalsehoodUserService service,
								  @Autowired PublicFigureService pfService)
	{
		super(service);
		this.pfService = pfService;
	}
	
	@PostMapping("/Add")
	public ResponseEntity<String> addPublicFigure(RequestEntity<PublicFigureEntry> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_ADD_FIGURE);
		
		if(ret != null)
			return ret;
		
		String response = pfService.submitPublicFigure(entry.getBody(), user);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/Approve")
	public ResponseEntity<String> approvePublicFigure(RequestEntity<Long> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_FIGURE);
		
		if(ret != null)
			return ret;
		
		String response = pfService.approveRejectPublicFigure(entry.getBody(), true);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/Reject")
	public ResponseEntity<String> rejectPublicFigure(RequestEntity<Long> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_FIGURE);
		
		if(ret != null)
			return ret;
		
		String response = pfService.approveRejectPublicFigure(entry.getBody(), false);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/list")
	public ResponseEntity<List<PublicFigure>> getFigures(@SuppressWarnings("rawtypes") RequestEntity entry, @RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "20")int pageSize)
	{
		FalsehoodUser user = super.getUser(entry);
		
		boolean viewAll = (user != null && user.getCredit() > MIN_CREDIT_VIEW_NON_APPROVE);
		
		return new ResponseEntity<List<PublicFigure>>(pfService.getPublicFigures(viewAll, page, pageSize), HttpStatus.OK);
	}
	
	@GetMapping("/listByName/{name}")
	public List<PublicFigure> getFigures(@PathVariable("name")String name)
	{
		return pfService.getPublicFigure(name);
	}
	
	@GetMapping("/id/{id}")
	public PublicFigureEntry getPublicFigure(@PathVariable("id")Long id)
	{
		return pfService.getEntryById(id);
		
	}
}
