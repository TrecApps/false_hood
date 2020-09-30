package com.trecapps.false_hood.services;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.model.FalsehoodAppeal;
import com.trecapps.false_hood.model.FalsehoodAppealEntry;
import com.trecapps.false_hood.model.FalsehoodAppealSignature;
import com.trecapps.false_hood.model.FalsehoodUser;
import com.trecapps.false_hood.repos.FalsehoodAppealRepo;
import com.trecapps.false_hood.repos.FalsehoodAppealSignatureRepo;
import com.trecapps.false_hood.repos.FalsehoodRepo;
import com.trecapps.false_hood.repos.FalsehoodStorageAws;

@Service
public class FalsehoodAppealService {

	@Autowired
	FalsehoodAppealRepo appealRepo;
	
	@Autowired
	FalsehoodAppealSignatureRepo signatureRepo;
	
	@Autowired
	FalsehoodRepo falsehoodRepo;
	
	@Autowired
	FalsehoodStorageAws awsStorage;
	
    final String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";
final int RANDOM_STRING_LENGTH = 30;
	
	public ResponseEntity<String> addAppeal(FalsehoodAppealEntry entry, FalsehoodUser user)
	{
		FalsehoodAppeal appeal = entry.getAppeal();
		
		BigInteger falsehoodId = appeal.getFalsehood().getId();
		
		if(!falsehoodRepo.existsById(falsehoodId))
			return new ResponseEntity<String>("References Falsehood does not Currently exist!", HttpStatus.BAD_REQUEST);
		
		appeal.setPetitioner(user);
		
		appeal.setRatified((byte)0);
		
		appeal = appealRepo.save(appeal);
		
		String storageResult = awsStorage.addNewFile("Appeal_entry-" + appeal.getId(), entry.getReason());
		
		if("Success".equals(storageResult))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		appealRepo.delete(appeal);
		
		return new ResponseEntity<String>("Failed to Write to storage", HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	public List<FalsehoodAppeal> getAppeals()
	{
		return appealRepo.getAppeals();
	}
	
	public FalsehoodAppealEntry getAppeal(BigInteger id)
	{
		FalsehoodAppealEntry ret = new FalsehoodAppealEntry();
		
		if(!appealRepo.existsById(id))
		{
			ret.setReason("Error! Appeal Not found!");
			return ret;
		}
		
		try
		{
			ret.setReason(awsStorage.retrieveContents("Appeal_entry-" + id));
			
			ret.setAppeal(appealRepo.getOne(id));
		}
		catch(Exception e)
		{
			ret.setReason(e.getMessage());
			
			ret.setAppeal(null);
		}
		
		return ret;
	}
	
	public String signAppeal(FalsehoodAppealSignature signature)
	{
		BigInteger apealId = signature.getAppeal().getId();
		
		if(!appealRepo.existsById(apealId))
			return "Appeal Referenced by Signature does not exist";
		
		List<FalsehoodAppealSignature> signatures = signatureRepo.getSignaturesByFalsehood(signature.getAppeal());
		
		for(FalsehoodAppealSignature signed : signatures)
		{
			if(signed.getId().equals(signature.getId()))
			{
				return "User had already signed this particular appeal. Can only sign once!";
			}
		}
		
		String validationToken = generateRandomString();
		
		// To-Do: make sure token is clean
		
		signature.setVerificationString(validationToken);
		
		// To-Do: Send Email to User regarding token
		
		
		signatureRepo.save(signature);
		
		return "";
		
	}
	
	
	private String generateRandomString()
	{
		StringBuilder sb = new StringBuilder();
		for(int c = 0; c < RANDOM_STRING_LENGTH; c++)
		{
			int ch = (int) (Math.random() * AlphaNumericString.length());
			sb.append(AlphaNumericString.charAt(ch));
		}
		return sb.toString();
	}
}
