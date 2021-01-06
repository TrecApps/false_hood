package com.trecapps.false_hood.publicFalsehoods;

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
    	
    	Pageable p = PageRequest.of(search.getPage(), search.getNumberOfEntries() == 0 ? 1 : search.getNumberOfEntries());
    	

		System.out.println("In Public Service Search, page generted is " + p);
    	
    	if(begin != null && end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityRegionAndInst(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityRegion(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityInstitution(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverity(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegionAndInst(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegion(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialInstitution(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficial(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityRegionAndInst(p, offType, begin, end, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityRegion(p, offType, begin, end, minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityInstitution(p, offType, begin, end, minSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverity(p, offType, begin, end, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegionAndInst(p, offType, begin, end, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegion(p, offType, begin, end, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialInstitution(p, offType, begin, end, minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(p, offType, begin, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityRegionAndInst(p, offType, begin, end, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityRegion(p, offType, begin, end, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityInstitution(p, offType, begin, end, maxSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverity(p, offType, begin, end, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegionAndInst(p, offType, begin, end, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegion(p, offType, begin, end, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialInstitution(p, offType, begin, end, maxSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(p, offType, begin, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByRegionAndInst(p, offType, begin, end, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByRegion(p, offType, begin, end, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByInstitution(p, offType, begin, end, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetween(p, offType, begin, end);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialRegionAndInst(p, offType, begin, end, official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialRegion(p, offType, begin, end, official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialInstitution(p, offType, begin, end, official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByOfficial(p, offType, begin, end, official);
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
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityRegionAndInst(p, offType, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityRegion(p, offType, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityInstitution(p, offType, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverity(p, offType, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialRegionAndInst(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialRegion(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialInstitution(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficial(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityRegionAndInst(p, offType, end, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityRegion(p, offType, end, minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityInstitution(p, offType, end, minSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverity(p, offType, end, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialRegionAndInst(p, offType, end, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialRegion(p, offType, end, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialInstitution(p, offType, end, minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficial(p, offType, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityRegionAndInst(p, offType, end, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityRegion(p, offType, end, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityInstitution(p, offType, end, maxSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverity(p, offType, end, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialRegionAndInst(p, offType, end, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialRegion(p, offType, end, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialInstitution(p, offType, end, maxSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficial(p, offType, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByRegionAndInst(p, offType, end, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByRegion(p, offType, end, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByInstitution(p, offType, end, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBefore(p, offType, end);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialRegionAndInst(p, offType, end, official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialRegion(p, offType, end, official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialInstitution(p, offType, end, official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByOfficial(p, offType, end, official);
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
    					return pfRepo.getConfirmedFalsehoodsBySeverityRegionAndInst(p, offType, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBySeverityRegion(p, offType, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBySeverityInstitution(p, offType, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBySeverity(p, offType, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsBySeverityOfficialRegionAndInst(p, offType, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsBySeverityOfficialRegion(p, offType, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsBySeverityOfficialInstitution(p, offType, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsBySeverityOfficial(p, offType, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityRegionAndInst(p, offType, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityRegion(p, offType, minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityInstitution(p, offType, minSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsByMinSeverity(p, offType, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialRegionAndInst(p, offType, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialRegion(p, offType, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialInstitution(p, offType, minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityOfficial(p, offType, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityRegionAndInst(p, offType, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityRegion(p, offType, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityInstitution(p, offType, maxSev.GetValue(), inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverity(p, offType, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialRegionAndInst(p, offType, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialRegion(p, offType, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialInstitution(p, offType, maxSev.GetValue(), official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficial(p, offType, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsByRegionAndInst(p, offType, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsByRegion(p, offType, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsByInstitution(p, offType, inst);
    				else
    					return pfRepo.getConfirmedFalsehoods(p, offType);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getConfirmedFalsehoodsByOfficialRegionAndInst(p, offType, official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getConfirmedFalsehoodsByOfficialRegion(p, offType, official, reg);
    				else if(inst != null)
    					return pfRepo.getConfirmedFalsehoodsByOfficialInstitution(p, offType, official, inst);
    				else
    					return pfRepo.getConfirmedFalsehoodsByOfficial(p, offType, official);
    				
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
    	Pageable p = PageRequest.of(search.getPage(), search.getNumberOfEntries() == 0 ? 1 : search.getNumberOfEntries());
    	
    	if(begin != null && end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityRegionAndInst(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityRegion(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityInstitution(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverity(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficialRegionAndInst(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficialRegion(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficialInstitution(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficial(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityRegionAndInst(p, offType, begin, end, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityRegion(p, offType, begin, end, minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityInstitution(p, offType, begin, end, minSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverity(p, offType, begin, end, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficialRegionAndInst(p, offType, begin, end, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficialRegion(p, offType, begin, end, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficialInstitution(p, offType, begin, end, minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficial(p, offType, begin, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityRegionAndInst(p, offType, begin, end, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityRegion(p, offType, begin, end, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityInstitution(p, offType, begin, end, maxSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverity(p, offType, begin, end, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialRegionAndInst(p, offType, begin, end, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialRegion(p, offType, begin, end, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficialInstitution(p, offType, begin, end, maxSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficial(p, offType, begin, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByRegionAndInst(p, offType, begin, end, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByRegion(p, offType, begin, end, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByInstitution(p, offType, begin, end, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetween(p, offType, begin, end);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByOfficialRegionAndInst(p, offType, begin, end, official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByOfficialRegion(p, offType, begin, end, official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBetweenAndByOfficialInstitution(p, offType, begin, end, official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBetweenAndByOfficial(p, offType, begin, end, official);
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
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityRegionAndInst(p, offType, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityRegion(p, offType, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityInstitution(p, offType, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverity(p, offType, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficialRegionAndInst(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficialRegion(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficialInstitution(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficial(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityRegionAndInst(p, offType, end, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityRegion(p, offType, end, minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityInstitution(p, offType, end, minSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverity(p, offType, end, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficialRegionAndInst(p, offType, end, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficialRegion(p, offType, end, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficialInstitution(p, offType, end, minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficial(p, offType, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityRegionAndInst(p, offType, end, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityRegion(p, offType, end, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityInstitution(p, offType, end, maxSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverity(p, offType, end, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialRegionAndInst(p, offType, end, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialRegion(p, offType, end, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficialInstitution(p, offType, end, maxSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficial(p, offType, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByRegionAndInst(p, offType, end, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByRegion(p, offType, end, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByInstitution(p, offType, end, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBefore(p, offType, end);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByOfficialRegionAndInst(p, offType, end, official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByOfficialRegion(p, offType, end, official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBeforeAndByOfficialInstitution(p, offType, end, official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBeforeAndByOfficial(p, offType, end, official);
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
    					return pfRepo.getRejectedFalsehoodsBySeverityRegionAndInst(p, offType, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBySeverityRegion(p, offType, maxSev.GetValue(), minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBySeverityInstitution(p, offType, maxSev.GetValue(), minSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBySeverity(p, offType, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsBySeverityOfficialRegionAndInst(p, offType, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsBySeverityOfficialRegion(p, offType, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsBySeverityOfficialInstitution(p, offType, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsBySeverityOfficial(p, offType, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsByMinSeverityRegionAndInst(p, offType, minSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsByMinSeverityRegion(p, offType, minSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsByMinSeverityInstitution(p, offType, minSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsByMinSeverity(p, offType, minSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsByMinSeverityOfficialRegionAndInst(p, offType, minSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsByMinSeverityOfficialRegion(p, offType, minSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsByMinSeverityOfficialInstitution(p, offType, minSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsByMinSeverityOfficial(p, offType, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityRegionAndInst(p, offType, maxSev.GetValue(), reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityRegion(p, offType, maxSev.GetValue(), reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityInstitution(p, offType, maxSev.GetValue(), inst);
    				else
    					return pfRepo.getRejectedFalsehoodsByMaxSeverity(p, offType, maxSev.GetValue());
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityOfficialRegionAndInst(p, offType, maxSev.GetValue(), official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityOfficialRegion(p, offType, maxSev.GetValue(), official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityOfficialInstitution(p, offType, maxSev.GetValue(), official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsByMaxSeverityOfficial(p, offType, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsByRegionAndInst(p, offType, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsByRegion(p, offType, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsByInstitution(p, offType, inst);
    				else
    					return pfRepo.getRejectedFalsehoods(p, offType);
    			}
    			else
    			{
    				if(reg != null && inst != null)
    					return pfRepo.getRejectedFalsehoodsByOfficialRegionAndInst(p, offType, official, reg, inst);
    				else if(reg != null)
    					return pfRepo.getRejectedFalsehoodsByOfficialRegion(p, offType, official, reg);
    				else if(inst != null)
    					return pfRepo.getRejectedFalsehoodsByOfficialInstitution(p, offType, official, inst);
    				else
    					return pfRepo.getRejectedFalsehoodsByOfficial(p, offType, official);
    				
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
