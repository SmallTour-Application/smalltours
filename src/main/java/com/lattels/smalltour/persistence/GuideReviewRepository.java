package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.GuideReview;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GuideReviewRepository extends JpaRepository<GuideReview, Integer> {

    Page<GuideReview> findAllByGuideId(int guideId, Pageable pageable);

    boolean existsByReviewerIdAndGuideId(int reviewerId, int guideId);
 
}
