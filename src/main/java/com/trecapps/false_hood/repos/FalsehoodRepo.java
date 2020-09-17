package com.trecapps.false_hood.repos;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trecapps.false_hood.model.Falsehood;
import com.trecapps.false_hood.model.MediaOutlet;

@Repository
public interface FalsehoodRepo extends JpaRepository<Falsehood, BigInteger> {

	@Query("select f from Falsehood f where f.dateMade > ?1 and f.dateMade < ?2")
	List<Falsehood> getFalsehoodsBetween(Date begin, Date end);
	
	@Query("select f from Falsehood f where f.dateMade < ?1")
	List<Falsehood> getFalsehoodsBefore(Date end);
	
	@Query("select f from Falsehood f where f.severity <= :minSeverity")
	List<Falsehood> getFalsehoodsByMinimumSeverity(@Param("minSeverity")byte minSeverity);
	
	@Query("select f from Falsehood f where f.severity >= :maxSeverity")
	List<Falsehood> getFalsehoodsByMaximumSeverity(@Param("maxSeverity")byte maxSeverity);
	
	@Query("select f from Falsehood f where f.severity >= :maxSeverity and f.severity <= :minSeverity")
	List<Falsehood> getFalshoodBySeverity(@Param("maxSeverity") byte maxSeverity, @Param("minSeverity") byte minSeverity);
	
	@Query("select f from Falsehood f where f.outlet = ?1")
	List<Falsehood> getFalsehoodsByOutlet(MediaOutlet outletId);
	
	@Query("select f from Falsehood f where f.mediaType = ?1")
	List<Falsehood> getFalsehoodsByMediaType(int mType);
	
	@Query("select f from Falsehood f where f.status = 2 or f.status = 5")
	List<Falsehood> getChallengedFalsehoods();
	
	@Query("select f from Falsehood f where f.status = 1 or f.status = 3 or f.status = 4")
	List<Falsehood> getConfirmedFalsehoods();
	
	@Query("select f from Falsehood f where f.author1 = ?1 or f.author2 = ?1")
	List<Falsehood> getFalsehoodsByAuthor(String author);
	
	@Query("select max(f.id) from Falsehood f")
	BigInteger getMaxId();
}
