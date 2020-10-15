package com.trecapps.false_hood.falsehoods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaOutletService {

	MediaOutletRepo moRepo;

	@Autowired
	public MediaOutletService(@Autowired MediaOutletRepo moRepo)
	{
		this.moRepo = moRepo;
	}
	
	List<MediaOutlet> GetMediaOutlets(){
		return moRepo.findAll();
	}
	
	public MediaOutlet GetMediaOutlet(Integer id)
	{
		return moRepo.getOne(id);
	}
	
	public MediaOutlet GetMediaOutlet(String name)
	{
		return moRepo.getOutletByName(name);
	}
}
