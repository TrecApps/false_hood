package com.trecapps.false_hood.commonLie;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.FalsehoodRepo;
import com.trecapps.false_hood.miscellanous.FalsehoodStorageHolder;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehood;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehoodRepo;

@Service
public class CommonLieService {

	CommonLieRepo clRepo;

	FalsehoodRepo falsehoodRepo;

	FalsehoodStorageHolder awsStorageRepo;
	PublicFalsehoodRepo publicFalsehoodRepo;

	@Autowired
	public CommonLieService(@Autowired CommonLieRepo clRepo,
							@Autowired FalsehoodRepo falsehoodRepo,
							@Autowired PublicFalsehoodRepo publicFalsehoodRepo, 
							@Autowired FalsehoodStorageHolder awsStorageRepo)
	{
		this.awsStorageRepo = awsStorageRepo;
		this.clRepo = clRepo;
		this.falsehoodRepo = falsehoodRepo;
		this.publicFalsehoodRepo = publicFalsehoodRepo;
	}

	
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
		if(cls.getFalsehoods().size() + cls.getPublicFalsehoods().size() < 5)
		{
			return "Need five Falsehoods to establish a Common Lie!";
		}
		
		List<Falsehood> falsehoods = new LinkedList<Falsehood>();
		List<PublicFalsehood> pFalsehoods = new LinkedList<>();
		
		CommonLie lie = clRepo.save(cls.getLie());
		
		// Update Regular Media Falsehoods to point to the Common Lie
		for(BigInteger bInt: cls.getFalsehoods())
		{
			if(!falsehoodRepo.existsById(bInt))
			{
				clRepo.delete(lie);
				return "Falsehood " + bInt + " does not exist in our records!";
			}
			
			
			falsehoods.add(falsehoodRepo.getOne(bInt));
		}
		
		// Update Public Falsehoods to point to the common Lie
		for(BigInteger bInt: cls.getPublicFalsehoods())
		{
			if(!publicFalsehoodRepo.existsById(bInt))
			{
				clRepo.delete(lie);
				return "Falsehood " + bInt + " does not exist in our records!";
			}
			
			
			pFalsehoods.add(publicFalsehoodRepo.getOne(bInt));
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
		
		for(PublicFalsehood f: pFalsehoods)
		{
			f.setCommonLie(lie);
			publicFalsehoodRepo.save(f);
		}
		
		return "";
	}
	
	
}
