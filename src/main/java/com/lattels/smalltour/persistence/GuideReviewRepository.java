package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.GuideReview;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GuideReviewRepository extends JpaRepository<GuideReview, Integer> {

    /**
     * 가이드와 페이지에 맞는 최근 가이드 리뷰를 불러옵니다.
     */
    Page<GuideReview> findAllByGuideIdOrderByCreatedDayDesc(@Param("guideId") int guideId, Pageable pageable);

    /**
     * 가이드의 평균 평점을 불러옵니다.
     */
    @Query("SELECT AVG(g.rating) FROM GuideReview g WHERE g.guide.id = :guideId")
    float averageOfRatingsByGuideId(@Param("guideId") int guideId);

    /**
     * 가이드의 가이드 리뷰 개수를 불러옵니다.
     */
    long countAllByGuideId(@Param("guideId") int guideId);

    /**
     * 리뷰 작성자가 가이드에게 댓글을 작성했는지 여부를 반환합니다.
     */
    boolean existsByReviewerIdAndGuideId(@Param("reviewerId") int reviewerId, @Param("guideId") int guideId);


    //가이드리뷰 테이블에 가이드id 랑 member 테이블 id랑 조인, role = 2 이어야함
    @Query("SELECT gr FROM GuideReview gr JOIN gr.guide m WHERE m.id = :guideId AND m.role = 2")
    List<GuideReview> findGuideReviewsByGuideIdAndRole(@Param("guideId") int guideId);


    //가이드에 대한 평점
    //굳이 해당 가이드 리뷰 남기는 조건들(payment 결제내역 있어야하고 해당 Tours에 대한 Guide_id에대해서 남겨야한다 블라블라)은 있을 필요가 없음
   @Query("SELECT AVG(r.rating) FROM GuideReview r JOIN r.guide g WHERE g.id = :guideId AND g.role = 2")
    Float findAverageRatingByGuideId(@Param("guideId") int guideId);










}
