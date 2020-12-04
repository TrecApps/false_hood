package com.trecapps.false_hood.publicFalsehoods;

import com.trecapps.false_hood.miscellanous.FalsehoodStorageHolder;
import com.trecapps.false_hood.miscellanous.Severity;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class PublicFalsehoodService {

    PublicFalsehoodRepo pfRepo;

    FalsehoodStorageHolder s3BucketManager;

    @Autowired
    public PublicFalsehoodService(@Autowired FalsehoodStorageHolder s3BucketManager,
                                  @Autowired PublicFalsehoodRepo pfRepo)
    {
        this.pfRepo = pfRepo;
        this.s3BucketManager = s3BucketManager;
    }
    
    
    public List<PublicFalsehood> searchConfirmedFalsehoodsByAttribte(SearchPublicFalsehood search)
    {
    	Institution inst = search.getInstitution();
    	Region reg = search.getRegion();
    	
    	PublicFigure official = search.getOfficial();
    	Date begin = search.getFrom();
    	Date end = search.getTo();
    	
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
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityRegionAndInst(p, begin, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityRegion(p, begin, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityInstitution(p, begin, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverity(p, begin, end, maxSev.GetValue(), minSev.GetValue());
    				}
    			}
    			else
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegionAndInst(p, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegion(p, begin, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficialInstitution(p, begin, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficial(p, begin, end, maxSev.GetValue(), minSev.GetValue(), official);
    				}
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityRegionAndInst(p, begin, end, minSev.GetValue(), reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityRegion(p, begin, end, minSev.GetValue(), reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityInstitution(p, begin, end, minSev.GetValue(), inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverity(p, begin, end, minSev.GetValue());
    				}
    			}
    			else
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegionAndInst(p, begin, end, minSev.GetValue(), official, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegion(p, begin, end, minSev.GetValue(), official, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialInstitution(p, begin, end, minSev.GetValue(), official, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(p, begin, end, minSev.GetValue(), official);
    				}
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityRegionAndInst(p, begin, end, maxSev.GetValue(), reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityRegion(p, begin, end, maxSev.GetValue(), reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityInstitution(p, begin, end, maxSev.GetValue(), inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverity(p, begin, end, maxSev.GetValue());
    				}
    			}
    			else
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegionAndInst(p, begin, end, maxSev.GetValue(), official, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegion(p, begin, end, maxSev.GetValue(), official, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialInstitution(p, begin, end, maxSev.GetValue(), official, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(p, begin, end, maxSev.GetValue(), official);
    				}
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByRegionAndInst(p, begin, end, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByRegion(p, begin, end, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByInstitution(p, begin, end, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBetween(p, begin, end);
    				}
    			}
    			else
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialRegionAndInst(p, begin, end, official, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialRegion(p, begin, end, official, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByOfficialInstitution(p, begin, end, official, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBetweenAndByOfficial(p, begin, end, official);
    				}
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
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityRegionAndInst(p, end, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityRegion(p, end, maxSev.GetValue(), minSev.GetValue(), reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityInstitution(p, end, maxSev.GetValue(), minSev.GetValue(), inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverity(p, end, maxSev.GetValue(), minSev.GetValue());
    				}
    			}
    			else
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialRegionAndInst(p, end, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialRegion(p, end, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficialInstitution(p, end, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficial(p, end, maxSev.GetValue(), minSev.GetValue(), official);
    				}
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityRegionAndInst(p, end, minSev.GetValue(), reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityRegion(p, end, minSev.GetValue(), reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityInstitution(p, end, minSev.GetValue(), inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverity(p, end, minSev.GetValue());
    				}
    			}
    			else
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialRegionAndInst(p, end, minSev.GetValue(), official, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialRegion(p, end, minSev.GetValue(), official, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficialInstitution(p, end, minSev.GetValue(), official, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficial(p, end, minSev.GetValue(), official);
    				}
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityRegionAndInst(p, end, maxSev.GetValue(), reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityRegion(p, end, maxSev.GetValue(), reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityInstitution(p, end, maxSev.GetValue(), inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverity(p, end, maxSev.GetValue());
    				}
    			}
    			else
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialRegionAndInst(p, end, maxSev.GetValue(), official, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialRegion(p, end, maxSev.GetValue(), official, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficialInstitution(p, end, maxSev.GetValue(), official, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficial(p, end, maxSev.GetValue(), official);
    				}
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByRegionAndInst(p, end, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByRegion(p, end, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByInstitution(p, end, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBefore(p, end);
    				}
    			}
    			else
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialRegionAndInst(p, end, official, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialRegion(p, end, official, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByOfficialInstitution(p, end, official, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBeforeAndByOfficial(p, end, official);
    				}
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
    				{
    					return pfRepo.getConfirmedFalsehoodsBySeverityRegionAndInst(p, maxSev.GetValue(), minSev.GetValue(), reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBySeverityRegion(p, maxSev.GetValue(), minSev.GetValue(), reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBySeverityInstitution(p, maxSev.GetValue(), minSev.GetValue(), inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBySeverity(p, maxSev.GetValue(), minSev.GetValue());
    				}
    			}
    			else
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBySeverityOfficialRegionAndInst(p, maxSev.GetValue(), minSev.GetValue(), official, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBySeverityOfficialRegion(p, maxSev.GetValue(), minSev.GetValue(), official, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsBySeverityOfficialInstitution(p, maxSev.GetValue(), minSev.GetValue(), official, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBySeverityOfficial(p, maxSev.GetValue(), minSev.GetValue(), official);
    				}
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityRegionAndInst(p, minSev.GetValue(), reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityRegion(p, minSev.GetValue(), reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityInstitution(p, minSev.GetValue(), inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsByMinSeverity(p, minSev.GetValue());
    				}
    			}
    			else
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialRegionAndInst(p, minSev.GetValue(), official, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialRegion(p, minSev.GetValue(), official, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityOfficialInstitution(p, minSev.GetValue(), official, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsByMinSeverityOfficial(p, minSev.GetValue(), official);
    				}
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityRegionAndInst(p, maxSev.GetValue(), reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityRegion(p, maxSev.GetValue(), reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityInstitution(p, maxSev.GetValue(), inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverity(p, maxSev.GetValue());
    				}
    			}
    			else
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialRegionAndInst(p, maxSev.GetValue(), official, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialRegion(p, maxSev.GetValue(), official, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficialInstitution(p, maxSev.GetValue(), official, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficial(p, maxSev.GetValue(), official);
    				}
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByRegionAndInst(p, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByRegion(p, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByInstitution(p, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsBefore(p, end);
    				}
    			}
    			else
    			{
    				if(reg != null && inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByOfficialRegionAndInst(p, official, reg, inst);
    				}
    				else if(reg != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByOfficialRegion(p, official, reg);
    				}
    				else if(inst != null)
    				{
    					return pfRepo.getConfirmedFalsehoodsByOfficialInstitution(p, official, inst);
    				}
    				else
    				{
    					return pfRepo.getConfirmedFalsehoodsByOfficial(p, official);
    				}
    			}
    		}
    	}
    }
    

    public List<PublicFalsehood> getFalseHoodByMinimumSeverity(byte severity)
    {
        return pfRepo.getFalsehoodsByMinimumSeverity(severity);
    }

    public List<PublicFalsehood> getFalseHoodByMaximumSeverity(byte severity)
    {
        return pfRepo.getFalsehoodsByMaximumSeverity(severity);
    }

    public List<PublicFalsehood> getFalsehoodBySeverityRange(byte min, byte max)
    {
        return pfRepo.getFalsehoodBySeverity(max, min);
    }

    public List<PublicFalsehood> getFalsehoodByDateRange(Date oldest)
    {
        return getFalsehoodByDateRange(oldest, new Date(Calendar.getInstance().getTime().getTime()));
    }

    public List<PublicFalsehood> getFalsehoodByDateRange(Date oldest, Date newest)
    {
        return pfRepo.getFalsehoodsBetween(oldest, newest);
    }
    public List<PublicFalsehood> getFalsehoodsBefore(Date newest)
    {
        return pfRepo.getFalsehoodsBefore(newest);
    }

    public PublicFalsehood getFalsehoodById(BigInteger id)
    {
        return pfRepo.getOne(id);
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

    public List<PublicFalsehood> getFalsehoodsByRegion(Region r)
    {
        return pfRepo.getFalsehoodsByRegion(r);
    }

    public List<PublicFalsehood> getFalsehoodsByInstitution(Institution i)
    {
        return pfRepo.getFalsehoodsByInstitution(i);
    }

    public boolean appendEntryToStorage(String contents, PublicFalsehood f)
    {
    	String objectId = "publicFalsehood-" + f.getId();
        return "Success".equals(s3BucketManager.appendFile(objectId, contents));
    }
}
