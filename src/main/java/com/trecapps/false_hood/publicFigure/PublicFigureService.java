package com.trecapps.false_hood.publicFigure;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.miscellanous.FalsehoodStorageAws;
import com.trecapps.false_hood.miscellanous.FalsehoodStorageHolder;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

@Service
public class PublicFigureService 
{
	FalsehoodStorageHolder awsStorage;

	PublicFigureRepo figureRepo;

	FalsehoodUserService userService;

	@Autowired
	public PublicFigureService(@Autowired FalsehoodUserService userService,
							   @Autowired PublicFigureRepo figureRepo,
							   @Autowired FalsehoodStorageHolder awsStorage)
	{
		this.awsStorage = awsStorage;
		this.figureRepo = figureRepo;
		this.userService = userService;
	}
	
	
	public String submitPublicFigure(PublicFigureEntry publicFigure, FalsehoodUser user)
	{
		if(publicFigure == null)
		{
			return "Public Figure Entry was null";
		}
		
		if(publicFigure.getFigure() == null)
		{
			return "Public Figure Metadata was null";
		}
		
		if(publicFigure.getText() == null)
		{
			return "Public Figure text was null";
		}
		
		PublicFigure pFigure = publicFigure.getFigure();
		
		pFigure.setSubmitter(user);
		
		pFigure = figureRepo.save(pFigure);
		
		if(!"Success".equals(awsStorage.addNewFile("PublicFigure-" + pFigure.getId(), publicFigure.getText())))
		{
			figureRepo.delete(pFigure);
			return "Failed to Save Public Figure to Storage!";
		}
		
		
		
		return "";
	}
	
	
	public String approveRejectPublicFigure(Long id, boolean approve)
	{
		if(id == null)
		{
			return "Id of Public Figure was null!";
		}
		
		if(!figureRepo.existsById(id))
		{
			return "Id of Public Figure does not currently exist in our Records!";
		}
		
		PublicFigure pFigure = figureRepo.getOne(id);
		
		
		byte setApproval = (byte) (pFigure.getApproved() + (byte)((approve) ? 1:-1));
		
		pFigure.setApproved(setApproval);
		
		figureRepo.save(pFigure);
		
		FalsehoodUser fUser = pFigure.getSubmitter();
		
		if(fUser != null)
		{
			int points = (approve) ? 5 : -3;
			userService.adjustCredibility(fUser, points);
		}
		
		return "";
	}
	
	public List<PublicFigure> getPublicFigures(boolean showAll, int page, int pageSize)
	{
		
		Page<PublicFigure> figureList = showAll ? figureRepo.findAll(PageRequest.of(page, pageSize)):
			figureRepo.findAllApproved(PageRequest.of(page, pageSize));
		
		return figureList.getContent();
	}
	
	public List<PublicFigure> getPublicFigure(String entry)
	{
		String names[] = entry.replace('_', ' ').trim().split(" ");
		
		if(names.length == 0)
		{
			return null;
		}
		if(names.length == 1)
		{
			return figureRepo.findLikeName(names[0]);
		}
		if(names.length == 2)
		{
			return figureRepo.findLikeName(names[0], names[1]);
		}
		String middle = "";
		for(int rust = 1; rust < names.length -1; rust++)
		{
			middle += names[rust] + " ";
		}
		
		return figureRepo.findLikeName(names[0], middle.trim(), names[names.length-1]);
	}
	
	public PublicFigureEntry getEntryById(Long id)
	{
		try {
			return new PublicFigureEntry(figureRepo.getOne(id),awsStorage.retrieveContents("PublicFigure-" + id));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
