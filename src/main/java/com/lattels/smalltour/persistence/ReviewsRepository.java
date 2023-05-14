package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Reviews;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {

    // 해당 투어에 대해 결제를 한 회원의 평점가져오기
    @Query("SELECT AVG(r.rating) FROM Reviews r JOIN r.member m WHERE r.tours.id = :tourId AND m.role = 0 AND EXISTS (SELECT p FROM Payment p WHERE p.member.id = m.id AND p.tours.id = :tourId)")
    Float findAverageRatingByTourId(@Param("tourId") int tourId);


    @Query("SELECT r FROM Reviews r WHERE r.tours.guide.id = :guideId AND r.tours.guide.role = 2")
    List<Reviews> findAllByGuideId(@Param("guideId") int guideId, Pageable pageable);


}
