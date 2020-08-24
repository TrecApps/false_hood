package com.trecapps.false_hood.controllers;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trecapps.false_hood.model.Falsehood;
import com.trecapps.false_hood.model.MediaOutlet;
import com.trecapps.false_hood.model.SearchFalsehood;
import com.trecapps.false_hood.model.Severity;
import com.trecapps.false_hood.services.FalsehoodService;
import com.trecapps.false_hood.services.KeywordService;
import com.trecapps.false_hood.services.MediaOutletService;

@RestController
@RequestMapping("/Falsehood")
public class FalsehoodController {

	@Autowired
	FalsehoodService service;
	
	@Autowired
	KeywordService keyService;
	
	@Autowired
	MediaOutletService mediaService;
	
	public FalsehoodController()
	{
		System.out.println("Falsehood Controller Constructor Called!");
	}
	
	
	@GetMapping("id/{id}")
	Falsehood GetFalsehood(@PathVariable("id")BigInteger id)
	{
		System.out.println("id endpoint hit! id = " + id);
		return service.getFalsehoodById(id);
	}
	
	Comparator<Falsehood> idCompare = (Falsehood f1, Falsehood f2) -> {
		return f1.getId().compareTo(f2.getId());
	};
	
	@PostMapping("/list")
	List<Falsehood> GetFalsehoodByParams(@RequestBody SearchFalsehood searchObj)
	{
		List<String> authors = searchObj.getAuthors();
		List<String> outletsStr = searchObj.getOutlets();
		
		List<MediaOutlet> outlets = null; 
		
		if(outletsStr != null)
		{
			System.out.println("Outlets Str is NOT null!");
			for(int Rust = 0; Rust < outletsStr.size(); Rust++)
				System.out.println(outletsStr.get(Rust));
			outlets = new ArrayList<>();
			for(int Rust = 0; Rust < outletsStr.size(); Rust++)
			{
				MediaOutlet out = mediaService.GetMediaOutlet(outletsStr.get(Rust));
				if(out != null)
				{
					System.out.println("Outlet " + out.getName() + " added!");
					outlets.add(out);
				}
				else
					System.out.println("Null Outlet detected!");
			}
		}
		else
			System.out.println("Outlets Str is null!");
		Date from = searchObj.getFrom();
		Date to = searchObj.getTo();
		
		Severity maxSeverity = searchObj.getMaximum();
		Severity minSeverity = searchObj.getMinimum();
		
		String terms = searchObj.getTerms();
		
		int max = searchObj.getNumberOfEntries();
		
		
		List<Falsehood> fromAuth = null;
		
		if(authors != null && authors.size() > 0)
		{
			fromAuth = service.getFalsehoodsByAuthor(authors.get(0));
			
			for (int Rust = 1; Rust < authors.size(); Rust++)
			{
				fromAuth.addAll(service.getFalsehoodsByAuthor(authors.get(Rust)));
			}
		}
		
		List<Falsehood> fromOut = null;
		
		if(outlets != null && outlets.size() > 0)
		{
			fromOut = new ArrayList<>();
			
			for (int Rust = 0; Rust < outlets.size(); Rust++)
			{
				fromOut.addAll(service.getFalsehoodsByOutlet(outlets.get(Rust)));
			}
		}
		
		List<Falsehood> fromDates = null;
		
		if(from != null && to != null)
		{
			fromDates = service.getFalsehoodByDateRange(from, to);
		}
		else if(from != null)
		{
			fromDates = service.getFalsehoodByDateRange(from);
		}
		else if(to != null)
		{
			fromDates = service.getFalsehoodsBefore(to);
		}
		
		List<Falsehood> fromSeverity = null;
		
		if(minSeverity != null && maxSeverity != null)
		{
			fromSeverity = service.getFalsehoodBySeverityRange(minSeverity.GetValue(), maxSeverity.GetValue());
		}
		else if(minSeverity != null)
		{
			fromSeverity = service.getFalseHoodByMinimumSeverity(minSeverity.GetValue());
		}
		else if(maxSeverity != null)
		{
			fromSeverity = service.getFalseHoodByMaximumSeverity(maxSeverity.GetValue());
		}
		
		List<Falsehood> fromTerms = null;
		
		if(terms != null && !terms.trim().equals(""))
		{
			List<String> termList = new LinkedList<String>();
			
		//	if(terms)
			
			while(terms.indexOf(" ") != -1)
			{
				boolean inQuotes = false;
				
				for(int c = 0; c < terms.length(); c++)
				{
					if(terms.charAt(c) == '\"')
					{
						inQuotes = !inQuotes;
						continue;
					}
					
					
					if(!inQuotes && terms.charAt(c) == ' ')
					{
						termList.add(terms.substring(0, c));
						
						terms = terms.substring(c).trim();
						break;
					}
				}
			}
			
			termList.add(terms);
			
			fromTerms = keyService.GetFalsehoodsBySearchTerms(termList);
		}
		
		
		// We have looked into all possible lists, now time to combine them
		
		List<Falsehood> falsehoodLists = new LinkedList<>();
		
		int listCount = 0;
		if(fromAuth != null)
		{
			System.out.println("Author List: " + fromAuth.size());
			for(int Rust = 0; Rust < fromAuth.size();Rust++)
			{
				System.out.println("\t" + fromAuth.get(Rust));
			}
			falsehoodLists.addAll(fromAuth);
			listCount++;
		}
		if(fromOut != null)
		{

			System.out.println("Outlet List: " + fromOut.size());
			for(int Rust = 0; Rust < fromOut.size();Rust++)
			{
				System.out.println("\t" + fromOut.get(Rust));
			}
			listCount++;
			falsehoodLists.addAll(fromOut);
		}
		if(fromDates != null)
		{

			System.out.println("Dates List: " + fromDates.size());
			for(int Rust = 0; Rust < fromDates.size();Rust++)
			{
				System.out.println("\t" + fromDates.get(Rust));
			}
			listCount++;
			falsehoodLists.addAll(fromDates);
		}
		
		if(fromSeverity != null)
		{
			System.out.println("Severity List: " + fromSeverity.size());
			for(int Rust = 0; Rust < fromSeverity.size();Rust++)
			{
				System.out.println("\t" + fromSeverity.get(Rust));
			}
			listCount++;
			falsehoodLists.addAll(fromSeverity);
		}
		
		if(fromTerms != null)
		{
			System.out.println("Terms List: " + fromTerms.size());
			for(int Rust = 0; Rust < fromTerms.size();Rust++)
			{
				System.out.println("\t" + fromTerms.get(Rust));
			}
			listCount++;
			falsehoodLists.addAll(fromTerms);
		}
		
		System.out.println("falsehood Lists now has " + falsehoodLists.size() + " in it!");
		for(int Rust = 0; Rust < falsehoodLists.size();Rust++)
		{
			System.out.println("\t" + falsehoodLists.get(Rust));
		}
		
		falsehoodLists = falsehoodLists.stream()
				.filter((Falsehood f) -> f != null) // Make sure no null values get involved
				.sorted(idCompare)					// sort results by id
				.collect(Collectors.toList());		// Collect into list
		
		List<Falsehood> ret = new ArrayList<>();
		
		for(int rust = 1, track = 1; rust < falsehoodLists.size();rust++)
		{
			if(falsehoodLists.get(rust).getId().equals(falsehoodLists.get(rust-1).getId()))
			{
				track++;
				
				if(track == listCount && rust == falsehoodLists.size() -1)
					ret.add(falsehoodLists.get(rust));
			}
			else
			{
				if(listCount == track)
					ret.add(falsehoodLists.get(rust-1));
				track = 1;
			}
			
			// Deal with the final set of falsehoods that should be in the 
		}
		int size;
		if((size = falsehoodLists.size()) > 1)
		{
			if(!falsehoodLists.get(size -1).getId().equals(falsehoodLists.get(size-2).getId()) && listCount == 1)
			{
				ret.add(falsehoodLists.get(size-1));
			}
		}
		
		
		return ret;
	}
}
