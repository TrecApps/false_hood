package com.trecapps.false_hood.controllers;

import com.trecapps.false_hood.miscellanous.FalsehoodStatus;
import com.trecapps.false_hood.miscellanous.VerdictSubmission;
import com.trecapps.false_hood.publicFalsehoods.FullPublicFalsehood;
import com.trecapps.false_hood.publicFalsehoods.InstitutionEntry;
import com.trecapps.false_hood.publicFalsehoods.PublicAttributeService;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehood;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehoodService;
import com.trecapps.false_hood.publicFalsehoods.RegionEntry;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Update/PublicFalsehood")
public class AuthPublicFalsehoodController extends AuthenticationControllerBase
{
    PublicFalsehoodService service;

    
    PublicAttributeService attService;

    public static final int MIN_CREDIT_SUBMIT_NEW = 5;

    public static final int MIN_CREDIT_APPROVE_REJECT = 60;

	public static final int MIN_CREDIT_APPROVE_REJECT_RESOURCE = 200;
    
    public static final int MIN_CREDIT_ADD_RECOURSE = 35;

    @Autowired
    public AuthPublicFalsehoodController(@Autowired FalsehoodUserService userService,
                                         @Autowired PublicFalsehoodService service,
                                         @Autowired PublicAttributeService attService)
    {
        super(userService);
        this.service = service;
        this.attService = attService;
    }

    @PostMapping("/Insert")
    public ResponseEntity<String> insertFalsehood(RequestEntity<FullPublicFalsehood> entity, HttpServletRequest req)
    {
        FalsehoodUser user = super.getUser(entity);

        ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_SUBMIT_NEW);

        if(ret != null)
            return ret;

        FullPublicFalsehood falsehood = entity.getBody();

        if(falsehood == null || falsehood.getContents() == null || falsehood.getMetadata() == null)
        {
            return new ResponseEntity<String>("Bad Data", HttpStatus.BAD_REQUEST);
        }

        PublicFalsehood meta = falsehood.getMetadata();
        


        meta.setStatus(FalsehoodStatus.SUBMITTED.GetValue());
        
		if(meta.getTags() != null && meta.getTags().length() > 400)
			return new ResponseEntity<String>(String.format("Falsehood keys field was %d characters (400 max)", meta.getTags().length()), HttpStatus.BAD_REQUEST);

        ///meta.setDateMade(new Date(Calendar.getInstance().getTime().getTime()));



        meta = service.insertNewFalsehood(meta);



        if(!service.insertEntryToStorage(meta, falsehood.getContents(),user, req))
        {
            return new ResponseEntity<String>("Failed to Write Falsehood to Storage!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

	@PutMapping("/Approve")
	public ResponseEntity<String> approveFalsehood(RequestEntity<VerdictSubmission> entity, HttpServletRequest req)
	{
		FalsehoodUser user = super.getUser(entity);

		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT);

		if(ret != null)
			return ret;

		VerdictSubmission falsehood = entity.getBody();

		String result = service.addVerdict(falsehood.getFalsehood(), 1, falsehood.getComment(), user, req);

		if("".equals(result))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/Reject")
	public ResponseEntity<String> rejectFalsehood(RequestEntity<VerdictSubmission> entity, HttpServletRequest req)
	{
		FalsehoodUser user = super.getUser(entity);

		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT);

		if(ret != null)
			return ret;

		VerdictSubmission falsehood = entity.getBody();

		String result = service.addVerdict(falsehood.getFalsehood(), falsehood.isShouldStrike() ? -1 : 0, falsehood.getComment(), user, req);

		if("".equals(result))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
	}
    
    @PostMapping("/AddRegion")
    public ResponseEntity<String> addRegion(RequestEntity<RegionEntry> entity)
    {
    	FalsehoodUser user = super.getUser(entity);

        ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_ADD_RECOURSE);

        if(ret != null)
            return ret;

        RegionEntry region = entity.getBody();
        
        String resp = attService.InsertAttribute(region, user);
        
        if("".equals(resp))
        	return new ResponseEntity<String>(resp, HttpStatus.NO_CONTENT);
        return new ResponseEntity<String>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @PostMapping("/AddInstitution")
    public ResponseEntity<String> addInstitution(RequestEntity<InstitutionEntry> entity)
    {
    	FalsehoodUser user = super.getUser(entity);

        ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_ADD_RECOURSE);

        if(ret != null)
            return ret;

        InstitutionEntry inst = entity.getBody();
        
        String resp = attService.InsertAttribute(inst, user);
        
        if("".equals(resp))
        	return new ResponseEntity<String>(resp, HttpStatus.NO_CONTENT);
        return new ResponseEntity<String>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
	@PutMapping("/ApproveRegion")
	public ResponseEntity<String> approveRegion(RequestEntity<Long> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT_RESOURCE);
		
		if(ret != null)
			return ret;
		
		String response = attService.approveRejectRegion(entry.getBody(), true, user);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/RejectRegion")
	public ResponseEntity<String> rejectRegion(RequestEntity<Long> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT_RESOURCE);
		
		if(ret != null)
			return ret;
		
		String response = attService.approveRejectRegion(entry.getBody(), false, user);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/ApproveInstitution")
	public ResponseEntity<String> approveInstitution(RequestEntity<Long> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT_RESOURCE);
		
		if(ret != null)
			return ret;
		
		String response = attService.approveRejectInstitution(entry.getBody(), true, user);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/RejectInstitution")
	public ResponseEntity<String> rejectInstitution(RequestEntity<Long> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT_RESOURCE);
		
		if(ret != null)
			return ret;
		
		String response = attService.approveRejectInstitution(entry.getBody(), false, user);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
}
