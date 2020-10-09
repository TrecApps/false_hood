package com.trecapps.false_hood.keywords;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepo extends JpaRepository<Keyword, String> {

//	@Query("select k from Keyword k where k.")
//	List<Keyword> GetKeywordsByTerm(String term);
	
}
