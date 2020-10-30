package com.trecapps.false_hood.publicFalsehoods;

import com.trecapps.false_hood.miscellanous.FalsehoodStorageHolder;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class PublicFalsehoodService {

    PublicFalsehoodRepo pFalsehoodRepo;

    FalsehoodStorageHolder s3BucketManager;

    @Autowired
    public PublicFalsehoodService(@Autowired FalsehoodStorageHolder s3BucketManager,
                                  @Autowired PublicFalsehoodRepo pFalsehoodRepo)
    {
        this.pFalsehoodRepo = pFalsehoodRepo;
        this.s3BucketManager = s3BucketManager;
    }

    public List<PublicFalsehood> getFalseHoodByMinimumSeverity(byte severity)
    {
        return pFalsehoodRepo.getFalsehoodsByMinimumSeverity(severity);
    }

    public List<PublicFalsehood> getFalseHoodByMaximumSeverity(byte severity)
    {
        return pFalsehoodRepo.getFalsehoodsByMaximumSeverity(severity);
    }

    public List<PublicFalsehood> getFalsehoodBySeverityRange(byte min, byte max)
    {
        return pFalsehoodRepo.getFalsehoodBySeverity(max, min);
    }

    public List<PublicFalsehood> getFalsehoodByDateRange(Date oldest)
    {
        return getFalsehoodByDateRange(oldest, new Date(Calendar.getInstance().getTime().getTime()));
    }

    public List<PublicFalsehood> getFalsehoodByDateRange(Date oldest, Date newest)
    {
        return pFalsehoodRepo.getFalsehoodsBetween(oldest, newest);
    }
    public List<PublicFalsehood> getFalsehoodsBefore(Date newest)
    {
        return pFalsehoodRepo.getFalsehoodsBefore(newest);
    }

    public PublicFalsehood getFalsehoodById(BigInteger id)
    {
        return pFalsehoodRepo.getOne(id);
    }

    public List<PublicFalsehood> getFalsehoodsByAuthor(PublicFigure author)
    {
        return pFalsehoodRepo.getFalsehoodsByPublicFigure(author);
    }

    public PublicFalsehood insertNewFalsehood(PublicFalsehood f)
    {
        f.setId(null);

        return pFalsehoodRepo.save(f);
    }

    public PublicFalsehood updateNewFalsehood(PublicFalsehood f)
    {
        return pFalsehoodRepo.save(f);
    }

    public boolean insertEntryToStorage(PublicFalsehood f, String contents)
    {
        String objectId = "publicFalsehood-" + f.getId();

        System.out.println("Object ID inserting is " + objectId);

        return "Success".equals(s3BucketManager.addNewFile(objectId, contents));

    }

    public List<PublicFalsehood> getFalsehoodsByRegion(Region r)
    {
        return pFalsehoodRepo.getFalsehoodsByRegion(r);
    }

    public List<PublicFalsehood> getFalsehoodsByInstitution(Institution i)
    {
        return pFalsehoodRepo.getFalsehoodsByInstitution(i);
    }

    public boolean appendEntryToStorage(String contents, PublicFalsehood f)
    {
        return false;
    }
}
