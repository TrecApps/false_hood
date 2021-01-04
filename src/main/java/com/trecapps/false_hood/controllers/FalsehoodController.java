package com.trecapps.false_hood.controllers;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.FalsehoodService;
import com.trecapps.false_hood.falsehoods.FullFalsehood;
import com.trecapps.false_hood.falsehoods.MediaOutlet;
import com.trecapps.false_hood.falsehoods.MediaOutletEntry;
import com.trecapps.false_hood.falsehoods.MediaOutletService;
import com.trecapps.false_hood.falsehoods.SearchFalsehood;

@RestController
@RequestMapping("/Falsehood")
public class FalsehoodController {

	FalsehoodService service;

	
	MediaOutletService mediaService;

	@Autowired
	public FalsehoodController(@Autowired FalsehoodService service,
							   @Autowired MediaOutletService mediaService)
	{
		this.service = service;
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
	
	@GetMapping("/searchSubmitted")
	public List<Falsehood> searchSubmittedFalsehoods(@RequestParam(value="size", defaultValue="20", required=false)int size,
			@RequestParam(value="page", defaultValue="0", required=false)int page)
	{
		return service.getSubmittedFalsehoods(size, page);
	}
	

}
