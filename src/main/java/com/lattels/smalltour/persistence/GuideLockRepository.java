package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.GuideLock;

import org.springframework.data.jpa.repository.JpaRepository;


public interface GuideLockRepository extends JpaRepository<GuideLock, Integer> {
  
 
}
