package com.trecapps.false_hood.falsehoods;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import com.trecapps.false_hood.publicFalsehoods.PublicFalsehood;
import com.trecapps.false_hood.publicFigure.PublicFigure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
	List<Falsehood> getFalsehoodsByPublicFigure(PublicFigure author);
	
	
	//// Queries where Falsehoods are merely submitted
	
	@Query("select f from Falsehood f where f.status = 0")
	List<Falsehood> getSubmittedFalsehood(Pageable p);
	
	//// Queries where Falsehoods are considered confirmed and active
	
	@Query("select f from Falsehood f where f.status > 1 and f.status < 5")
	List<Falsehood> getConfirmedFalsehoods(Pageable p);
	
	@Query("select f from Falsehood f where f.status > 1 and f.status < 5 and (f.author1 = :author or f.author2 = :author)")
	List<Falsehood> getConfirmedFalsehoodsByPublicOfficial(Pageable p, @Param("author")PublicFigure author);
	
	@Query("select f from Falsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.severity <= :minSeverity")
	List<Falsehood> getConfirmedFalsehoodsBySeverity(Pageable p, @Param("maxSeverity") byte maxSeverity, @Param("minSeverity") byte minSeverity);
	
	@Query("select f from Falsehood f where f.status > 1 and f.status < 5 and f.severity <= :minSeverity")
	List<Falsehood> getConfirmedFalsehoodByMinSeverity(Pageable p, @Param("minSeverity")byte minSeverity);
	
	@Query("select f from Falsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity")
	List<Falsehood> getConfirmedFalsehoodByMaxSeverity(Pageable p, @Param("maxSeverity") byte maxSeverity);
	
	
	
	//// Queries where Falsehoods are considered Rejected
	
	@Query("select f from Falsehood f where f.status > 4")
	List<Falsehood> getRejectedFalsehoods(Pageable p);
	
	@Query("select f from Falsehood f where f.status > 4 and (f.author1 = :author or f.author2 = :author)")
	List<Falsehood> getRejectedFalsehoodsByPublicOfficial(Pageable p, @Param("author")PublicFigure author);
	
	@Query("select f from Falsehood f where f.status > 4 and f.severity >= :maxSeverity and f.severity <= :minSeverity")
	List<Falsehood> getRejectedFalsehoodsBySeverity(Pageable p, @Param("maxSeverity") byte maxSeverity, @Param("minSeverity") byte minSeverity);
	
	@Query("select f from Falsehood f where f.status > 4 and f.severity <= :minSeverity")
	List<Falsehood> getRejectedFalsehoodByMinSeverity(Pageable p, @Param("minSeverity")byte minSeverity);
	
	@Query("select f from Falsehood f where f.status > 4 and f.severity >= :maxSeverity")
	List<Falsehood> getRejectedFalsehoodByMaxSeverity(Pageable p, @Param("maxSeverity") byte maxSeverity);
}
