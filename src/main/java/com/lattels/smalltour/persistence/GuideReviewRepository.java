package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.GuideReview;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GuideReviewRepository extends JpaRepository<GuideReview, Integer> {

    Page<GuideReview> findAllByGuideId(int guideId, Pageable pageable);

    boolean existsByReviewerIdAndGuideId(int reviewerId, int guideId);


    //가이드리뷰 테이블에 가이드id 랑 member 테이블 id랑 조인, role = 2 이어야함
    @Query("SELECT gr FROM GuideReview gr JOIN gr.guide m WHERE m.id = :guideId AND m.role = 2")
    List<GuideReview> findGuideReviewsByGuideIdAndRole(@Param("guideId") int guideId);


    //가이드에 대한 평점
    @Query("SELECT AVG(r.rating) FROM GuideReview r JOIN r.guide g WHERE g.id = :guideId AND g.role = 2")
    Float findAverageRatingByGuideId(@Param("guideId") Integer guideId);





}
