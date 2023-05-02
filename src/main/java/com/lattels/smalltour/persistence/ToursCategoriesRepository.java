package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.ToursCategories;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ToursCategoriesRepository extends JpaRepository<ToursCategories, Integer> {

 
}
