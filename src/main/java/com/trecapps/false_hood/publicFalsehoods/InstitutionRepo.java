package com.trecapps.false_hood.publicFalsehoods;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface InstitutionRepo extends JpaRepository<Institution, Long> {
	
	@Query("select i from Institution i where i.name like %:name%")
	List<Institution> getLikeName(String name);
}
