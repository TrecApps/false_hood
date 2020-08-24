package com.trecapps.false_hood.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.model.CommonLie;
import com.trecapps.false_hood.repos.CommonLieRepo;

@Service
public class CommonLieService {

	@Autowired
	CommonLieRepo clRepo;
	
	CommonLie GetLieById(long id)
	{
		return clRepo.getOne(id);
	}
	
	List<CommonLie> GetLieBySearchTerm(String title)
	{
		return clRepo.getCommonLieByTitleStart(title);
	}
}
