package com.trecapps.false_hood.keywords;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PublicKeywordRepo extends JpaRepository<PublicKeyword, String>
{

}
