package com.trecapps.false_hood.publicFalsehoods;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.miscellanous.FalsehoodStorageHolder;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

@Service
public class PublicAttributeService {

	InstitutionRepo iRepo;

	RegionRepo rRepo;

	FalsehoodStorageHolder storage;

	@Autowired
	EntityManager em;
	
	FalsehoodUserService userService;
	

	@Autowired
	public PublicAttributeService(@Autowired FalsehoodStorageHolder storage,
									@Autowired RegionRepo rRepo,
									@Autowired InstitutionRepo iRepo,
									@Autowired FalsehoodUserService userService)
	{
		this.iRepo = iRepo;
		this.rRepo = rRepo;
		this.storage = storage;
		this.userService = userService;
	}
	
	public List<Institution> getInstitutionList(String name)
	{
		return iRepo.getLikeName(name);
	}
	
	public List<Region> getRegionList(String name)
	{
		return rRepo.getLikeName(name);
	}
	
	
	public String approveRejectRegion(Long id, boolean approve, FalsehoodUser curUser)
	{
		if(id == null)
		{
			return "Id of Region was null!";
		}
		
		if(!rRepo.existsById(id))
		{
			return "Id of Region does not currently exist in our Records!";
		}
		
		Region reg = rRepo.getOne(id);
		FalsehoodUser fUser = reg.getSubmitter();
		if(fUser.equals(curUser))
		{
			return "User that Submitted the Region cannot be the same one that Approves of it!";
		}
		
		byte setApproval = (byte) (reg.getApproved() + (byte)((approve) ? 1:-1));
		
		reg.setApproved(setApproval);
		
		rRepo.save(reg);
		
		
		
		if(fUser != null)
		{
			int points = (approve) ? 5 : -3;
			userService.adjustCredibility(fUser, points);
		}
		
		return "";
	}
	
	public String approveRejectInstitution(Long id, boolean approve, FalsehoodUser curUser)
	{
		if(id == null)
		{
			return "Id of Institution was null!";
		}
		
		if(!iRepo.existsById(id))
		{
			return "Id of Institution does not currently exist in our Records!";
		}
		
		Institution inst = iRepo.getOne(id);
		FalsehoodUser fUser = inst.getSubmitter();
		if(fUser.equals(curUser))
		{
			return "User that Submitted the Institution cannot be the same one that Approves of it!";
		}
		byte setApproval = (byte) (inst.getApproved() + (byte)((approve) ? 1:-1));
		
		inst.setApproved(setApproval);
		
		iRepo.save(inst);
		
		
		
		if(fUser != null)
		{
			int points = (approve) ? 5 : -3;
			userService.adjustCredibility(fUser, points);
		}
		
		return "";
	}
	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String InsertAttribute(InstitutionEntry i, FalsehoodUser user)
	{
		if(i == null)
			return "Null value detected";
		if(user == null)
			return "null User detected";
		
		if(i.getContents() == null)
			return "Null contents detected";
		if(i.getInstitution() == null)
			return "Null metadata detected";
		
		Institution inst = i.getInstitution();
		
		inst.setId(null);
		inst.setApproved((byte)0);
		inst.setSubmitter(user);
		
		inst = iRepo.saveAndFlush(inst);

		
		//iRepo.deleteById(null);
		
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
	
	public String InsertAttribute(RegionEntry i, FalsehoodUser user)
	{
		if(i == null)
			return "Null value detected";
		if(user == null)
			return "null User detected";
		
		if(i.getContents() == null)
			return "Null contents detected";
		if(i.getRegion() == null)
			return "Null metadata detected";
		
		Region reg = i.getRegion();
		
		reg.setId(null);
		reg.setApproved((byte)0);
		reg.setSubmitter(user);
		
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
