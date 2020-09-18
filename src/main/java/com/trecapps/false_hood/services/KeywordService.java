package com.trecapps.false_hood.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trecapps.false_hood.model.Falsehood;
import com.trecapps.false_hood.model.Keyword;
import com.trecapps.false_hood.repos.KeywordRepo;

@Service
public class KeywordService {

	
	@Autowired
	KeywordRepo kRepo;
	
	public boolean addKeywords(String keywords, Falsehood f)
	{
		String[] words = keywords.split(";;");
		
		for(String word: words)
		{
			word = word.trim();
			if(word.length() > 0)
			{
				Keyword keyword = (kRepo.existsById(word)) ? kRepo.getOne(word) : new Keyword(word, new ArrayList<Falsehood>());

				List<Falsehood> l = keyword.getFalsehoods();
				l.add(f);
				keyword.setFalsehoods(l);
				
				kRepo.save(keyword);
			}
		}
		
		return true;
	}
	
	public List<Falsehood> GetFalsehoodsBySearchTerms(List<String> query)
	{
		List<Falsehood> falsehoods = new ArrayList<>();
		for(int rust = 0; rust < query.size(); rust++)
		{
			Keyword key = kRepo.getOne(query.get(rust));
			
			falsehoods.addAll(key.getFalsehoods());
		}
		
		
		Map<BigInteger, List<Falsehood>> falsehoodMap = falsehoods.stream().collect(Collectors.groupingBy((Falsehood falsehood)-> falsehood.getId()));
		
		
		
		falsehoods.clear();
		
		List<List<Falsehood>> mapList = new ArrayList<>();
		
		for(Map.Entry<BigInteger, List<Falsehood>> entry : falsehoodMap.entrySet())
		{
			mapList.add(entry.getValue());
		}
		
		mapList.stream()
			.sorted((l1,l2) -> l1.size() == l2.size() ? 0 : l2.size() - l1.size()) 	// Sort the entries by how often they appear
			.map(list -> list.size() > 0 ? list.get(0) : null) 						// All entries represent the same falsehood - so just get the first in each list
			.filter(obj -> obj != null)												// Null filter, just in case
			.forEach(obj-> falsehoods.add(obj));									// Add it to the return object
		
		System.out.println("Search terms uncovered: " + falsehoods.size());
		
		return falsehoods;
	}
}
