package com.trecapps.false_hood.repos;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trecapps.false_hood.model.FalsehoodAppealSignature;

public interface FalsehoodAppealSignatureRepo extends JpaRepository<FalsehoodAppealSignature, BigInteger>
{

}
