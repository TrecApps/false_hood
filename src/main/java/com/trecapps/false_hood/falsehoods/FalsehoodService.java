package com.trecapps.false_hood.falsehoods;

import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import com.trecapps.false_hood.json.VerdictListObj;
import com.trecapps.false_hood.json.VerdictObj;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.miscellanous.FalsehoodStorageAws;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehood;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehoodRepo;

import javax.servlet.http.HttpServletRequest;

@Service
public class FalsehoodService {
	
	@Autowired
	FalsehoodRepo fRepo;

	public static final int MIN_CREDIT_APPROVE_REJECT = 60;

	@Autowired
	FalsehoodStorageAws s3BucketManager;

	@Autowired
	FalsehoodUserService userService;
	
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
	
	public List<Falsehood> getFalsehoodsByAuthor(PublicFigure author)
	{
		return fRepo.getFalsehoodsByPublicFigure(author);
	}
	
	public Falsehood insertNewFalsehood(Falsehood f)
	{
		f.setId(null);
		
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

	public String addVerdict(BigInteger id, boolean approve, String comment, FalsehoodUser user, HttpServletRequest ip)
	{
		if(user.getCredit() < MIN_CREDIT_APPROVE_REJECT)
			return "Not Enough Credit";

		if(!fRepo.existsById(id))
			return "Falsehood Doesn't exist";

		Falsehood f = fRepo.getOne(id);
		if(f.getStatus() != Falsehood.SUBMITTED)
			return "Cannot cast Verdict on established Falsehood! File an Appeal to update the status";

		JSONObject verdictJson = null;
		VerdictListObj verdicts = new VerdictListObj();
		try {
			verdictJson = s3BucketManager.getJSONObj(f.getContentId());
			verdicts.initializeFromJson(verdictJson);

			List<VerdictObj> verdictList = verdicts.getVerdicts();

			for(int rust = 0; rust < verdictList.size(); rust++)
			{
				if(verdictList.get(rust).getUserId() == user.getUserId())
				{
					verdictList.remove(rust);
					break;
				}
			}
			verdicts.setVerdicts(verdictList);
		} catch(Exception e)
		{

		}

		verdicts.setApproversAvailable(userService.getUserCountAboveCredibility(MIN_CREDIT_APPROVE_REJECT));

		VerdictObj newVerdict = new VerdictObj(approve, user.getUserId(),
				new Date(Calendar.getInstance().getTime().getTime()), comment, null);

		newVerdict.setIpAddress(ip);

		List<VerdictObj> verdictList = verdicts.getVerdicts();
		verdictList.add(newVerdict);
		verdicts.setVerdicts(verdictList);

		verdictJson = verdicts.toJsonObject();

		if(!"Success".equals(s3BucketManager.addJsonFile(f.getContentId(), verdictJson)))
		{
			return "failed to Write Verdict to storage!";
		}

		if(verdicts.isApproved())
		{
			f.setStatus(Falsehood.VERIFIED);
			fRepo.save(f);
		}
		else if(verdicts.isRejected())
		{
			f.setStatus(Falsehood.REJECTED);
			fRepo.save(f);
		}


		return "";
	}
	
	public boolean appendEntryToStorage(String contents, Falsehood f)
	{
		return false;
	}
}
