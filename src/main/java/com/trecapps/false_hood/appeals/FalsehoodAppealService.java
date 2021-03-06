package com.trecapps.false_hood.appeals;

import java.math.BigInteger;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.FalsehoodRepo;
import com.trecapps.false_hood.miscellanous.FalsehoodEmailService;
import com.trecapps.false_hood.miscellanous.FalsehoodStatus;
import com.trecapps.false_hood.miscellanous.FalsehoodStorageAws;
import com.trecapps.false_hood.miscellanous.FalsehoodStorageHolder;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehood;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehoodRepo;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserRepo;

@Service
public class FalsehoodAppealService {

	FalsehoodAppealRepo appealRepo;

	FalsehoodAppealSignatureRepo signatureRepo;

	FalsehoodRepo falsehoodRepo;
	
	PublicFalsehoodRepo pFalsehoodRepo;

	FalsehoodUserRepo userRepo;

	FalsehoodEmailService emailService;

	FalsehoodStorageHolder awsStorage;

	@Autowired
	public FalsehoodAppealService(@Autowired FalsehoodAppealRepo appealRepo,
								  @Autowired FalsehoodAppealSignatureRepo signatureRepo,
								  @Autowired FalsehoodRepo falsehoodRepo,
								  @Autowired PublicFalsehoodRepo pFalsehoodRepo,
								  @Autowired FalsehoodUserRepo userRepo,
								  @Autowired FalsehoodEmailService emailService,
								  @Autowired FalsehoodStorageHolder awsStorage)
	{
		this.appealRepo = appealRepo;
		this.signatureRepo = signatureRepo;
		this.falsehoodRepo = falsehoodRepo;
		this.userRepo = userRepo;
		this.pFalsehoodRepo = pFalsehoodRepo;
		this.emailService = emailService;
		this.awsStorage = awsStorage;
	}
	
    final String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";
final int RANDOM_STRING_LENGTH = 30;
	
	public ResponseEntity<String> addAppeal(FalsehoodAppealEntry entry, FalsehoodUser user)
	{
		FalsehoodAppeal appeal = entry.getAppeal();
		
		if(appeal == null)
			return new ResponseEntity<String>("Null Appeal Object provided", HttpStatus.BAD_REQUEST);
		
		if(appeal.getFalsehood() == null && appeal.getpFalsehood() == null)
			return new ResponseEntity<String>("Appeal needs to reference an exiting Media Falsehood OR an existing Public Falsehood", HttpStatus.BAD_REQUEST);
		
		if(appeal.getFalsehood() != null && appeal.getpFalsehood() != null)
			return new ResponseEntity<String>("Appeal needs to reference an exiting Media Falsehood OR an existing Public Falsehood", HttpStatus.BAD_REQUEST);
		
		
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
	
	public String signAppeal(FalsehoodAppealSignature signature, String email)
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
		try {
			emailService.sendValidationEmail(email, "Falsehood Appeal Verification", validationToken, signature.getAppeal().getId().toString());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Failed to send Email with verification token";
		}
		
		signatureRepo.save(signature);
		
		return "";
		
	}
	
	public String verifySignature(FalsehoodUser user, BigInteger id, String token)
	{
		if(!appealRepo.existsById(id))
		{
			return "Indended Appeal to sign does not exist";
		}
		
		FalsehoodAppeal appeal = appealRepo.getOne(id);
		
		
		
		
		List<FalsehoodAppealSignature> signatures = signatureRepo.getSignaturesByFalsehood(appeal);
		
		for(FalsehoodAppealSignature sign : signatures)
		{
			if(sign.getUser().equals(user))
			{
				if(sign.getVerificationString().equals(token))
				{
					sign.setVerificationString("");
					signatureRepo.save(sign);
					
					
					
					return scanSignatures(signatures, appeal);
				}
				else
				{
					System.out.println("Submitted Token: " + token);
					System.out.println("Expected Token : " + sign.getVerificationString());
					return "Signature token did not match!";
				}
			}
		}
		
		return "Could not find the Signature to validate!";
	}
	
	
	private String scanSignatures(List<FalsehoodAppealSignature> signatures, FalsehoodAppeal appeal)
	{
		int signatureCount = 0;
		for(FalsehoodAppealSignature sign: signatures)
		{
			if("".equals(sign.getVerificationString()) )
			{
				if(sign.getGrantAnon() > (byte)0)
					signatureCount++;
				else
					signatureCount += 2;
			}
		}
		
		if(signatureCount >= 100)
		{
			appeal.setRatified((byte)1);
			
			FalsehoodUser user = appeal.getPetitioner();
			
			user.setCredit(user.getCredit() + 5);
			user = userRepo.save(user);
			
			appeal.setPetitioner(user);
			
			appealRepo.save(appeal);
			
			Falsehood f = appeal.getFalsehood();
			PublicFalsehood pf = appeal.getpFalsehood();
			
			switch(appeal.getDesiredState())
			{
			case "Upgrade":
				if(f != null)
				{
					f.upgrade();
					this.falsehoodRepo.save(f);
				}
				else
				{
					pf.upgrade();
					this.pFalsehoodRepo.save(pf);
				}
				break;
			case "Challenge":
				if(f != null)
				{
					f.setStatus(FalsehoodStatus.CHALLENGED.GetValue());
					this.falsehoodRepo.save(f);
				}
				else
				{
					pf.setStatus(FalsehoodStatus.CHALLENGED.GetValue());
					this.pFalsehoodRepo.save(pf);
				}
				break;
			case "Corrected":
				if(f != null)
				{
					int curStatus = f.getStatus();
					if(curStatus > 4)
					{
						
						return "Cannot allow falsehood to be 'corrected' if it is considered Rejected!";
					}
					f.setStatus((byte) (f.getStatus() + (byte)10));
					this.falsehoodRepo.save(f);
				}
				else
				{
					int curStatus = pf.getStatus();
					if(curStatus > 4)
					{
					
						return "Cannot allow falsehood to be 'corrected' if it is considered Rejected!";
					}
					pf.setStatus((byte) (pf.getStatus() + (byte)10));
					this.pFalsehoodRepo.save(pf);
				}
				break;
			}
		}
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
