package com.trecapps.false_hood.repos;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trecapps.false_hood.model.FalsehoodAppeal;

public interface FalsehoodAppealRepo extends JpaRepository<FalsehoodAppeal, BigInteger> {

}
