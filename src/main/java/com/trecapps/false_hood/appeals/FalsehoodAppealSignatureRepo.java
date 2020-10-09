package com.trecapps.false_hood.appeals;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FalsehoodAppealSignatureRepo extends JpaRepository<FalsehoodAppealSignature, BigInteger>
{
	@Query("select s from FalsehoodAppealSignature s where s.appeal = appeal")
	List<FalsehoodAppealSignature> getSignaturesByFalsehood(FalsehoodAppeal appeal);
}
