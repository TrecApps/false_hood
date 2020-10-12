package com.trecapps.false_hood.publicFalsehoods;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import com.trecapps.false_hood.commonLie.CommonLie;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface PublicFalsehoodRepo extends JpaRepository<PublicFalsehood, BigInteger> {

    @Query("select f from PublicFalsehood f where f.dateMade > ?1 and f.dateMade < ?2")
    List<PublicFalsehood> getFalsehoodsBetween(Date begin, Date end);

    @Query("select f from PublicFalsehood f where f.dateMade < ?1")
    List<PublicFalsehood> getFalsehoodsBefore(Date end);

    @Query("select f from PublicFalsehood f where f.severity <= :minSeverity")
    List<PublicFalsehood> getFalsehoodsByMinimumSeverity(@Param("minSeverity")byte severity);

    @Query("select f from PublicFalsehood f where f.severity >= :maxSeverity")
    List<PublicFalsehood> getFalsehoodsByMaximumSeverity(@Param("maxSeverity")byte maxSeverity);

    @Query("select f from PublicFalsehood f where f.severity >= :maxSeverity and f.severity <= :minSeverity")
    List<PublicFalsehood> getFalsehoodBySeverity(@Param("maxSeverity") byte maxSeverity, @Param("minSeverity") byte minSeverity);

    @Query("select f from PublicFalsehood f where f.status = 2 or f.status = 5")
    List<PublicFalsehood> getChallengedFalsehoods();

    @Query("select f from PublicFalsehood f where f.status = 1 or f.status = 3 or f.status = 4")
    List<PublicFalsehood> getConfirmedFalsehoods();

    @Query("select f from PublicFalsehood f where f.official = ?1")
    List<PublicFalsehood> getFalsehoodsByPublicFigure(PublicFigure author);

    @Query("select f from PublicFalsehood f where f.commonLie = ?1")
    List<PublicFalsehood> getFalsehoodsByCommonLie(CommonLie lie);

    @Query("select f from PublicFalsehood f where f.region = ?1")
    List<PublicFalsehood> getFalsehoodsByRegion(Region region);

    @Query("select f from PublicFalsehood f where f.institution = ?1")
    List<PublicFalsehood> getFalsehoodsByInstitution(Institution institution);
}
