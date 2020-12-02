package com.trecapps.false_hood.falsehoods;

import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.trecapps.false_hood.json.EventObj;
import com.trecapps.false_hood.json.VerdictListObj;
import com.trecapps.false_hood.json.VerdictObj;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.miscellanous.FalsehoodStatus;
import com.trecapps.false_hood.miscellanous.FalsehoodStorageHolder;
import com.trecapps.false_hood.miscellanous.Severity;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehood;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehoodRepo;

import javax.servlet.http.HttpServletRequest;

@Service
public class FalsehoodService {
	public static final int MIN_CREDIT_APPROVE_REJECT = 60;

	FalsehoodRepo fRepo;

	FalsehoodStorageHolder s3BucketManager;

	FalsehoodUserService userService;

	@Autowired
	public FalsehoodService(@Autowired FalsehoodRepo fRepo,
							@Autowired FalsehoodStorageHolder s3BucketManager,
							@Autowired FalsehoodUserService userService)
	{
		this.s3BucketManager = s3BucketManager;
		this.fRepo = fRepo;
		this.userService = userService;
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
	
	public List<Falsehood> getConfirmedFalsehoodsBySearchFeatures(SearchFalsehood s)
	{
		MediaOutlet mo = s.getOutlet();
		PublicFigure pf = s.getAuthor();
		
		Date before = s.getTo();
		Date after = s.getFrom();
		
		Severity minSev = s.getMinimum();
		Severity maxSev = s.getMaximum();
		
		Pageable p = PageRequest.of(s.getPage(), s.getNumberOfEntries() == 0 ? 1 : s.getNumberOfEntries());
		
		
		if(mo == null)
		{
			if(pf == null)
			{
				if(before != null && after != null)
				{
					if(minSev != null && maxSev != null)
					{
						
					}
					else if(minSev != null)
					{
						
					}
					else if(maxSev != null)
					{
						
					}
					else
					{
						return fRepo.getConfirmedFalsehoodsBetween(p, after, before);
					}
				}
				else if(before != null)
				{
					if(minSev != null && maxSev != null)
					{
						
					}
					else if(minSev != null)
					{
						
					}
					else if(maxSev != null)
					{
						
					}
					else
					{
						return fRepo.getConfirmedFalsehoodsBefore(p, before);
					}
				}
				else
				{
					if(minSev != null && maxSev != null)
					{
						
					}
					else if(minSev != null)
					{
						
					}
					else if(maxSev != null)
					{
						
					}
					else
					{
						return fRepo.getConfirmedFalsehoods(p);
					}
				}
			}
			else // We have no Media Outlet but we do have a public figure
			{
				if(before != null && after != null)
				{
					if(minSev != null && maxSev != null)
					{
						
					}
					else if(minSev != null)
					{
						
					}
					else if(maxSev != null)
					{
						
					}
					else
					{
						return fRepo.getConfirmedFalsehoodsBetweenAndByPublicOfficial(p, pf, after, before);
					}
				}
				else if(before != null)
				{
					if(minSev != null && maxSev != null)
					{
						
					}
					else if(minSev != null)
					{
						
					}
					else if(maxSev != null)
					{
						
					}
					else
					{
						return fRepo.getConfirmedFalsehoodsBeforeAndByPublicOfficial(p, pf, before);
					}
				}
				else
				{
					if(minSev != null && maxSev != null)
					{
						
					}
					else if(minSev != null)
					{
						
					}
					else if(maxSev != null)
					{
						
					}
					else
					{
						return fRepo.getConfirmedFalsehoodsByPublicOfficial(p, pf);
					}
				}
			}
		}
		else // We have a media Outlet
		{
			if(pf == null)
			{
				if(before != null && after != null) // Between
				{
					if(minSev != null && maxSev != null)
					{
						
					}
					else if(minSev != null)
					{
						
					}
					else if(maxSev != null)
					{
						
					}
					else
					{
						return fRepo.getConfirmedFalsehoodsBetweenAndByOutlet(p, mo, after, before);
					}
				}
				else if(before != null) // Before
				{
					if(minSev != null && maxSev != null)
					{
						
					}
					else if(minSev != null)
					{
						
					}
					else if(maxSev != null)
					{
						
					}
					else
					{
						return fRepo.getConfirmedFalsehoodsBeforeAndByOutlet(p, mo, before);
					}
				}
				else // No time
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsByOutletAndSeverity(p, mo, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getConfirmedFalsehoodsByOutletAndMinSeverity(p, mo, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsByOutletAndMaxSeverity(p, mo, maxSev.GetValue());
					}
					else
					{
						return fRepo.getConfirmedFalsehoodsByOutlet(p, mo);
					}
				}
			}
			else // We have a media Outlet and a public figure
			{
				if(before != null && after != null) // We have a "between"
				{
					if(minSev != null && maxSev != null)
					{
						
					}
					else if(minSev != null)
					{
						
					}
					else if(maxSev != null)
					{
						
					}
					else
					{
					
					}
				}
				else if(before != null) // We only have before
				{
					if(minSev != null && maxSev != null)
					{
						
					}
					else if(minSev != null)
					{
						
					}
					else if(maxSev != null)
					{
						
					}
					else
					{
						
					}
				}
				else // No Dates
				{
					if(minSev != null && maxSev != null)
					{
						fRepo.getConfirmedFalsehoodsByPublicOfficialOutletAndSeverity(p, pf, mo, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						fRepo.getConfirmedFalsehoodsByPublicOfficialOutletAndMinSeverity(p, pf, mo, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						fRepo.getConfirmedFalsehoodsByPublicOfficialOutletAndMaxSeverity(p, pf, mo, maxSev.GetValue());
					}
					else
					{
						fRepo.getConfirmedFalsehoodsByPublicOfficialAndOutlet(p, pf, mo);
					}
				}
			}
		}
		
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
		
		
		
		f = fRepo.save(f);
		
		f.setContentId(f.getId().toString() + "-" + f.getSource());
		
		return fRepo.save(f);
	}
	
	public Falsehood updateNewFalsehood(Falsehood f)
	{
		return fRepo.save(f);
	}
	
	public boolean insertEntryToStorage(String contents, Falsehood f,FalsehoodUser user,HttpServletRequest ip)
	{
		String objectId = f.getContentId();
		
		System.out.println("Object ID inserting is " + objectId);

		JSONObject insertJson = null;
		VerdictListObj verdicts = new VerdictListObj();
		try {
			insertJson = s3BucketManager.getJSONObj(f.getContentId());
			verdicts.initializeFromJson(insertJson);

			List<EventObj> verdictList = verdicts.getEvents();

			for(int rust = 0; rust < verdictList.size(); rust++)
			{
				if(verdictList.get(rust).getUserId() == user.getUserId())
				{
					verdictList.remove(rust);
					break;
				}
			}
			verdicts.setEvents(verdictList);
		} catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		verdicts.setApproversAvailable(userService.getUserCountAboveCredibility(MIN_CREDIT_APPROVE_REJECT));

		EventObj event = new EventObj(true, user.getUserId(),
				new Date(Calendar.getInstance().getTime().getTime()), null, null);

		event.setIpAddress(ip);

		List<EventObj> eventList = verdicts.getEvents();
		if(eventList == null)
		{
			eventList = new LinkedList<>();
			System.out.println("Detected Null EventList Somehow!");
		}
		eventList.add(event);
		verdicts.setEvents(eventList);

		insertJson = verdicts.toJsonObject();

		if(!"Success".equals(s3BucketManager.addJsonFile(f.getContentId(), insertJson)))
		{
			return false;
		}

		return "Success".equals(s3BucketManager.addNewFile(objectId, contents));
		
	}

	public String addVerdict(BigInteger id, boolean approve, String comment, FalsehoodUser user, HttpServletRequest ip)
	{
		if(user.getCredit() < MIN_CREDIT_APPROVE_REJECT)
			return "Not Enough Credit";

		if(!fRepo.existsById(id))
			return "Falsehood Doesn't exist";

		Falsehood f = fRepo.getOne(id);
		if(f.getStatus() != FalsehoodStatus.SUBMITTED.GetValue())
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
			f.setStatus(FalsehoodStatus.VERIFIED.GetValue());
			fRepo.save(f);
		}
		else if(verdicts.isRejected())
		{
			f.setStatus(FalsehoodStatus.REJECTED.GetValue());
			fRepo.save(f);
		}


		return "";
	}
	
	public boolean appendEntryToStorage(String contents, Falsehood f, FalsehoodUser user, HttpServletRequest ip)
	{
		String objectId = f.getContentId();

		System.out.println("Object ID inserting is " + objectId);

		JSONObject insertJson = null;
		VerdictListObj verdicts = new VerdictListObj();
		try {
			insertJson = s3BucketManager.getJSONObj(f.getContentId());
			verdicts.initializeFromJson(insertJson);

			List<EventObj> verdictList = verdicts.getEvents();

			for(int rust = 0; rust < verdictList.size(); rust++)
			{
				if(verdictList.get(rust).getUserId() == user.getUserId())
				{
					verdictList.remove(rust);
					break;
				}
			}
			verdicts.setEvents(verdictList);
		} catch(Exception e)
		{

		}

		verdicts.setApproversAvailable(userService.getUserCountAboveCredibility(MIN_CREDIT_APPROVE_REJECT));

		EventObj event = new EventObj(false, user.getUserId(),
				new Date(Calendar.getInstance().getTime().getTime()), null, null);

		event.setIpAddress(ip);

		List<EventObj> eventList = verdicts.getEvents();
		eventList.add(event);
		verdicts.setEvents(eventList);

		insertJson = verdicts.toJsonObject();

		if(!"Success".equals(s3BucketManager.addJsonFile(f.getContentId(), insertJson)))
		{
			return false;
		}

		return "Success".equals(s3BucketManager.appendFile(objectId, contents));
	}
}
