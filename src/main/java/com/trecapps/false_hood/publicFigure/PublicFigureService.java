package com.trecapps.false_hood.publicFigure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.miscellanous.FalsehoodStorageAws;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

@Service
public class PublicFigureService 
{
	FalsehoodStorageAws awsStorage;

	PublicFigureRepo figureRepo;

	FalsehoodUserService userService;

	@Autowired
	public PublicFigureService(@Autowired FalsehoodUserService userService,
							   @Autowired PublicFigureRepo figureRepo,
							   @Autowired FalsehoodStorageAws awsStorage)
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
}
