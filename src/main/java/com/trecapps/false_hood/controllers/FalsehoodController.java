package com.trecapps.false_hood.controllers;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.trecapps.false_hood.publicFigure.PublicFigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.FalsehoodService;
import com.trecapps.false_hood.falsehoods.FullFalsehood;
import com.trecapps.false_hood.falsehoods.MediaOutlet;
import com.trecapps.false_hood.falsehoods.MediaOutletEntry;
import com.trecapps.false_hood.falsehoods.MediaOutletService;
import com.trecapps.false_hood.falsehoods.SearchFalsehood;
import com.trecapps.false_hood.keywords.KeywordService;
import com.trecapps.false_hood.miscellanous.Severity;

@RestController
@RequestMapping("/Falsehood")
public class FalsehoodController {

	FalsehoodService service;

	KeywordService keyService;
	
	MediaOutletService mediaService;

	@Autowired
	public FalsehoodController(@Autowired FalsehoodService service,
							   @Autowired KeywordService keyService,
							   @Autowired MediaOutletService mediaService)
	{
		this.service = service;
		this.keyService = keyService;
		this.mediaService = mediaService;
	}
	
	@GetMapping("/outlet/{name}")
	public List<MediaOutlet> searchOutlets(@PathVariable("name")String name)
	{
		return mediaService.SearchOutletByName(name.replace('_', ' ').trim());
	}
	
	@GetMapping("/outletId/{id}")
	public MediaOutletEntry getOutlet(@PathVariable("id")Integer id)
	{
		return mediaService.GetOutletEntry(id);
	}
	
	@GetMapping("/id/{id}")
	public FullFalsehood GetFalsehood(@PathVariable("id")BigInteger id)
	{
		System.out.println("id endpoint hit! id = " + id);
		return service.getFalsehoodById(id);
	}
	
	Comparator<Falsehood> idCompare = (Falsehood f1, Falsehood f2) -> {
		return f1.getId().compareTo(f2.getId());
	};
	
	
	@PostMapping("/searchConfirmed")
	public List<Falsehood> searchFalsehoodByParams(@RequestBody SearchFalsehood searchObj)
	{
		return service.getConfirmedFalsehoodsBySearchFeatures(searchObj);
		
	}
	
	@PostMapping("/searchRejected")
	public List<Falsehood> searchRFalsehoodByParams(@RequestBody SearchFalsehood searchObj)
	{
		return service.getRejectedFalsehoodsBySearchFeatures(searchObj);
		
		
		
	}
	

}
