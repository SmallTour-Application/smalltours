package com.lattels.smalltour.persistence;



import com.lattels.smalltour.model.BestGuide;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BestGuideRepository extends JpaRepository<BestGuide, Integer> {

    boolean existsBestGuideByGuideId(int guideId);

}
