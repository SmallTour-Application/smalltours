package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.GuideEducation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GuideEducationRepository extends JpaRepository<GuideEducation, GuideEducation.GuideEducationID> {


}

