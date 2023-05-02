package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.EmailToken;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EmailTokenRepository extends JpaRepository<EmailToken, String> {
  
 
}
