package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Notice;

import org.springframework.data.jpa.repository.JpaRepository;


public interface NoticeRepository extends JpaRepository<Notice, Integer> {
  
 
}
