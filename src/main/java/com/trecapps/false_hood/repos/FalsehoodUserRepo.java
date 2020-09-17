package com.trecapps.false_hood.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trecapps.false_hood.model.FalsehoodUser;

public interface FalsehoodUserRepo extends JpaRepository<FalsehoodUser, Long> {

}
