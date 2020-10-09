package com.trecapps.false_hood.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FalsehoodUserRepo extends JpaRepository<FalsehoodUser, Long> {

}
