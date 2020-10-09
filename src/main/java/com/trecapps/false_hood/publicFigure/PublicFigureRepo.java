package com.trecapps.false_hood.publicFigure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PublicFigureRepo extends JpaRepository<PublicFigure, Long>
{
	@Query("select p from PublicFigure p where p.approved > 0")
	Page<PublicFigure> findAllApproved(Pageable pageable); 
}
