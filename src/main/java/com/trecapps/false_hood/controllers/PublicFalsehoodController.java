package com.trecapps.false_hood.controllers;

import com.trecapps.false_hood.publicFalsehoods.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.*;

@RestController
@RequestMapping("/PublicFalsehood")
public class PublicFalsehoodController
{
    PublicFalsehoodService service;
    
    PublicAttributeService attService;

    @Autowired
    public PublicFalsehoodController(@Autowired PublicFalsehoodService service,
                                     @Autowired PublicAttributeService attService)
    {
        this.service = service;
        this.attService = attService;
    }

    @GetMapping("/id/{id}")
    public FullPublicFalsehood GetFalsehood(@PathVariable("id") BigInteger id)
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
    
    @GetMapping("/ByRegion/{id}")
    public List<PublicFalsehood> searchFalseoodsByRegion(@PathVariable("id") Long id, @RequestParam(value="size", defaultValue="20", required=false)int size,
			@RequestParam(value="page", defaultValue="0", required=false)int page)
    {
    	RegionEntry r = attService.getRegion(id);
    	if(r == null || r.getRegion() == null)
    		return null;
    	return service.searchByRegion(r.getRegion(), size, page);
    }
    
    @GetMapping("/ByInstitution/{id}")
    public List<PublicFalsehood> searchFalseoodsByInstitution(@PathVariable("id") Long id, @RequestParam(value="size", defaultValue="20", required=false)int size,
			@RequestParam(value="page", defaultValue="0", required=false)int page)
    {
    	InstitutionEntry r = attService.getInstitution(id);
    	if(r == null || r.getInstitution() == null)
    		return null;
    	return service.searchByInstitution(r.getInstitution(), size, page);
    }
    
    @GetMapping("/Institution/{id}")
    public InstitutionEntry getInstitutionById(@PathVariable("id") Long id)
    {
    	return attService.getInstitution(id);
    }
    
	@PostMapping("/searchConfirmed")
	public List<PublicFalsehood> searchFalsehoodByParams(@RequestBody SearchPublicFalsehood searchObj)
	{
		return service.searchConfirmedFalsehoodsByAttribte(searchObj);
	}
	
	@PostMapping("/searchRejected")
	public List<PublicFalsehood> searchRFalsehoodByParams(@RequestBody SearchPublicFalsehood searchObj)
	{
		return service.searchRejectedFalsehoodsByAttribte(searchObj);	
	}
	
	@GetMapping("/searchSubmitted")
	public List<PublicFalsehood> searchSubmittedFalsehoods(@RequestParam(value="size", defaultValue="20", required=false)int size,
			@RequestParam(value="page", defaultValue="0", required=false)int page)
	{
		return service.getSubmittedFalsehoods(size, page);
	}
    
}
