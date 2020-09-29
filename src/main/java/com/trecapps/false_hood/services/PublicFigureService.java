package com.trecapps.false_hood.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.model.PublicFigure;
import com.trecapps.false_hood.model.PublicFigureEntry;
import com.trecapps.false_hood.repos.FalsehoodStorageAws;
import com.trecapps.false_hood.repos.PublicFigureRepo;

@Service
public class PublicFigureService 
{
	@Autowired
	FalsehoodStorageAws awsStorage;
	
	@Autowired
	PublicFigureRepo figureRepo;
	
	
	public String submitPublicFigure(PublicFigureEntry publicFigure)
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
		
		pFigure = figureRepo.save(pFigure);
		
		if(!"Success".equals(awsStorage.addNewFile("PublicFigure-" + pFigure.getId(), publicFigure.getText())))
		{
			figureRepo.delete(pFigure);
			return "Failed to Save Public Figure to Storage!";
		}
		
		
		
		return "";
	}
}
