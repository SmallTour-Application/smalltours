package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Question;

import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionRepository extends JpaRepository<Question, Integer> {
  
 
}
