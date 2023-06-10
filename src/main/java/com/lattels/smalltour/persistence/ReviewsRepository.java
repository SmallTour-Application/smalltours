package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Reviews;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {

    // 해당 투어에 대해 회원들의 평점가져오기
    // 해당 상품에 결제한 이력이 있어야함
    //Reviews, payment, Member, Tours
    //Member는 회원이니까 role이 0, payment는 결제가 완료되어야하니까 state 1
    @Query("SELECT AVG(r.rating) FROM Reviews r JOIN r.tours t JOIN r.member m JOIN Payment p ON p.tours.id = t.id AND p.member.id = m.id WHERE t.id = :tourId AND m.role = 0 AND p.state = 1")
    Float findAverageRatingByTourId(@Param("tourId") int tourId);



    @Query("SELECT r FROM Reviews r JOIN r.payment p WHERE r.tours.guide.id = :guideId AND r.payment.id = p.id AND r.payment.tours.id = r.tours.id")
    List<Reviews> findAllByGuideId(@Param("guideId") int guideId, Pageable pageable);

    @Query("SELECT count(r) FROM Reviews r JOIN r.payment p WHERE r.tours.guide.id = :guideId AND r.payment.id = p.id AND r.payment.tours.id = r.tours.id ")
    int countByGuideId(@Param("guideId") int guideId);



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
     * 해당 회원이 해당 결제에 작성한 리뷰를 불러옵니다.
     */
    List<Reviews> findByMemberIdAndPaymentId(int memberId, int paymentId);

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
