package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Reviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {

    // 해당 투어에 대해 결제를 한 회원의 평점가져오기
    @Query("SELECT AVG(r.rating) FROM Reviews r JOIN r.member m WHERE r.tours.id = :tourId AND m.role = 0 AND EXISTS (SELECT p FROM Payment p WHERE p.member.id = m.id AND p.tours.id = :tourId)")
    Float findAverageRatingByTourId(@Param("tourId") int tourId);

    //가이드에 대한 평점
    @Query("SELECT AVG(r.rating) FROM GuideReview r JOIN r.guide g WHERE g.id = :guideId AND g.role = 2")
    Float findAverageRatingByGuideId(@Param("guideId") Integer guideId);


}
