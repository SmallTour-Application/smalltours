package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.GuideReview;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface GuideReviewRepository extends JpaRepository<GuideReview, Integer> {

    /**
     * 가이드와 페이지에 맞는 최근 가이드 리뷰를 불러옵니다.
     */
    Page<GuideReview> findAllByGuideIdOrderByCreatedDayDesc(@Param("guideId") int guideId, Pageable pageable);

    /**
     * 해당 사용자가 작성한 페이지에 맞는 최근 가이드 리뷰를 불러옵니다.
     */
    Page<GuideReview> findAllByReviewerIdOrderByCreatedDayDesc(@Param("reviewerId") int reviewerId, Pageable pageable);

    /**
     * 해당 회원이 해당 결제에 작성한 리뷰를 불러옵니다.
     */
    List<GuideReview> findByReviewerIdAndPaymentId(int memberId, int paymentId);

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
     * 해당 사용자가 작성한 가이드 리뷰 개수를 불러옵니다.
     */
    long countAllByReviewerId(@Param("reviewerId") int reviewerId);

    /**
     * 리뷰 작성자가 가이드에게 댓글을 작성했는지 여부를 반환합니다.
     */
    boolean existsByReviewerIdAndGuideId(@Param("reviewerId") int reviewerId, @Param("guideId") int guideId);


    //
    @Query("SELECT gr FROM GuideReview gr JOIN gr.guide m WHERE m.id = :guideId")
    List<GuideReview> findGuideReviewsByGuideIdAndRole(@Param("guideId") int guideId);







/*    @Query("SELECT gr FROM GuideReview gr JOIN gr.guide m1 JOIN gr.reviewer m2 WHERE m1.id = :guideId AND m1.role = 2 AND m2.role = 0")
    List<GuideReview> findGuideReviewsByGuideId(@Param("guideId") int guideId);*/






    //가이드에 대한 평점
    //굳이 해당 가이드 리뷰 남기는 조건들(payment 결제내역 있어야하고 해당 Tours에 대한 Guide_id에대해서 남겨야한다 블라블라)은 있을 필요가 없음
   @Query("SELECT AVG(r.rating) FROM GuideReview r JOIN r.guide g WHERE g.id = :guideId AND g.role = 2")
    Float findAverageRatingByGuideId(@Param("guideId") int guideId);


    // 가이드 리뷰 평점과 투어 리뷰 평점의 평점
    @Query(value = "SELECT ( " +
            "(SELECT AVG(rating) FROM guide_review WHERE guide_id = :guideId) " +
            "+ (SELECT AVG(r.rating) FROM reviews r, tours t , `member` m  " +
            "where r.tour_id = t.id and t.guide_id = m.id and m.id = :guideId ) " +
            ") / 2 AS rating", nativeQuery = true)
    double getGuideAndToursRating(@Param("guideId") int guideId);



    /*
     * 해당 기간에 받은 리뷰 평균
     */
    @Query("SELECT AVG(gr.rating) FROM GuideReview gr " +
            "WHERE gr.guide = :guide " +
            "AND gr.createdDay >= :startDate AND gr.createdDay <= :endDate")
    String getReviewRating(@Param("guide") Member member,
                           @Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate);



}
