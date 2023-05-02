package com.lattels.smalltour.persistence;



import com.lattels.smalltour.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EducationRepository extends JpaRepository<Education, Integer> {
  
 
}
