package com.trecapps.false_hood.publicFalsehoods;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.miscellanous.FalsehoodStorageHolder;

@Service
public class PublicAttributeService {

	InstitutionRepo iRepo;

	RegionRepo rRepo;

	FalsehoodStorageHolder storage;

	@Autowired
	public PublicAttributeService(@Autowired FalsehoodStorageHolder storage,
									@Autowired RegionRepo rRepo,
									@Autowired InstitutionRepo iRepo)
	{
		this.iRepo = iRepo;
		this.rRepo = rRepo;
		this.storage = storage;
	}
	
	public String InsertAttribute(InstitutionEntry i)
	{
		if(i == null)
			return "Null value detected";
		
		if(i.getContents() == null)
			return "Null contents detected";
		if(i.getInstitution() == null)
			return "Null metadata detected";
		
		Institution inst = i.getInstitution();
		
		inst.setId(null);
		
		inst = iRepo.save(inst);
		
		if(!"Success".equals(storage.addNewFile("Institution-" + inst.getId(), i.getContents())))
		{
			iRepo.delete(inst);
			return "Failed to save to storage!";
		}
		
		return "";
	}
	
	public InstitutionEntry getInstitution(long id)
	{
		if(!iRepo.existsById(id))
		{
			System.out.println("Repository " + id + " Doesn't Exist!");
			return null;
		}
		Institution i = iRepo.getOne(id);
		String s;
		try {
			s = storage.retrieveContents("Institution-" + id);
		} catch (IOException e) {
			s = "ERROR: " + e.getMessage();
		}
		return new InstitutionEntry(i,s);
	}
	
	public String UpdateAttribute(InstitutionEntry i)
	{
		if(i == null)
			return "Null value detected";
		
		if(i.getContents() == null)
			return "Null contents detected";
		if(i.getInstitution() == null)
			return "Null metadata detected";
		
		Institution inst = i.getInstitution();
		
		if(!iRepo.existsById(inst.getId()))
			return "Expected Institution to already exist";
		
		if(!"Success".equals(storage.appendFile("Institution-" + inst.getId(), i.getContents())))
		{
			return "Failed to save to storage!";
		}
		return "";
	}
	
	public String InsertAttribute(RegionEntry i)
	{
		if(i == null)
			return "Null value detected";
		
		if(i.getContents() == null)
			return "Null contents detected";
		if(i.getRegion() == null)
			return "Null metadata detected";
		
		Region reg = i.getRegion();
		
		reg.setId(null);
		
		reg = rRepo.save(reg);
		
		if(!"Success".equals(storage.addNewFile("Region-" + reg.getId(), i.getContents())))
		{
			rRepo.delete(reg);
			return "Failed to save to storage!";
		}
		
		return "";
	}
	
	public RegionEntry getRegion(long id)
	{
		if(!rRepo.existsById(id))
			return null;
		
		Region i = rRepo.getOne(id);
		String s;
		try {
			s = storage.retrieveContents("Region-" + id);
		} catch (IOException e) {
			s = "ERROR: " + e.getMessage();
		}
		return new RegionEntry(i,s);
	}
	
	public String UpdateAttribute(RegionEntry i)
	{
		if(i == null)
			return "Null value detected";
		
		if(i.getContents() == null)
			return "Null contents detected";
		if(i.getRegion() == null)
			return "Null metadata detected";
		
		Region reg = i.getRegion();
		
		if(!rRepo.existsById(reg.getId()))
			return "Expected Region to already exist";
		
		if(!"Success".equals(storage.appendFile("Region-" + reg.getId(), i.getContents())))
		{
			return "Failed to save to storage!";
		}
		return "";
	}
}
