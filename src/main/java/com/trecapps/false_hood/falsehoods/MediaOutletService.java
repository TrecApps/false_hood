package com.trecapps.false_hood.falsehoods;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.miscellanous.FalsehoodStorageHolder;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.publicFigure.PublicFigureEntry;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

@Service
public class MediaOutletService {

	MediaOutletRepo moRepo;
	FalsehoodStorageHolder awsStorage;

	FalsehoodUserService userService;

	@Autowired
	public MediaOutletService(@Autowired MediaOutletRepo moRepo,
			@Autowired FalsehoodStorageHolder awsStorage,
			@Autowired FalsehoodUserService userService)
	{
		this.moRepo = moRepo;
		this.awsStorage = awsStorage;
		this.userService = userService;
	}
	
	
	public String submitMediaOutlet(MediaOutletEntry outletEntry, FalsehoodUser user)
	{
		if(outletEntry == null)
		{
			return "Public Figure Entry was null";
		}
		
		if(outletEntry.getOutlet() == null)
		{
			return "Public Figure Metadata was null";
		}
		
		if(outletEntry.getText() == null)
		{
			return "Public Figure text was null";
		}
		
		MediaOutlet pFigure = outletEntry.getOutlet();
		
		pFigure.setSubmitter(user);
		
		pFigure = moRepo.save(pFigure);
		
		if(!"Success".equals(awsStorage.addNewFile("MediaOutlet-" + pFigure.getOutletId(), outletEntry.getText())))
		{
			moRepo.delete(pFigure);
			return "Failed to Save Public Figure to Storage!";
		}
		
		
		
		return "";
	}
	
	
	public String approveRejectMediaOutlet(Integer id, boolean approve)
	{
		if(id == null)
		{
			return "Id of Media Outlet was null!";
		}
		
		if(!moRepo.existsById(id))
		{
			return "Id of Public Figure does not currently exist in our Records!";
		}
		
		MediaOutlet mOutlet = moRepo.getOne(id);
		
		
		byte setApproval = (byte) (mOutlet.getApproved() + (byte)((approve) ? 1:-1));
		
		mOutlet.setApproved(setApproval);
		
		moRepo.save(mOutlet);
		
		FalsehoodUser fUser = mOutlet.getSubmitter();
		
		if(fUser != null)
		{
			int points = (approve) ? 5 : -3;
			userService.adjustCredibility(fUser, points);
		}
		
		return "";
	}
	
	
	List<MediaOutlet> GetMediaOutlets(){
		return moRepo.findAll();
	}
	
	public MediaOutlet GetMediaOutlet(Integer id)
	{
		return moRepo.getOne(id);
	}
	
	public MediaOutletEntry GetOutletEntry(Integer id)
	{
		try {
			return new MediaOutletEntry( moRepo.getOne(id),awsStorage.retrieveContents("MediaOutlet-" + id));
		} catch(IOException e)
		{
			return null;
		}
	}
	
	public MediaOutlet GetMediaOutlet(String name)
	{
		return moRepo.getOutletByName(name);
	}
	
	public List<MediaOutlet> SearchOutletByName(String name) {
		return moRepo.getOutletLikeName(name);
	}
}
