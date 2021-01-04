package com.trecapps.false_hood.falsehoods;

import java.io.IOException;
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
	
	
	public List<Falsehood> getConfirmedFalsehoodsBySearchFeatures(SearchFalsehood s)
	{
		MediaOutlet mo = s.getOutlet();
		PublicFigure pf = s.getAuthor();
		
		Date before = s.getTo();
		Date after = s.getFrom();
		
		if(before == null && after != null)
			before = new Date(Calendar.getInstance().getTime().getTime());
		
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
						return fRepo.getConfirmedFalsehoodsBetweenAndBySeverity(p, after, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBetweenAndByMinSeverity(p, after, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverity(p, after, before, maxSev.GetValue());
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
						return fRepo.getConfirmedFalsehoodsBeforeAndBySeverity(p, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBeforeAndByMinSeverity(p, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverity(p, before, maxSev.GetValue());
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
						return fRepo.getConfirmedFalsehoodsBySeverity(p, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getConfirmedFalsehoodByMinSeverity(p, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodByMaxSeverity(p, maxSev.GetValue());
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
						return fRepo.getConfirmedFalsehoodsBetweenAndBySeverityPublicFigure(p, pf, after, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityPublicFigure(p, pf, after, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityPublicFigure(p, pf, after, before, maxSev.GetValue());
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
						return fRepo.getConfirmedFalsehoodsBeforeAndBySeverityPublicFigure(p, pf, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityPublicFigure(p, pf, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityPublicFigure(p, pf, before, maxSev.GetValue());
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
						return fRepo.getConfirmedFalsehoodsByPublicOfficialAndSeverity(p, pf, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getConfirmedFalsehoodsByPublicOfficialAndMinSeverity(p, pf, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsByPublicOfficialAndMaxSeverity(p, pf, maxSev.GetValue());
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
						return fRepo.getConfirmedFalsehoodsBetweenAndBySeverityOutlet(p, mo, after, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOutlet(p, mo, after, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOutlet(p, mo, after, before, maxSev.GetValue());
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
						return fRepo.getConfirmedFalsehoodsBeforeAndBySeverityOutlet(p, mo, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOutlet(p, mo, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOutlet(p, mo, before, maxSev.GetValue());
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
						return fRepo.getConfirmedFalsehoodsBetweenAndBySeverityOutletPublicFigure(p, pf, mo, after, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOutletPublicFigure(p, pf, mo, after, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOutletPublicFigure(p, pf, mo, after, before, maxSev.GetValue());
					}
					else
					{
						return fRepo.getConfirmedFalsehoodsBetweenAndByOutletPublicFigure(p, pf, mo, after, before);
					}
				}
				else if(before != null) // We only have before
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBeforeAndBySeverityOutletPublicFigure(p, pf, mo, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOutletPublicFigure(p, pf, mo, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOutletPublicFigure(p, pf, mo, before, maxSev.GetValue());
					}
					else
					{
						return fRepo.getConfirmedFalsehoodsBeforeAndByOutletPublicFigure(p, pf, mo, before);
					}
				}
				else // No Dates
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsByPublicOfficialOutletAndSeverity(p, pf, mo, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getConfirmedFalsehoodsByPublicOfficialOutletAndMinSeverity(p, pf, mo, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getConfirmedFalsehoodsByPublicOfficialOutletAndMaxSeverity(p, pf, mo, maxSev.GetValue());
					}
					else
					{
						return fRepo.getConfirmedFalsehoodsByPublicOfficialAndOutlet(p, pf, mo);
					}
				}
			}
		}
		
	}
	
	public List<Falsehood> getRejectedFalsehoodsBySearchFeatures(SearchFalsehood s)
	{
		MediaOutlet mo = s.getOutlet();
		PublicFigure pf = s.getAuthor();
		
		Date before = s.getTo();
		Date after = s.getFrom();
		
		if(before == null && after != null)
			before = new Date(Calendar.getInstance().getTime().getTime());
		
		
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
						return fRepo.getRejectedFalsehoodsBetweenAndBySeverity(p, after, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getRejectedFalsehoodsBetweenAndByMinSeverity(p, after, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBetweenAndByMaxSeverity(p, after, before, maxSev.GetValue());
					}
					else
					{
						return fRepo.getRejectedFalsehoodsBetween(p, after, before);
					}
				}
				else if(before != null)
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBeforeAndBySeverity(p, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getRejectedFalsehoodsBeforeAndByMinSeverity(p, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBeforeAndByMaxSeverity(p, before, maxSev.GetValue());
					}
					else
					{
						return fRepo.getRejectedFalsehoodsBefore(p, before);
					}
				}
				else
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBySeverity(p, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getRejectedFalsehoodByMinSeverity(p, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getRejectedFalsehoodByMaxSeverity(p, maxSev.GetValue());
					}
					else
					{
						return fRepo.getRejectedFalsehoods(p);
					}
				}
			}
			else // We have no Media Outlet but we do have a public figure
			{
				if(before != null && after != null)
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBetweenAndBySeverityPublicFigure(p, pf, after, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getRejectedFalsehoodsBetweenAndByMinSeverityPublicFigure(p, pf, after, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityPublicFigure(p, pf, after, before, maxSev.GetValue());
					}
					else
					{
						return fRepo.getRejectedFalsehoodsBetweenAndByPublicOfficial(p, pf, after, before);
					}
				}
				else if(before != null)
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBeforeAndBySeverityPublicFigure(p, pf, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getRejectedFalsehoodsBeforeAndByMinSeverityPublicFigure(p, pf, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityPublicFigure(p, pf, before, maxSev.GetValue());
					}
					else
					{
						return fRepo.getRejectedFalsehoodsBeforeAndByPublicOfficial(p, pf, before);
					}
				}
				else
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsByPublicOfficialAndSeverity(p, pf, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getRejectedFalsehoodsByPublicOfficialAndMinSeverity(p, pf, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsByPublicOfficialAndMaxSeverity(p, pf, maxSev.GetValue());
					}
					else
					{
						return fRepo.getRejectedFalsehoodsByPublicOfficial(p, pf);
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
						return fRepo.getRejectedFalsehoodsBetweenAndBySeverityOutlet(p, mo, after, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOutlet(p, mo, after, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOutlet(p, mo, after, before, maxSev.GetValue());
					}
					else
					{
						return fRepo.getRejectedFalsehoodsBetweenAndByOutlet(p, mo, after, before);
					}
				}
				else if(before != null) // Before
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBeforeAndBySeverityOutlet(p, mo, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOutlet(p, mo, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOutlet(p, mo, before, maxSev.GetValue());
					}
					else
					{
						return fRepo.getRejectedFalsehoodsBeforeAndByOutlet(p, mo, before);
					}
				}
				else // No time
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsByOutletAndSeverity(p, mo, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getRejectedFalsehoodsByOutletAndMinSeverity(p, mo, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsByOutletAndMaxSeverity(p, mo, maxSev.GetValue());
					}
					else
					{
						return fRepo.getRejectedFalsehoodsByOutlet(p, mo);
					}
				}
			}
			else // We have a media Outlet and a public figure
			{
				if(before != null && after != null) // We have a "between"
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBetweenAndBySeverityOutletPublicFigure(p, pf, mo, after, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOutletPublicFigure(p, pf, mo, after, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOutletPublicFigure(p, pf, mo, after, before, maxSev.GetValue());
					}
					else
					{
						return fRepo.getRejectedFalsehoodsBetweenAndByOutletPublicFigure(p, pf, mo, after, before);
					}
				}
				else if(before != null) // We only have before
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBeforeAndBySeverityOutletPublicFigure(p, pf, mo, before, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOutletPublicFigure(p, pf, mo, before, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOutletPublicFigure(p, pf, mo, before, maxSev.GetValue());
					}
					else
					{
						return fRepo.getRejectedFalsehoodsBeforeAndByOutletPublicFigure(p, pf, mo, before);
					}
				}
				else // No Dates
				{
					if(minSev != null && maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsByPublicOfficialOutletAndSeverity(p, pf, mo, maxSev.GetValue(), minSev.GetValue());
					}
					else if(minSev != null)
					{
						return fRepo.getRejectedFalsehoodsByPublicOfficialOutletAndMinSeverity(p, pf, mo, minSev.GetValue());
					}
					else if(maxSev != null)
					{
						return fRepo.getRejectedFalsehoodsByPublicOfficialOutletAndMaxSeverity(p, pf, mo, maxSev.GetValue());
					}
					else
					{
						return fRepo.getRejectedFalsehoodsByPublicOfficialAndOutlet(p, pf, mo);
					}
				}
			}
		}
		
	}

	public List<Falsehood> getSubmittedFalsehoods(int size, int page)
	{
		return fRepo.getSubmittedFalsehood(PageRequest.of(page, size));
	}
	
	public FullFalsehood getFalsehoodById(BigInteger id)
	{
		if(!fRepo.existsById(id))
			return null;
		
		Falsehood obj = fRepo.getOne(id);
		String contents = "";
		try {
			contents = s3BucketManager.retrieveContents(obj.getContentId());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return new FullFalsehood(contents, obj, null);
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
