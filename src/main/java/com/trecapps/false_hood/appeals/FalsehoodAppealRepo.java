package com.trecapps.false_hood.appeals;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface FalsehoodAppealRepo extends JpaRepository<FalsehoodAppeal, BigInteger> {
	
	@Query("select f from FalsehoodAppeal f where f.ratified > 0")
	List<FalsehoodAppeal> getAppeals();
}
