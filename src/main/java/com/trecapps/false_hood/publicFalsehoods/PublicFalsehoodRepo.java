package com.trecapps.false_hood.publicFalsehoods;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import com.trecapps.false_hood.commonLie.CommonLie;
import com.trecapps.false_hood.publicFigure.PublicFigure;

import org.springframework.data.domain.Pageable;
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
    
    //// Queries where falsehoods are merely submitted
    
    @Query("select f from PublicFalsehood f where f.status = 0")
	List<PublicFalsehood> getSubmittedFalsehoods(Pageable p);
    
    //// Queries where falsehoods are considered confirmed and active
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5")
    List<PublicFalsehood> getConfirmedFalsehoods(Pageable p);
    
    //// Start with Between
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5")
    List<PublicFalsehood> getConfirmedFalsehoodsBetween(Pageable p, @Param("begin") Date begin, @Param("end")Date end);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.region = :region and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByRegion(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("region") Region region);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByInstitution(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("inst") Institution i);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.region = :region and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByRegionAndInst(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("region") Region region, @Param("inst") Institution i);
    

    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.official = :official and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByOfficial(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("official")PublicFigure official);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.official = :official and f.region = :region and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByOfficialRegion(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("official")PublicFigure official, @Param("region") Region region);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.official = :official and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByOfficialInstitution(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("official")PublicFigure official, @Param("inst") Institution i);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.official = :official and f.region = :region and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByOfficialRegionAndInst(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("official")PublicFigure official, @Param("region") Region region, @Param("inst") Institution i);
    
    //// Between and base Severity
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.severity <= :minSeverity and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverity(Pageable p, @Param("begin") Date begin, @Param("end")Date end, @Param("maxSeverity") byte maxSeverity, @Param("minSeverity") byte minSeverity);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.severity <= :minSeverity and f.region = :region and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityRegion(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("minSeverity") byte minSeverity, @Param("region") Region region);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.severity <= :minSeverity and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityInstitution(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("minSeverity") byte minSeverity, @Param("inst") Institution i);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.severity <= :minSeverity and f.region = :region and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityRegionAndInst(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("minSeverity") byte minSeverity, @Param("region") Region region, @Param("inst") Institution i);
    

    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.severity <= :minSeverity and f.official = :official and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityOfficial(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("minSeverity") byte minSeverity, @Param("official")PublicFigure official);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.severity <= :minSeverity and f.official = :official and f.region = :region and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegion(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("minSeverity") byte minSeverity, @Param("official")PublicFigure official, @Param("region") Region region);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.severity <= :minSeverity and f.official = :official and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityOfficialInstitution(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("minSeverity") byte minSeverity, @Param("official")PublicFigure official, @Param("inst") Institution i);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.severity <= :minSeverity and f.official = :official and f.region = :region and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndBySeverityOfficialRegionAndInst(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("minSeverity") byte minSeverity, @Param("official")PublicFigure official, @Param("region") Region region, @Param("inst") Institution i);
    
    //// Between and Min Severity
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity <= :minSeverity and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverity(Pageable p, @Param("begin") Date begin, @Param("end")Date end, @Param("minSeverity") byte minSeverity);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity <= :minSeverity and f.region = :region and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityRegion(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("minSeverity") byte minSeverity, @Param("region") Region region);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity <= :minSeverity and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityInstitution(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("minSeverity") byte minSeverity, @Param("inst") Institution i);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity <= :minSeverity and f.region = :region and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityRegionAndInst(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("minSeverity") byte minSeverity, @Param("region") Region region, @Param("inst") Institution i);
    

    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity <= :minSeverity and f.official = :official and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("minSeverity") byte minSeverity, @Param("official")PublicFigure official);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity <= :minSeverity and f.official = :official and f.region = :region and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegion(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("minSeverity") byte minSeverity, @Param("official")PublicFigure official, @Param("region") Region region);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity <= :minSeverity and f.official = :official and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialInstitution(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("minSeverity") byte minSeverity, @Param("official")PublicFigure official, @Param("inst") Institution i);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity <= :minSeverity and f.official = :official and f.region = :region and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMinSeverityOfficialRegionAndInst(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("minSeverity") byte minSeverity, @Param("official")PublicFigure official, @Param("region") Region region, @Param("inst") Institution i);
    
    //// Between and Max Severity
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverity(Pageable p, @Param("begin") Date begin, @Param("end")Date end, @Param("maxSeverity") byte maxSeverity);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.region = :region and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityRegion(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("region") Region r);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityInstitution(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("inst") Institution i);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.region = :region and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityRegionAndInst(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("region") Region r, @Param("inst") Institution i);
    

    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.official = :official and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("official")PublicFigure official);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.official = :official and f.region = :region and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegion(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("official")PublicFigure official, @Param("region") Region region);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.official = :official and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialInstitution(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("official")PublicFigure official, @Param("inst") Institution i);
    
    @Query("select f from PublicFalsehood f where f.status > 1 and f.status < 5 and f.severity >= :maxSeverity and f.official = :official and f.region = :region and f.institution = :inst and f.dateMade >= :begin and f.dateMade <= :end")
    List<PublicFalsehood> getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficialRegionAndInst(Pageable p, @Param("begin") Date begin, @Param("end")Date end,
    		@Param("maxSeverity") byte maxSeverity, @Param("official")PublicFigure official, @Param("region") Region region, @Param("inst") Institution i);
    
}
