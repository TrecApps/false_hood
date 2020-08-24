package com.trecapps.false_hood.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trecapps.false_hood.model.Keyword;

@Repository
public interface KeywordRepo extends JpaRepository<Keyword, String> {

//	@Query("select k from Keyword k where k.")
//	List<Keyword> GetKeywordsByTerm(String term);
	
}
