package com.trecapps.false_hood.repos;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.trecapps.false_hood.model.FalsehoodAppeal;

public interface FalsehoodAppealRepo extends JpaRepository<FalsehoodAppeal, BigInteger> {
	
	@Query("select f from FalsehoodAppeal f where f.ratified > 0")
	List<FalsehoodAppeal> getAppeals();
}
