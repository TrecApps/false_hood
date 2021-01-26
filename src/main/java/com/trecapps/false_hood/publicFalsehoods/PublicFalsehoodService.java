package com.trecapps.false_hood.publicFalsehoods;

import com.trecapps.false_hood.json.EventObj;
import com.trecapps.false_hood.json.VerdictListObj;
import com.trecapps.false_hood.json.VerdictObj;
import com.trecapps.false_hood.miscellanous.FalsehoodStatus;
import com.trecapps.false_hood.miscellanous.FalsehoodStorageHolder;
import com.trecapps.false_hood.miscellanous.Severity;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Service
public class PublicFalsehoodService {

	public static final int MIN_CREDIT_APPROVE_REJECT = 60;
    PublicFalsehoodRepo pfRepo;

    FalsehoodStorageHolder s3BucketManager;

    FalsehoodUserService uServe;
    
    @Autowired
    public PublicFalsehoodService(@Autowired FalsehoodStorageHolder s3BucketManager,
                                  @Autowired PublicFalsehoodRepo pfRepo,
                                  @Autowired FalsehoodUserService uServe)
    {
        this.pfRepo = pfRepo;
        this.s3BucketManager = s3BucketManager;
        this.uServe = uServe;
    }
    
	public String addVerdict(BigInteger id, int approve, String comment, FalsehoodUser user, HttpServletRequest ip)
	{
		if(user.getCredit() < MIN_CREDIT_APPROVE_REJECT)
			return "Not Enough Credit";

		if(!pfRepo.existsById(id))
			return "Falsehood Doesn't exist";

		PublicFalsehood f = pfRepo.getOne(id);
		if(f.getStatus() != FalsehoodStatus.SUBMITTED.GetValue())
			return "Cannot cast Verdict on established Public Falsehood! File an Appeal to update the status";

		JSONObject verdictJson = null;
		VerdictListObj verdicts = new VerdictListObj();
		try {
			verdictJson = s3BucketManager.getJSONObj("publicFalsehood-" + f.getId());
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

		verdicts.setApproversAvailable(uServe.getUserCountAboveCredibility(MIN_CREDIT_APPROVE_REJECT));

		VerdictObj newVerdict = new VerdictObj(approve, user.getUserId(),
				new Date(Calendar.getInstance().getTime().getTime()), comment, null);

		newVerdict.setIpAddress(ip);

		List<VerdictObj> verdictList = verdicts.getVerdicts();
		verdictList.add(newVerdict);
		verdicts.setVerdicts(verdictList);

		verdictJson = verdicts.toJsonObject();

		if(!"Success".equals(s3BucketManager.addJsonFile("publicFalsehood-" + f.getId(), verdictJson)))
		{
			return "failed to Write Verdict to storage!";
		}

		if(verdicts.isApproved())
		{
			f.setStatus(FalsehoodStatus.VERIFIED.GetValue());
			pfRepo.save(f);
		}
		else if(verdicts.isRejected())
		{
			System.out.println("Rejecting Public Falsehood!");
			f.setStatus(FalsehoodStatus.REJECTED.GetValue());
			pfRepo.save(f);
			
			if(verdicts.shouldStrike())
			{
				// To-Do: Retrieve User that created the falsehood
			}
		}


		return "";
	}
    
	public List<PublicFalsehood> getSubmittedFalsehoods(int size, int page)
	{
		return pfRepo.getSubmittedFalsehoods(PageRequest.of(page, size));
	}
    
	public List<PublicFalsehood> searchByRegion(Region id, int size, int page)
	{
		return pfRepo.getConfirmedFalsehoodsByRegion(PageRequest.of(page, size), (byte) 20, id);
	}
	
	public List<PublicFalsehood> searchByInstitution(Institution id, int size, int page)
	{
		return pfRepo.getConfirmedFalsehoodsByInstitution(PageRequest.of(page, size), (byte) 20, id);
	}
	
    public List<PublicFalsehood> searchConfirmedFalsehoodsByAttribte(SearchPublicFalsehood search)
    {
    	
    	PublicFigure official = search.getOfficial();
    	Date begin = search.getFrom();
    	Date end = search.getTo();
		if(end == null && begin != null)
			end = new Date(Calendar.getInstance().getTime().getTime());
		
    	
    	Severity minSev = search.getMinimum();
    	Severity maxSev = search.getMaximum();
    	
    	byte offType = search.getOfficialType();
    	String terms = search.getTerms();
    	
    	Pageable p = PageRequest.of(search.getPage(), search.getNumberOfEntries() == 0 ? 1 : search.getNumberOfEntries());
    	
    	
    	if(begin != null && end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndBySeverity(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndBySeverity(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{

    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficial(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficial(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverity(p, offType, begin, end, minSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverity(p, terms, offType, begin, end, minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(p, offType, begin, end, minSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(p, terms, offType, begin, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverity(p, offType, begin, end, maxSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverity(p, terms, offType, begin, end, maxSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(p, offType, begin, end, maxSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(p, terms, offType, begin, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetween(p, offType, begin, end) : 
    					pfRepo.getConfirmedFalsehoodsBetween(p, terms, offType, begin, end);
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByOfficial(p, offType, begin, end, official) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndByOfficial(p, terms, offType, begin, end, official);
    			}
    		}
    	}
    	else if(end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndBySeverity(p, offType, end, maxSev.GetValue(), minSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndBySeverity(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficial(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficial(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverity(p, offType, end, minSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverity(p, terms, offType, end, minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficial(p, offType, end, minSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficial(p, terms, offType, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverity(p, offType, end, maxSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverity(p, terms, offType, end, maxSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficial(p, offType, end, maxSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficial(p, terms, offType, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBefore(p, offType, end) : 
    					pfRepo.getConfirmedFalsehoodsBefore(p, terms, offType, end);
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByOfficial(p, offType, end, official) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndByOfficial(p, terms, offType, end, official);
    			}
    		}
    	}
    	else
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBySeverity(p, offType, maxSev.GetValue(), minSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBySeverity(p, terms, offType, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBySeverityOfficial(p, offType, maxSev.GetValue(), minSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBySeverityOfficial(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMinSeverity(p, offType, minSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsByMinSeverity(p, terms, offType, minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMinSeverityOfficial(p, offType, minSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsByMinSeverityOfficial(p, terms, offType, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMaxSeverity(p, offType, maxSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsByMaxSeverity(p, terms, offType, maxSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficial(p, offType, maxSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficial(p, terms, offType, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoods(p, offType) : 
    					pfRepo.getConfirmedFalsehoods(p, terms, offType);
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsByOfficial(p, offType, official) : 
    					pfRepo.getConfirmedFalsehoodsByOfficial(p, terms, offType, official);    				
    			}
    		}
    	}
    }
    
    public List<PublicFalsehood> searchRejectedFalsehoodsByAttribte(SearchPublicFalsehood search)
    {
    	PublicFigure official = search.getOfficial();
    	Date begin = search.getFrom();
    	Date end = search.getTo();
		if(end == null && begin != null)
			end = new Date(Calendar.getInstance().getTime().getTime());
		
    	
    	Severity minSev = search.getMinimum();
    	Severity maxSev = search.getMaximum();
    	
    	byte offType = search.getOfficialType();
    	String terms = search.getTerms();
    	
    	Pageable p = PageRequest.of(search.getPage(), search.getNumberOfEntries() == 0 ? 1 : search.getNumberOfEntries());
    	
    	
    	if(begin != null && end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndBySeverity(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndBySeverity(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficial(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficial(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverity(p, offType, begin, end, minSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverity(p, terms, offType, begin, end, minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficial(p, offType, begin, end, minSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficial(p, terms, offType, begin, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverity(p, offType, begin, end, maxSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverity(p, terms, offType, begin, end, maxSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficial(p, offType, begin, end, maxSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficial(p, terms, offType, begin, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetween(p, offType, begin, end) : 
    					pfRepo.getRejectedFalsehoodsBetween(p, terms, offType, begin, end);
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByOfficial(p, offType, begin, end, official) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndByOfficial(p, terms, offType, begin, end, official);
    			}
    		}
    	}
    	else if(end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndBySeverity(p, offType, end, maxSev.GetValue(), minSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndBySeverity(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficial(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficial(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverity(p, offType, end, minSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverity(p, terms, offType, end, minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficial(p, offType, end, minSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficial(p, terms, offType, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverity(p, offType, end, maxSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverity(p, terms, offType, end, maxSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficial(p, offType, end, maxSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficial(p, terms, offType, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBefore(p, offType, end) : 
    					pfRepo.getRejectedFalsehoodsBefore(p, terms, offType, end);
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByOfficial(p, offType, end, official) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndByOfficial(p, terms, offType, end, official);
    			}
    		}
    	}
    	else
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBySeverity(p, offType, maxSev.GetValue(), minSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBySeverity(p, terms, offType, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBySeverityOfficial(p, offType, maxSev.GetValue(), minSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBySeverityOfficial(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsByMinSeverity(p, offType, minSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsByMinSeverity(p, terms, offType, minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsByMinSeverityOfficial(p, offType, minSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsByMinSeverityOfficial(p, terms, offType, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsByMaxSeverity(p, offType, maxSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsByMaxSeverity(p, terms, offType, maxSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsByMaxSeverityOfficial(p, offType, maxSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsByMaxSeverityOfficial(p, terms, offType, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoods(p, offType) : 
    					pfRepo.getRejectedFalsehoods(p, terms, offType);
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsByOfficial(p, offType, official) : 
    					pfRepo.getRejectedFalsehoodsByOfficial(p, terms, offType, official);
    				
    			}
    		}
    	}
    }
    

    public FullPublicFalsehood getFalsehoodById(BigInteger id)
    {
    	if(!pfRepo.existsById(id))
    		return null;
        PublicFalsehood obj = pfRepo.getOne(id);
        String contents = "";
		List<VerdictObj> verdicts = null;
		List<EventObj> events = null;
        try
        {
        	contents = s3BucketManager.retrieveContents("publicFalsehood-" + obj.getId());
			JSONObject jObj = s3BucketManager.getJSONObj("publicFalsehood-" + obj.getId());
			
			VerdictListObj verList = new VerdictListObj();
			verList.initializeFromJson(jObj);
			
			verdicts = verList.getVerdicts();
			events = verList.getEvents();
			
			for(VerdictObj v : verdicts)
			{
				v.setIpAddress((String)null);
			}
			for(EventObj e : events)
			{
				e.setIpAddress((String)null);
			}
        }
        catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return new FullPublicFalsehood(contents, obj, verdicts, events);
    }

    public List<PublicFalsehood> getFalsehoodsByAuthor(PublicFigure author)
    {
        return pfRepo.getFalsehoodsByPublicFigure(author);
    }

    public PublicFalsehood insertNewFalsehood(PublicFalsehood f)
    {
        f.setId(null);

        return pfRepo.save(f);
    }

    public PublicFalsehood updateNewFalsehood(PublicFalsehood f)
    {
        return pfRepo.save(f);
    }

    public boolean insertEntryToStorage(PublicFalsehood f, String contents,FalsehoodUser user,HttpServletRequest ip)
    {
        String objectId = "publicFalsehood-" + f.getId();
        
        VerdictListObj verdicts = new VerdictListObj();
		verdicts.setApproversAvailable(uServe.getUserCountAboveCredibility(MIN_CREDIT_APPROVE_REJECT));

		EventObj event = new EventObj(1, user.getUserId(),
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

		JSONObject insertJson = verdicts.toJsonObject();

		if(!"Success".equals(s3BucketManager.addJsonFile(objectId, insertJson)))
		{
			return false;
		}
        return "Success".equals(s3BucketManager.addNewFile(objectId, contents));

    }

    public boolean appendEntryToStorage(String contents, PublicFalsehood f)
    {
    	String objectId = "publicFalsehood-" + f.getId();
        return "Success".equals(s3BucketManager.appendFile(objectId, contents));
    }
}
