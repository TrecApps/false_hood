package com.trecapps.false_hood.publicFalsehoods;

import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.FullFalsehood;
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
    
	public String addVerdict(BigInteger id, boolean approve, String comment, FalsehoodUser user, HttpServletRequest ip)
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
		}


		return "";
	}
    
	public List<PublicFalsehood> getSubmittedFalsehoods(int size, int page)
	{
		return pfRepo.getSubmittedFalsehoods(PageRequest.of(page, size));
	}
    
    public List<PublicFalsehood> searchConfirmedFalsehoodsByAttribte(SearchPublicFalsehood search)
    {
    	Institution inst = search.getInstitution();
    	Region reg = search.getRegion();
    	
    	PublicFigure official = search.getOfficial();
    	Date begin = search.getFrom();
    	Date end = search.getTo();
		if(end == null && begin != null)
			end = new Date(Calendar.getInstance().getTime().getTime());
		
    	
    	Severity minSev = search.getMinimum();
    	Severity maxSev = search.getMaximum();
    	
    	Pageable p = PageRequest.of(search.getPage(), search.getNumberOfEntries() == 0 ? 1 : search.getNumberOfEntries());
    	

		System.out.println("In Public Service Search, page generted is " + p);
    	
    	if(begin != null && end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityRegionAndInst(p, begin, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityRegion(p, begin, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityInstitution(p, begin, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverity(p, begin, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegionAndInst(p, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegion(p, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialInstitution(p, begin, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficial(p, begin, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityRegionAndInst(p, begin, end, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityRegion(p, begin, end, minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityInstitution(p, begin, end, minSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverity(p, begin, end, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegionAndInst(p, begin, end, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegion(p, begin, end, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialInstitution(p, begin, end, minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(p, begin, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityRegionAndInst(p, begin, end, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityRegion(p, begin, end, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityInstitution(p, begin, end, maxSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverity(p, begin, end, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegionAndInst(p, begin, end, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegion(p, begin, end, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialInstitution(p, begin, end, maxSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(p, begin, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByRegionAndInst(p, begin, end, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByRegion(p, begin, end, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByInstitution(p, begin, end, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetween(p, begin, end);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialRegionAndInst(p, begin, end, official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialRegion(p, begin, end, official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialInstitution(p, begin, end, official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByOfficial(p, begin, end, official);
    			}
    		}
    	}
    	else if(end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityRegionAndInst(p, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityRegion(p, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityInstitution(p, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverity(p, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialRegionAndInst(p, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialRegion(p, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialInstitution(p, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficial(p, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityRegionAndInst(p, end, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityRegion(p, end, minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityInstitution(p, end, minSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverity(p, end, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialRegionAndInst(p, end, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialRegion(p, end, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialInstitution(p, end, minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficial(p, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityRegionAndInst(p, end, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityRegion(p, end, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityInstitution(p, end, maxSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverity(p, end, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialRegionAndInst(p, end, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialRegion(p, end, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialInstitution(p, end, maxSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficial(p, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByRegionAndInst(p, end, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByRegion(p, end, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByInstitution(p, end, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBefore(p, end);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialRegionAndInst(p, end, official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialRegion(p, end, official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialInstitution(p, end, official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByOfficial(p, end, official);
    			}
    		}
    	}
    	else
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBySeverityRegionAndInst(p, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBySeverityRegion(p, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBySeverityInstitution(p, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBySeverity(p, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBySeverityOfficialRegionAndInst(p, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBySeverityOfficialRegion(p, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBySeverityOfficialInstitution(p, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBySeverityOfficial(p, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityRegionAndInst(p, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityRegion(p, minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityInstitution(p, minSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsByMinSeverity(p, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialRegionAndInst(p, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialRegion(p, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialInstitution(p, minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityOfficial(p, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityRegionAndInst(p, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityRegion(p, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityInstitution(p, maxSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverity(p, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialRegionAndInst(p, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialRegion(p, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialInstitution(p, maxSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficial(p, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsByRegionAndInst(p, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsByRegion(p, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsByInstitution(p, inst);
    				else
    					return pfRepo.getConfirmedFalsehoods(p);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsByOfficialRegionAndInst(p, official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsByOfficialRegion(p, official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsByOfficialInstitution(p, official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsByOfficial(p, official);
    				
    			}
    		}
    	}
    }
    
    public List<PublicFalsehood> searchRejectedFalsehoodsByAttribte(SearchPublicFalsehood search)
    {
    	Institution inst = search.getInstitution();
    	Region reg = search.getRegion();
    	
    	PublicFigure official = search.getOfficial();
    	Date begin = search.getFrom();
    	Date end = search.getTo();

		if(end == null && begin != null)
			end = new Date(Calendar.getInstance().getTime().getTime());
		
    	Severity minSev = search.getMinimum();
    	Severity maxSev = search.getMaximum();
    	
    	Pageable p = PageRequest.of(search.getPage(), search.getNumberOfEntries() == 0 ? 1 : search.getNumberOfEntries());
    	
    	if(begin != null && end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityRegionAndInst(p, begin, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityRegion(p, begin, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityInstitution(p, begin, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverity(p, begin, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficialRegionAndInst(p, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficialRegion(p, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficialInstitution(p, begin, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficial(p, begin, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityRegionAndInst(p, begin, end, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityRegion(p, begin, end, minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityInstitution(p, begin, end, minSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverity(p, begin, end, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficialRegionAndInst(p, begin, end, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficialRegion(p, begin, end, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficialInstitution(p, begin, end, minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficial(p, begin, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityRegionAndInst(p, begin, end, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityRegion(p, begin, end, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityInstitution(p, begin, end, maxSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverity(p, begin, end, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialRegionAndInst(p, begin, end, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialRegion(p, begin, end, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialInstitution(p, begin, end, maxSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficial(p, begin, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByRegionAndInst(p, begin, end, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByRegion(p, begin, end, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByInstitution(p, begin, end, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetween(p, begin, end);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByOfficialRegionAndInst(p, begin, end, official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByOfficialRegion(p, begin, end, official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByOfficialInstitution(p, begin, end, official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndByOfficial(p, begin, end, official);
    			}
    		}
    	}
    	else if(end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityRegionAndInst(p, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityRegion(p, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityInstitution(p, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverity(p, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficialRegionAndInst(p, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficialRegion(p, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficialInstitution(p, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficial(p, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityRegionAndInst(p, end, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityRegion(p, end, minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityInstitution(p, end, minSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverity(p, end, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficialRegionAndInst(p, end, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficialRegion(p, end, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficialInstitution(p, end, minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficial(p, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityRegionAndInst(p, end, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityRegion(p, end, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityInstitution(p, end, maxSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverity(p, end, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialRegionAndInst(p, end, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialRegion(p, end, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialInstitution(p, end, maxSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficial(p, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByRegionAndInst(p, end, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByRegion(p, end, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByInstitution(p, end, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBefore(p, end);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByOfficialRegionAndInst(p, end, official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByOfficialRegion(p, end, official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByOfficialInstitution(p, end, official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndByOfficial(p, end, official);
    			}
    		}
    	}
    	else
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBySeverityRegionAndInst(p, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBySeverityRegion(p, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBySeverityInstitution(p, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBySeverity(p, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBySeverityOfficialRegionAndInst(p, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBySeverityOfficialRegion(p, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBySeverityOfficialInstitution(p, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBySeverityOfficial(p, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsByMinSeverityRegionAndInst(p, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsByMinSeverityRegion(p, minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsByMinSeverityInstitution(p, minSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsByMinSeverity(p, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsByMinSeverityOfficialRegionAndInst(p, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsByMinSeverityOfficialRegion(p, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsByMinSeverityOfficialInstitution(p, minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsByMinSeverityOfficial(p, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityRegionAndInst(p, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityRegion(p, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityInstitution(p, maxSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsByMaxSeverity(p, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityOfficialRegionAndInst(p, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityOfficialRegion(p, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityOfficialInstitution(p, maxSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityOfficial(p, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsByRegionAndInst(p, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsByRegion(p, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsByInstitution(p, inst);
    				else
    					return pfRepo.getRejectedFalsehoods(p);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsByOfficialRegionAndInst(p, official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsByOfficialRegion(p, official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsByOfficialInstitution(p, official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsByOfficial(p, official);
    				
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
        try
        {
        	contents = s3BucketManager.retrieveContents("publicFalsehood-" + obj.getId());
        }
        catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return new FullPublicFalsehood(contents, obj, null);
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

    public boolean insertEntryToStorage(PublicFalsehood f, String contents)
    {
        String objectId = "publicFalsehood-" + f.getId();

        return "Success".equals(s3BucketManager.addNewFile(objectId, contents));

    }

    public boolean appendEntryToStorage(String contents, PublicFalsehood f)
    {
    	String objectId = "publicFalsehood-" + f.getId();
        return "Success".equals(s3BucketManager.appendFile(objectId, contents));
    }
}
