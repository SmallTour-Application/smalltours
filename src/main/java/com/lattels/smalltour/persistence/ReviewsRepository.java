package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Reviews;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {

 
}
