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

    @PostMapping("/list")
    public List<PublicFalsehood> GetFalsehoodByParams(@RequestBody SearchPublicFalsehood searchObj)
    {
        // Get variables from Search Object
        List<PublicFigure> authors = searchObj.getAuthors();
        List<Region> regions = searchObj.getRegions();
        List<Institution> institutions = searchObj.getInstitutions();

        Date from = searchObj.getFrom();
        Date to = searchObj.getTo();

        Severity maxSeverity = searchObj.getMaximum();
        Severity minSeverity = searchObj.getMinimum();

        String terms = searchObj.getTerms();
        int max = searchObj.getNumberOfEntries();

        // Begin collecting entries from Search Object specs

        int listCount = 0;
        List<PublicFalsehood> falsehoodLists = new LinkedList<>();
        // 1. fromAuth - focusing on Public Figures
        if(authors != null && authors.size() > 0)
        {
            List<PublicFalsehood> fromAuth = service.getFalsehoodsByAuthor(authors.get(0));

            for (int Rust = 1; Rust < authors.size(); Rust++)
            {
                fromAuth.addAll(service.getFalsehoodsByAuthor(authors.get(Rust)));
            }
            listCount++;
            falsehoodLists.addAll(fromAuth);
        }

        // 2 fromDates - establishing a date range
        List<PublicFalsehood> fromDates = null;

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
        if(fromDates != null)
        {
            listCount++;
            falsehoodLists.addAll(fromDates);
            fromDates= null;
        }

        // 3. fromSeverity - focus on Severity
        List<PublicFalsehood> fromSeverity = null;

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
        if(fromSeverity != null) {
            listCount++;
            falsehoodLists.addAll(fromSeverity);
            fromSeverity = null;
        }

        // 4. fromTerms - focus on search terms
        if(terms != null && !terms.trim().equals(""))
        {
            List<String> termList = new LinkedList<String>();
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

            falsehoodLists.addAll(keyService.GetPublicFalsehoodsBySearchTerms(termList));
            listCount++;
        }

        // 5. fromRegion - focus on regions
        if(regions != null && regions.size() > 0)
        {
            List<PublicFalsehood> fromReg = service.getFalsehoodsByRegion(regions.get(0));

            for (int Rust = 1; Rust < regions.size(); Rust++)
            {
                fromReg.addAll(service.getFalsehoodsByRegion(regions.get(Rust)));
            }
            listCount++;
            falsehoodLists.addAll(fromReg);
        }

        // 6. Institution
        if(institutions != null && institutions.size() > 0)
        {
            List<PublicFalsehood> fromReg = service.getFalsehoodsByInstitution(institutions.get(0));

            for (int Rust = 1; Rust < institutions.size(); Rust++)
            {
                fromReg.addAll(service.getFalsehoodsByInstitution(institutions.get(Rust)));
            }
            listCount++;
            falsehoodLists.addAll(fromReg);
        }
        falsehoodLists = falsehoodLists.stream()
                .filter((PublicFalsehood f) -> f != null) // Make sure no null values get involved
                .sorted(idCompare)					// sort results by id
                .collect(Collectors.toList());		// Collect into list

        List<PublicFalsehood> ret = new ArrayList<>();

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
