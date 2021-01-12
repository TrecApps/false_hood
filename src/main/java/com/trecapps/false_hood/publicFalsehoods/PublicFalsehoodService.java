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
    	
    	byte offType = search.getOfficialType();
    	String terms = search.getTerms();
    	
    	Pageable p = PageRequest.of(search.getPage(), search.getNumberOfEntries() == 0 ? 1 : search.getNumberOfEntries());
    	

		System.out.println("In Public Service Search, page generted is " + p);
    	
    	if(begin != null && end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityRegionAndInst(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), reg, inst) :
    						pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityRegionAndInst(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityRegion(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), reg) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityRegion(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityInstitution(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), inst) :
    						pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityInstitution(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndBySeverity(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue()) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndBySeverity(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegionAndInst(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegionAndInst(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegion(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegion(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialInstitution(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialInstitution(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficial(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficial(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityRegionAndInst(p, offType, begin, end, minSev.GetValue(), reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityRegionAndInst(p, terms, offType, begin, end, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityRegion(p, offType, begin, end, minSev.GetValue(), reg) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityRegion(p, terms, offType, begin, end, minSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityInstitution(p, offType, begin, end, minSev.GetValue(), inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityInstitution(p, terms, offType, begin, end, minSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverity(p, offType, begin, end, minSev.GetValue()) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverity(p, terms, offType, begin, end, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegionAndInst(p, offType, begin, end, minSev.GetValue(), official, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegionAndInst(p, terms, offType, begin, end, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegion(p, offType, begin, end, minSev.GetValue(), official, reg) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegion(p, terms, offType, begin, end, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialInstitution(p, offType, begin, end, minSev.GetValue(), official, inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialInstitution(p, terms, offType, begin, end, minSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(p, offType, begin, end, minSev.GetValue(), official) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(p, terms, offType, begin, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityRegionAndInst(p, offType, begin, end, maxSev.GetValue(), reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityRegionAndInst(p, terms, offType, begin, end, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityRegion(p, offType, begin, end, maxSev.GetValue(), reg) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityRegion(p, terms, offType, begin, end, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityInstitution(p, offType, begin, end, maxSev.GetValue(), inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityInstitution(p, terms, offType, begin, end, maxSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverity(p, offType, begin, end, maxSev.GetValue()) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverity(p, terms, offType, begin, end, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegionAndInst(p, offType, begin, end, maxSev.GetValue(), official, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegionAndInst(p, terms, offType, begin, end, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegion(p, offType, begin, end, maxSev.GetValue(), official, reg) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegion(p, terms, offType, begin, end, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialInstitution(p, offType, begin, end, maxSev.GetValue(), official, inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialInstitution(p, terms, offType, begin, end, maxSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(p, offType, begin, end, maxSev.GetValue(), official) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(p, terms, offType, begin, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByRegionAndInst(p, offType, begin, end, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByRegionAndInst(p, terms, offType, begin, end, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByRegion(p, offType, begin, end, reg) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByRegion(p, terms, offType, begin, end, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByInstitution(p, offType, begin, end, inst) :
    						pfRepo.getConfirmedFalsehoodsBetweenAndByInstitution(p, terms, offType, begin, end, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetween(p, offType, begin, end) : 
    						pfRepo.getConfirmedFalsehoodsBetween(p, terms, offType, begin, end);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialRegionAndInst(p, offType, begin, end, official, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialRegionAndInst(p, terms, offType, begin, end, official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialRegion(p, offType, begin, end, official, reg) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialRegion(p, terms, offType, begin, end, official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialInstitution(p, offType, begin, end, official, inst) : 
    						pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialInstitution(p, terms, offType, begin, end, official, inst);
    				else
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
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityRegionAndInst(p, offType, end, maxSev.GetValue(), minSev.GetValue(), reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityRegionAndInst(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityRegion(p, offType, end, maxSev.GetValue(), minSev.GetValue(), reg) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityRegion(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityInstitution(p, offType, end, maxSev.GetValue(), minSev.GetValue(), inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityInstitution(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndBySeverity(p, offType, end, maxSev.GetValue(), minSev.GetValue()) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndBySeverity(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialRegionAndInst(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialRegionAndInst(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialRegion(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official, reg) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialRegion(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialInstitution(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialInstitution(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficial(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficial(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityRegionAndInst(p, offType, end, minSev.GetValue(), reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityRegionAndInst(p, terms, offType, end, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityRegion(p, offType, end, minSev.GetValue(), reg) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityRegion(p, terms, offType, end, minSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityInstitution(p, offType, end, minSev.GetValue(), inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityInstitution(p, terms, offType, end, minSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverity(p, offType, end, minSev.GetValue()) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverity(p, terms, offType, end, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialRegionAndInst(p, offType, end, minSev.GetValue(), official, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialRegionAndInst(p, terms, offType, end, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialRegion(p, offType, end, minSev.GetValue(), official, reg) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialRegion(p, terms, offType, end, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialInstitution(p, offType, end, minSev.GetValue(), official, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialInstitution(p, terms, offType, end, minSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficial(p, offType, end, minSev.GetValue(), official) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficial(p, terms, offType, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityRegionAndInst(p, offType, end, maxSev.GetValue(), reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityRegionAndInst(p, terms, offType, end, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityRegion(p, offType, end, maxSev.GetValue(), reg) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityRegion(p, terms, offType, end, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityInstitution(p, offType, end, maxSev.GetValue(), inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityInstitution(p, terms, offType, end, maxSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverity(p, offType, end, maxSev.GetValue()) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverity(p, terms, offType, end, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialRegionAndInst(p, offType, end, maxSev.GetValue(), official, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialRegionAndInst(p, terms, offType, end, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialRegion(p, offType, end, maxSev.GetValue(), official, reg) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialRegion(p, terms, offType, end, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialInstitution(p, offType, end, maxSev.GetValue(), official, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialInstitution(p, terms, offType, end, maxSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficial(p, offType, end, maxSev.GetValue(), official) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficial(p, terms, offType, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByRegionAndInst(p, offType, end, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByRegionAndInst(p, terms, offType, end, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByRegion(p, offType, end, reg) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByRegion(p, terms, offType, end, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByInstitution(p, offType, end, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByInstitution(p, terms, offType, end, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBefore(p, offType, end) : 
    						pfRepo.getConfirmedFalsehoodsBefore(p, terms, offType, end);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialRegionAndInst(p, offType, end, official, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialRegionAndInst(p, terms, offType, end, official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialRegion(p, offType, end, official, reg) : 
    						 pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialRegion(p, terms, offType, end, official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialInstitution(p, offType, end, official, inst) : 
    						pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialInstitution(p, terms, offType, end, official, inst);
    				else
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
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBySeverityRegionAndInst(p, offType, maxSev.GetValue(), minSev.GetValue(), reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBySeverityRegionAndInst(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBySeverityRegion(p, offType, maxSev.GetValue(), minSev.GetValue(), reg) : 
    						pfRepo.getConfirmedFalsehoodsBySeverityRegion(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBySeverityInstitution(p, offType, maxSev.GetValue(), minSev.GetValue(), inst) : 
    						pfRepo.getConfirmedFalsehoodsBySeverityInstitution(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBySeverity(p, offType, maxSev.GetValue(), minSev.GetValue()) : 
    						pfRepo.getConfirmedFalsehoodsBySeverity(p, terms, offType, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBySeverityOfficialRegionAndInst(p, offType, maxSev.GetValue(), minSev.GetValue(), official, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsBySeverityOfficialRegionAndInst(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBySeverityOfficialRegion(p, offType, maxSev.GetValue(), minSev.GetValue(), official, reg) : 
    						pfRepo.getConfirmedFalsehoodsBySeverityOfficialRegion(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBySeverityOfficialInstitution(p, offType, maxSev.GetValue(), minSev.GetValue(), official, inst) : 
    						pfRepo.getConfirmedFalsehoodsBySeverityOfficialInstitution(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsBySeverityOfficial(p, offType, maxSev.GetValue(), minSev.GetValue(), official) : 
    						pfRepo.getConfirmedFalsehoodsBySeverityOfficial(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMinSeverityRegionAndInst(p, offType, minSev.GetValue(), reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsByMinSeverityRegionAndInst(p, terms, offType, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMinSeverityRegion(p, offType, minSev.GetValue(), reg) : 
    						pfRepo.getConfirmedFalsehoodsByMinSeverityRegion(p, terms, offType, minSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMinSeverityInstitution(p, offType, minSev.GetValue(), inst) : 
    						pfRepo.getConfirmedFalsehoodsByMinSeverityInstitution(p, terms, offType, minSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMinSeverity(p, offType, minSev.GetValue()) : 
    						pfRepo.getConfirmedFalsehoodsByMinSeverity(p, terms, offType, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialRegionAndInst(p, offType, minSev.GetValue(), official, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialRegionAndInst(p, terms, offType, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialRegion(p, offType, minSev.GetValue(), official, reg) : 
    						pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialRegion(p, terms, offType, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialInstitution(p, offType, minSev.GetValue(), official, inst) :
    						pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialInstitution(p, terms, offType, minSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMinSeverityOfficial(p, offType, minSev.GetValue(), official) : 
    						pfRepo.getConfirmedFalsehoodsByMinSeverityOfficial(p, terms, offType, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMaxSeverityRegionAndInst(p, offType, maxSev.GetValue(), reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsByMaxSeverityRegionAndInst(p, terms, offType, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMaxSeverityRegion(p, offType, maxSev.GetValue(), reg) : 
    						pfRepo.getConfirmedFalsehoodsByMaxSeverityRegion(p, terms, offType, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMaxSeverityInstitution(p, offType, maxSev.GetValue(), inst) : 
    						pfRepo.getConfirmedFalsehoodsByMaxSeverityInstitution(p, terms, offType, maxSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMaxSeverity(p, offType, maxSev.GetValue()) : 
    						pfRepo.getConfirmedFalsehoodsByMaxSeverity(p, terms, offType, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialRegionAndInst(p, offType, maxSev.GetValue(), official, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialRegionAndInst(p, terms, offType, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialRegion(p, offType, maxSev.GetValue(), official, reg) : 
    						pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialRegion(p, terms, offType, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialInstitution(p, offType, maxSev.GetValue(), official, inst) : 
    						pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialInstitution(p, terms, offType, maxSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficial(p, offType, maxSev.GetValue(), official) : 
    						pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficial(p, terms, offType, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByRegionAndInst(p, offType, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsByRegionAndInst(p, terms, offType, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByRegion(p, offType, reg) : 
    						pfRepo.getConfirmedFalsehoodsByRegion(p, terms, offType, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByInstitution(p, offType, inst) : 
    						pfRepo.getConfirmedFalsehoodsByInstitution(p, terms, offType, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoods(p, offType) : 
    						pfRepo.getConfirmedFalsehoods(p, terms, offType);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByOfficialRegionAndInst(p, offType, official, reg, inst) : 
    						pfRepo.getConfirmedFalsehoodsByOfficialRegionAndInst(p, terms, offType, official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByOfficialRegion(p, offType, official, reg) : 
    						pfRepo.getConfirmedFalsehoodsByOfficialRegion(p, terms, offType, official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByOfficialInstitution(p, offType, official, inst) : 
    						pfRepo.getConfirmedFalsehoodsByOfficialInstitution(p, terms, offType, official, inst);
    				else
    					return (terms == null) ? pfRepo.getConfirmedFalsehoodsByOfficial(p, offType, official) : 
    						pfRepo.getConfirmedFalsehoodsByOfficial(p, terms, offType, official);
    				
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
    	
    	byte offType = search.getOfficialType();
    	String terms = search.getTerms();
    	
    	Pageable p = PageRequest.of(search.getPage(), search.getNumberOfEntries() == 0 ? 1 : search.getNumberOfEntries());
    	

		System.out.println("In Public Service Search, page generted is " + p);
    	
    	if(begin != null && end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndBySeverityRegionAndInst(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), reg, inst) :
    						pfRepo.getRejectedFalsehoodsBetweenAndBySeverityRegionAndInst(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndBySeverityRegion(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), reg) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndBySeverityRegion(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndBySeverityInstitution(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), inst) :
    						pfRepo.getRejectedFalsehoodsBetweenAndBySeverityInstitution(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndBySeverity(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue()) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndBySeverity(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficialRegionAndInst(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficialRegionAndInst(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficialRegion(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficialRegion(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficialInstitution(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficialInstitution(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficial(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficial(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityRegionAndInst(p, offType, begin, end, minSev.GetValue(), reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityRegionAndInst(p, terms, offType, begin, end, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityRegion(p, offType, begin, end, minSev.GetValue(), reg) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityRegion(p, terms, offType, begin, end, minSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityInstitution(p, offType, begin, end, minSev.GetValue(), inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityInstitution(p, terms, offType, begin, end, minSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverity(p, offType, begin, end, minSev.GetValue()) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverity(p, terms, offType, begin, end, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficialRegionAndInst(p, offType, begin, end, minSev.GetValue(), official, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficialRegionAndInst(p, terms, offType, begin, end, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficialRegion(p, offType, begin, end, minSev.GetValue(), official, reg) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficialRegion(p, terms, offType, begin, end, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficialInstitution(p, offType, begin, end, minSev.GetValue(), official, inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficialInstitution(p, terms, offType, begin, end, minSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficial(p, offType, begin, end, minSev.GetValue(), official) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficial(p, terms, offType, begin, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityRegionAndInst(p, offType, begin, end, maxSev.GetValue(), reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityRegionAndInst(p, terms, offType, begin, end, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityRegion(p, offType, begin, end, maxSev.GetValue(), reg) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityRegion(p, terms, offType, begin, end, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityInstitution(p, offType, begin, end, maxSev.GetValue(), inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityInstitution(p, terms, offType, begin, end, maxSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverity(p, offType, begin, end, maxSev.GetValue()) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverity(p, terms, offType, begin, end, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialRegionAndInst(p, offType, begin, end, maxSev.GetValue(), official, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialRegionAndInst(p, terms, offType, begin, end, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialRegion(p, offType, begin, end, maxSev.GetValue(), official, reg) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialRegion(p, terms, offType, begin, end, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialInstitution(p, offType, begin, end, maxSev.GetValue(), official, inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialInstitution(p, terms, offType, begin, end, maxSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficial(p, offType, begin, end, maxSev.GetValue(), official) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficial(p, terms, offType, begin, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByRegionAndInst(p, offType, begin, end, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByRegionAndInst(p, terms, offType, begin, end, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByRegion(p, offType, begin, end, reg) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByRegion(p, terms, offType, begin, end, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByInstitution(p, offType, begin, end, inst) :
    						pfRepo.getRejectedFalsehoodsBetweenAndByInstitution(p, terms, offType, begin, end, inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetween(p, offType, begin, end) : 
    						pfRepo.getRejectedFalsehoodsBetween(p, terms, offType, begin, end);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByOfficialRegionAndInst(p, offType, begin, end, official, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByOfficialRegionAndInst(p, terms, offType, begin, end, official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByOfficialRegion(p, offType, begin, end, official, reg) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByOfficialRegion(p, terms, offType, begin, end, official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByOfficialInstitution(p, offType, begin, end, official, inst) : 
    						pfRepo.getRejectedFalsehoodsBetweenAndByOfficialInstitution(p, terms, offType, begin, end, official, inst);
    				else
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
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndBySeverityRegionAndInst(p, offType, end, maxSev.GetValue(), minSev.GetValue(), reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndBySeverityRegionAndInst(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndBySeverityRegion(p, offType, end, maxSev.GetValue(), minSev.GetValue(), reg) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndBySeverityRegion(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndBySeverityInstitution(p, offType, end, maxSev.GetValue(), minSev.GetValue(), inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndBySeverityInstitution(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndBySeverity(p, offType, end, maxSev.GetValue(), minSev.GetValue()) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndBySeverity(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficialRegionAndInst(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficialRegionAndInst(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficialRegion(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official, reg) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficialRegion(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficialInstitution(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficialInstitution(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficial(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficial(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityRegionAndInst(p, offType, end, minSev.GetValue(), reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityRegionAndInst(p, terms, offType, end, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityRegion(p, offType, end, minSev.GetValue(), reg) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityRegion(p, terms, offType, end, minSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityInstitution(p, offType, end, minSev.GetValue(), inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityInstitution(p, terms, offType, end, minSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverity(p, offType, end, minSev.GetValue()) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverity(p, terms, offType, end, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficialRegionAndInst(p, offType, end, minSev.GetValue(), official, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficialRegionAndInst(p, terms, offType, end, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficialRegion(p, offType, end, minSev.GetValue(), official, reg) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficialRegion(p, terms, offType, end, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficialInstitution(p, offType, end, minSev.GetValue(), official, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficialInstitution(p, terms, offType, end, minSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficial(p, offType, end, minSev.GetValue(), official) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficial(p, terms, offType, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityRegionAndInst(p, offType, end, maxSev.GetValue(), reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityRegionAndInst(p, terms, offType, end, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityRegion(p, offType, end, maxSev.GetValue(), reg) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityRegion(p, terms, offType, end, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityInstitution(p, offType, end, maxSev.GetValue(), inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityInstitution(p, terms, offType, end, maxSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverity(p, offType, end, maxSev.GetValue()) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverity(p, terms, offType, end, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialRegionAndInst(p, offType, end, maxSev.GetValue(), official, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialRegionAndInst(p, terms, offType, end, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialRegion(p, offType, end, maxSev.GetValue(), official, reg) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialRegion(p, terms, offType, end, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialInstitution(p, offType, end, maxSev.GetValue(), official, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialInstitution(p, terms, offType, end, maxSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficial(p, offType, end, maxSev.GetValue(), official) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficial(p, terms, offType, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByRegionAndInst(p, offType, end, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByRegionAndInst(p, terms, offType, end, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByRegion(p, offType, end, reg) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByRegion(p, terms, offType, end, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByInstitution(p, offType, end, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByInstitution(p, terms, offType, end, inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBefore(p, offType, end) : 
    						pfRepo.getRejectedFalsehoodsBefore(p, terms, offType, end);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByOfficialRegionAndInst(p, offType, end, official, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByOfficialRegionAndInst(p, terms, offType, end, official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByOfficialRegion(p, offType, end, official, reg) : 
    						 pfRepo.getRejectedFalsehoodsBeforeAndByOfficialRegion(p, terms, offType, end, official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByOfficialInstitution(p, offType, end, official, inst) : 
    						pfRepo.getRejectedFalsehoodsBeforeAndByOfficialInstitution(p, terms, offType, end, official, inst);
    				else
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
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBySeverityRegionAndInst(p, offType, maxSev.GetValue(), minSev.GetValue(), reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBySeverityRegionAndInst(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBySeverityRegion(p, offType, maxSev.GetValue(), minSev.GetValue(), reg) : 
    						pfRepo.getRejectedFalsehoodsBySeverityRegion(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBySeverityInstitution(p, offType, maxSev.GetValue(), minSev.GetValue(), inst) : 
    						pfRepo.getRejectedFalsehoodsBySeverityInstitution(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBySeverity(p, offType, maxSev.GetValue(), minSev.GetValue()) : 
    						pfRepo.getRejectedFalsehoodsBySeverity(p, terms, offType, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBySeverityOfficialRegionAndInst(p, offType, maxSev.GetValue(), minSev.GetValue(), official, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsBySeverityOfficialRegionAndInst(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBySeverityOfficialRegion(p, offType, maxSev.GetValue(), minSev.GetValue(), official, reg) : 
    						pfRepo.getRejectedFalsehoodsBySeverityOfficialRegion(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBySeverityOfficialInstitution(p, offType, maxSev.GetValue(), minSev.GetValue(), official, inst) : 
    						pfRepo.getRejectedFalsehoodsBySeverityOfficialInstitution(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsBySeverityOfficial(p, offType, maxSev.GetValue(), minSev.GetValue(), official) : 
    						pfRepo.getRejectedFalsehoodsBySeverityOfficial(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMinSeverityRegionAndInst(p, offType, minSev.GetValue(), reg, inst) : 
    						pfRepo.getRejectedFalsehoodsByMinSeverityRegionAndInst(p, terms, offType, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMinSeverityRegion(p, offType, minSev.GetValue(), reg) : 
    						pfRepo.getRejectedFalsehoodsByMinSeverityRegion(p, terms, offType, minSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMinSeverityInstitution(p, offType, minSev.GetValue(), inst) : 
    						pfRepo.getRejectedFalsehoodsByMinSeverityInstitution(p, terms, offType, minSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMinSeverity(p, offType, minSev.GetValue()) : 
    						pfRepo.getRejectedFalsehoodsByMinSeverity(p, terms, offType, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMinSeverityOfficialRegionAndInst(p, offType, minSev.GetValue(), official, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsByMinSeverityOfficialRegionAndInst(p, terms, offType, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMinSeverityOfficialRegion(p, offType, minSev.GetValue(), official, reg) : 
    						pfRepo.getRejectedFalsehoodsByMinSeverityOfficialRegion(p, terms, offType, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMinSeverityOfficialInstitution(p, offType, minSev.GetValue(), official, inst) :
    						pfRepo.getRejectedFalsehoodsByMinSeverityOfficialInstitution(p, terms, offType, minSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMinSeverityOfficial(p, offType, minSev.GetValue(), official) : 
    						pfRepo.getRejectedFalsehoodsByMinSeverityOfficial(p, terms, offType, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMaxSeverityRegionAndInst(p, offType, maxSev.GetValue(), reg, inst) : 
    						pfRepo.getRejectedFalsehoodsByMaxSeverityRegionAndInst(p, terms, offType, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMaxSeverityRegion(p, offType, maxSev.GetValue(), reg) : 
    						pfRepo.getRejectedFalsehoodsByMaxSeverityRegion(p, terms, offType, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMaxSeverityInstitution(p, offType, maxSev.GetValue(), inst) : 
    						pfRepo.getRejectedFalsehoodsByMaxSeverityInstitution(p, terms, offType, maxSev.GetValue(), inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMaxSeverity(p, offType, maxSev.GetValue()) : 
    						pfRepo.getRejectedFalsehoodsByMaxSeverity(p, terms, offType, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMaxSeverityOfficialRegionAndInst(p, offType, maxSev.GetValue(), official, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsByMaxSeverityOfficialRegionAndInst(p, terms, offType, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMaxSeverityOfficialRegion(p, offType, maxSev.GetValue(), official, reg) : 
    						pfRepo.getRejectedFalsehoodsByMaxSeverityOfficialRegion(p, terms, offType, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMaxSeverityOfficialInstitution(p, offType, maxSev.GetValue(), official, inst) : 
    						pfRepo.getRejectedFalsehoodsByMaxSeverityOfficialInstitution(p, terms, offType, maxSev.GetValue(), official, inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByMaxSeverityOfficial(p, offType, maxSev.GetValue(), official) : 
    						pfRepo.getRejectedFalsehoodsByMaxSeverityOfficial(p, terms, offType, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByRegionAndInst(p, offType, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsByRegionAndInst(p, terms, offType, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByRegion(p, offType, reg) : 
    						pfRepo.getRejectedFalsehoodsByRegion(p, terms, offType, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByInstitution(p, offType, inst) : 
    						pfRepo.getRejectedFalsehoodsByInstitution(p, terms, offType, inst);
    				else
    					return (terms == null) ? pfRepo.getRejectedFalsehoods(p, offType) : 
    						pfRepo.getRejectedFalsehoods(p, terms, offType);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByOfficialRegionAndInst(p, offType, official, reg, inst) : 
    						pfRepo.getRejectedFalsehoodsByOfficialRegionAndInst(p, terms, offType, official, reg, inst);
    				else if(reg != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByOfficialRegion(p, offType, official, reg) : 
    						pfRepo.getRejectedFalsehoodsByOfficialRegion(p, terms, offType, official, reg);
    				else if(inst != null)
    					return (terms == null) ? pfRepo.getRejectedFalsehoodsByOfficialInstitution(p, offType, official, inst) : 
    						pfRepo.getRejectedFalsehoodsByOfficialInstitution(p, terms, offType, official, inst);
    				else
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
