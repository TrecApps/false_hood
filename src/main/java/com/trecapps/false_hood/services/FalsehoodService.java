package com.trecapps.false_hood.services;

import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.model.Falsehood;
import com.trecapps.false_hood.model.MediaOutlet;
import com.trecapps.false_hood.repos.FalsehoodRepo;
import com.trecapps.false_hood.repos.FalsehoodStorageAws;

@Service
public class FalsehoodService {
	
	@Autowired
	FalsehoodRepo fRepo;
	
	@Autowired
	FalsehoodStorageAws s3BucketManager;
	
	public FalsehoodService()
	{
		System.out.println("Falsehood Service Constructor Called!");
	}
	

	public List<Falsehood> getFalsehoodsByOutlet(MediaOutlet outletId)
	{
		List<Falsehood> ret = fRepo.getFalsehoodsByOutlet(outletId);
		
		if(ret != null)
			System.out.println("Falsehoods Repo by outlet returned " + ret.size() + " entries with id=" + outletId + "!");
		else
			System.out.println("Falsehoods Repo returned null list with id = " + outletId + "!");
		
		return ret; 
	}
	
	public List<Falsehood> getFalseHoodByMinimumSeverity(byte severity)
	{
		return fRepo.getFalsehoodsByMinimumSeverity(severity);
	}
	
	public List<Falsehood> getFalseHoodByMaximumSeverity(byte severity)
	{
		return fRepo.getFalsehoodsByMaximumSeverity(severity);
	}
	
	public List<Falsehood> getFalsehoodBySeverityRange(byte min, byte max)
	{
		return fRepo.getFalshoodBySeverity(max, min);
	}
	
	public List<Falsehood> getFalsehoodByDateRange(Date oldest)
	{
		return getFalsehoodByDateRange(oldest, new Date(Calendar.getInstance().getTime().getTime()));
	}
	
	public List<Falsehood> getFalsehoodByDateRange(Date oldest, Date newest)
	{
		return fRepo.getFalsehoodsBetween(oldest, newest);
	}
	
	public List<Falsehood> getFalsehoodsBefore(Date newest)
	{
		return fRepo.getFalsehoodsBefore(newest);
	}
	
	public List<Falsehood> getFalsehoodByMediaType(int mt)
	{
		return fRepo.getFalsehoodsByMediaType(mt);
	}
	
	public Falsehood getFalsehoodById(BigInteger id)
	{
		return fRepo.getOne(id);
	}
	
	public List<Falsehood> getFalsehoodsByAuthor(String author)
	{
		return fRepo.getFalsehoodsByAuthor(author);
	}
	
	public Falsehood insertNewFalsehood(Falsehood f)
	{
		BigInteger id = fRepo.getMaxId();
		
		if(id == null)
		{
			byte[] param = {0};
			id = new BigInteger(param);
		}
		else
		{
			System.out.println("Current id " + id);
			id = id.add(BigInteger.ONE);
			System.out.println("New id " + id);
		}
		
		f.setId(id);
		
		f.setContentId(f.getId().toString() + "-" + f.getSource());
		
		return fRepo.save(f);
	}
	
	public Falsehood updateNewFalsehood(Falsehood f)
	{
		return fRepo.save(f);
	}
	
	public boolean insertEntryToStorage(String contents, Falsehood f)
	{
		String objectId = f.getContentId();
		
		System.out.println("Object ID inserting is " + objectId);
		
		return "Success".equals(s3BucketManager.addNewFile(objectId, contents));
		
	}
	
	public boolean appendEntryToStorage(String contents, Falsehood f)
	{
		return false;
	}
}
