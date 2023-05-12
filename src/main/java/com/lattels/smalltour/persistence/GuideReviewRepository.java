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


    //Member를 두 번 참조하면 JPA는 자동으로 두 개의 외래 키 관계를 설정
    //reviewer_id를 member에 id랑 조인할 필요가 없다는 소리
    @Query("SELECT gr FROM GuideReview gr JOIN gr.guide m WHERE m.id = :guideId")
    List<GuideReview> findByGuideId(@Param("guideId") int guideId);

}
