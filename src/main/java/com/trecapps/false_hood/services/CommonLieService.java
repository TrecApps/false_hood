package com.trecapps.false_hood.services;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.model.CommonLie;
import com.trecapps.false_hood.model.CommonLieSubmission;
import com.trecapps.false_hood.model.Falsehood;
import com.trecapps.false_hood.repos.CommonLieRepo;
import com.trecapps.false_hood.repos.FalsehoodRepo;
import com.trecapps.false_hood.repos.FalsehoodStorageAws;

@Service
public class CommonLieService {

	@Autowired
	CommonLieRepo clRepo;
	
	@Autowired
	FalsehoodRepo falsehoodRepo;
	
	@Autowired
	FalsehoodStorageAws awsStorageRepo;
	
	CommonLie GetLieById(long id)
	{
		return clRepo.getOne(id);
	}
	
	List<CommonLie> GetLieBySearchTerm(String title)
	{
		return clRepo.getCommonLieByTitleStart(title);
	}
	
	
	public String submitCommonLie(CommonLieSubmission cls)
	{
		if(cls.getFalsehoods().size() < 5)
		{
			return "Need five Falsehoods to establish a Common Lie!";
		}
		
		List<Falsehood> falsehoods = new LinkedList<Falsehood>();
		
		CommonLie lie = clRepo.save(cls.getLie());
		
		for(BigInteger bInt: cls.getFalsehoods())
		{
			if(!falsehoodRepo.existsById(bInt))
			{
				clRepo.delete(lie);
				return "Falsehood " + bInt + " does not exist in our records!";
			}
			
			
			falsehoods.add(falsehoodRepo.getOne(bInt));
		}
		
		String storageKey = "common_lie-" + lie.getId();
		
		if(!"Success".equals(awsStorageRepo.addNewFile(storageKey, cls.getContents())))
		{
			clRepo.delete(lie);
			return "Failed to write Common Lie to storage";
		}
		
		for(Falsehood f: falsehoods)
		{
			f.setCommonLie(lie);
			falsehoodRepo.save(f);
		}
		
		
		
		return "";
	}
	
	
}
