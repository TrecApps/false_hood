package com.trecapps.false_hood.publicFalsehoods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.miscellanous.FalsehoodStorageAws;

@Service
public class PublicAttributeService {

	InstitutionRepo iRepo;

	RegionRepo rRepo;

	FalsehoodStorageAws storage;

	@Autowired
	public PublicAttributeService(@Autowired FalsehoodStorageAws storage,
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
		if(i.getInsitution() == null)
			return "Null metadata detected";
		
		Institution inst = i.getInsitution();
		
		inst.setId(null);
		
		inst = iRepo.save(inst);
		
		if(!"Success".equals(storage.addNewFile("Institution-" + inst.getId(), i.getContents())))
		{
			iRepo.delete(inst);
			return "Failed to save to storage!";
		}
		
		return "";
	}
	
	public String UpdateAttribute(InstitutionEntry i)
	{
		if(i == null)
			return "Null value detected";
		
		if(i.getContents() == null)
			return "Null contents detected";
		if(i.getInsitution() == null)
			return "Null metadata detected";
		
		Institution inst = i.getInsitution();
		
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
