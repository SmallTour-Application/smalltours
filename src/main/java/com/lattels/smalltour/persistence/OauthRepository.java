package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Oauth;

import org.springframework.data.jpa.repository.JpaRepository;


public interface OauthRepository extends JpaRepository<Oauth, Integer> {
  
 
}
