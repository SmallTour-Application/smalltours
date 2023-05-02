package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.GuideReview;

import org.springframework.data.jpa.repository.JpaRepository;


public interface GuideReviewRepository extends JpaRepository<GuideReview, Integer> {
  
 
}
