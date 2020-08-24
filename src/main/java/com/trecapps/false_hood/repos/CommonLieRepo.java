package com.trecapps.false_hood.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trecapps.false_hood.model.CommonLie;

@Repository
public interface CommonLieRepo extends JpaRepository<CommonLie, Long>
{
	@Query("select cl from CommonLie cl where ?1 like cl.title")
	List<CommonLie> getCommonLieByTitleStart(String title);
}
