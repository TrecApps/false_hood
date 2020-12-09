package com.trecapps.false_hood.controllers;

import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.SearchFalsehood;
import com.trecapps.false_hood.keywords.KeywordService;
import com.trecapps.false_hood.miscellanous.Severity;
import com.trecapps.false_hood.publicFalsehoods.*;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/PublicFalsehood")
public class PublicFalsehoodController
{
    KeywordService keyService;

    PublicFalsehoodService service;
    
    PublicAttributeService attService;

    @Autowired
    public PublicFalsehoodController(@Autowired KeywordService keyService,
                                     @Autowired PublicFalsehoodService service,
                                     @Autowired PublicAttributeService attService)
    {
        this.keyService = keyService;
        this.service = service;
        this.attService = attService;
    }

    @GetMapping("/id/{id}")
    public PublicFalsehood GetFalsehood(@PathVariable("id") BigInteger id)
    {
        return service.getFalsehoodById(id);
    }

    Comparator<PublicFalsehood> idCompare = (PublicFalsehood f1, PublicFalsehood f2) -> {
        return f1.getId().compareTo(f2.getId());
    };
    
    @GetMapping("/Regions/{name}")
    public List<Region> getRegionsBySearchTerm(@PathVariable("name") String name)
    {
    	return attService.getRegionList(name.replace('_', ' ').trim());
    }
    
    @GetMapping("/Institutions/{name}")
    public List<Institution> getInstitutionBySearchTerm(@PathVariable("name") String name)
    {
    	return attService.getInstitutionList(name.replace('_', ' ').trim());
    }
    
    @GetMapping("/Region/{id}")
    public RegionEntry getRegionsById(@PathVariable("id") Long id)
    {
    	return attService.getRegion(id);
    }
    
    @GetMapping("/Institution/{id}")
    public InstitutionEntry getInstitutionById(@PathVariable("id") Long id)
    {
    	return attService.getInstitution(id);
    }
    
	@PostMapping("/searchConfirmed")
	public List<PublicFalsehood> searchFalsehoodByParams(@RequestBody SearchPublicFalsehood searchObj)
	{
		List<PublicFalsehood> traits = service.searchConfirmedFalsehoodsByAttribte(searchObj);
		
		List<String> termList = new LinkedList<String>();
		String terms = searchObj.getTerms();
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
		
		int start = searchObj.getNumberOfEntries() * searchObj.getPage();
		int end = start + searchObj.getNumberOfEntries();
		
		if(traits.size() == 0)
		{
			return keyService.GetPublicFalsehoodsBySearchTerms(termList).subList(start, end);
			
		}
		else
		{
			return traits;
		}
		
	}
	
	@PostMapping("/searchRejected")
	public List<PublicFalsehood> searchRFalsehoodByParams(@RequestBody SearchPublicFalsehood searchObj)
	{
		List<PublicFalsehood> traits = service.searchRejectedFalsehoodsByAttribte(searchObj);
		
		List<String> termList = new LinkedList<String>();
		String terms = searchObj.getTerms();
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
		
		int start = searchObj.getNumberOfEntries() * searchObj.getPage();
		int end = start + searchObj.getNumberOfEntries();
		
		if(traits.size() == 0)
		{
			return keyService.GetPublicFalsehoodsBySearchTerms(termList).subList(start, end);
			
		}
		else
		{
			return traits;
		}
		
	}
    
}
