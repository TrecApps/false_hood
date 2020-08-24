package com.trecapps.false_hood.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trecapps.false_hood.model.MediaOutlet;

@Repository
public interface MediaOutletRepo extends JpaRepository<MediaOutlet, Integer> {

	
	@Query("select m from MediaOutlet m where m.name = ?1")
	MediaOutlet getOutletByName(String name);
}
