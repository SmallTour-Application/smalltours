package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.ReviewComment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Integer> {
  
 
}
