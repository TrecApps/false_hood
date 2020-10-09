package com.trecapps.false_hood.publicFalsehoods;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PublicFalsehoodRepo extends JpaRepository<PublicFalsehood, BigInteger> {

}
