package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.GuideProfile;

import org.springframework.data.jpa.repository.JpaRepository;


public interface GuideProfileRepository extends JpaRepository<GuideProfile, Integer> {
  
 
}
