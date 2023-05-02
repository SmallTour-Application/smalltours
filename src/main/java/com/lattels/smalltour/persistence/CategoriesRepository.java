package com.lattels.smalltour.persistence;



import com.lattels.smalltour.model.Categories;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
  
 
}
