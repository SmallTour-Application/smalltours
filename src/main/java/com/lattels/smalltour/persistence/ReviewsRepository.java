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

    //가이드에 대한 평점
    @Query("SELECT AVG(r.rating) FROM GuideReview r JOIN r.guide g WHERE g.id = :guideId AND g.role = 2")
    Float findAverageRatingByGuideId(@Param("guideId") Integer guideId);

    /**
     * 회원이 작성한 리뷰를 페이지에 맞게 최근순으로 반환합니다.
     */
    List<Reviews> findByMemberIdOrderByCreatedDayDesc(int memberId, Pageable pageable);

    /**
     * 패키지 리뷰를 페이지에 맞게 최근순으로 반환합니다.
     */
    List<Reviews> findByToursIdOrderByCreatedDayDesc(int tourId, Pageable pageable);

    /**
     * 해당 패키지에 해당 회원이 리뷰를 작성했는지 반환합니다.
     */
    boolean existsByToursIdAndMemberId(int tourId, int memberId);

    /**
     * 회원이 작성한 리뷰 개수를 반환합니다.
     */
    long countAllByMemberId(int memberId);

    /**
     * 패키지 리뷰 개수를 반환합니다.
     */
    long countAllByToursId(int tourId);

}
