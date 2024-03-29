package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Reviews;

import com.lattels.smalltour.model.Tours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {

    // 해당 투어에 대해 회원들의 평점가져오기
    // 해당 상품에 결제한 이력이 있어야함
    //Reviews, payment, Member, Tours
    //Member는 회원이니까 role이 0, payment는 결제가 완료되어야하니까 state 1
    @Query("SELECT AVG(r.rating) FROM Reviews r JOIN r.tours t JOIN r.member m JOIN Payment p ON p.tours.id = t.id AND p.member.id = m.id WHERE t.id = :tourId AND m.role = 0 AND p.state = 1")
    Float findAverageRatingByTourId(@Param("tourId") int tourId);

    //countByMemberId jpa로
    //해당 회원이 작성한 리뷰 개수
    int countByMemberIdAndStateOrderByCreatedDayDesc(int memberId, int state);

    List<Reviews> findByMemberIdAndStateOrderByCreatedDayDesc(int memberId, int state, Pageable pageable);

    //countByGuideIdAndState
    //해당 가이드에 대한 리뷰 개수
    @Query("SELECT COUNT(r) FROM Reviews r JOIN r.tours t WHERE t.guide.id = :guideId AND r.tours.id = t.guide.id AND r.state = :state ORDER BY r.createdDay DESC")
    int countByGuideIdAndState(@Param("guideId") int guideId, @Param("state") int state);


    @Query("SELECT r FROM Reviews r JOIN r.payment p WHERE r.tours.guide.id = :guideId AND r.payment.id = p.id AND r.payment.tours.id = r.tours.id")
    List<Reviews> findAllByGuideId(@Param("guideId") int guideId, Pageable pageable);

    @Query("SELECT count(r) FROM Reviews r JOIN r.payment p WHERE r.tours.guide.id = :guideId AND r.payment.id = p.id AND r.payment.tours.id = r.tours.id ")
    int countByGuideId(@Param("guideId") int guideId);



    //가이드에 대한 평점
    @Query("SELECT AVG(r.rating) FROM GuideReview r JOIN r.guide g WHERE g.id = :guideId AND g.role = 2")
    Float findAverageRatingByGuideId(@Param("guideId") Integer guideId);


    /**
     * 특정 멤버가 만든 여행의 리뷰를 날짜순으로 가져옵니다
     */
    @Query("SELECT r FROM Reviews r JOIN r.tours t WHERE t.guide.id = :guideId AND r.state = :state ORDER BY r.createdDay DESC")
    List<Reviews> findAllByGuideIdAndStateOrderByCreatedDayDESC(@Param("guideId") int guideId, @Param("state") int state, Pageable pageable);


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
     * 해당 회원이 해당 결제에 대한 리뷰를 작성했는지 반환합니다.
     */
    boolean existsByMemberIdAndPaymentId(int memberId, int paymentId);

    /**
     * 회원이 작성한 리뷰 개수를 반환합니다.
     */
    long countAllByMemberId(int memberId);

    /**
     * 패키지 리뷰 개수를 반환합니다.
     */
    long countAllByToursId(int tourId);


    /*
     * 해당 기간에 받은 리뷰 평균
     */
    @Query("SELECT AVG(r.rating) FROM Reviews r " +
            "WHERE r.tours.guide = :guide " +
            "AND r.createdDay >= :startDate AND r.createdDay <= :endDate")
    String getReviewRating(@Param("guide") Member guide,
                         @Param("startDate") LocalDateTime startDate,
                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT AVG(r.rating) FROM Reviews r WHERE r.tours = :tours")
    String getRating(@Param("tours") Tours tours);


    @Query(value = "SELECT r.id AS reviewId,t.id, t.title, r.content, r.created_day, p.id AS paymentId, m.name, r.state, r.member_id AS reviewerId, g.id AS guideId " +
            "FROM reviews r " +
            "INNER JOIN payment p ON r.payment_id = p.id " +
            "INNER JOIN tours t ON r.tour_id = t.id " +
            "INNER JOIN member m ON r.member_id = m.id " +
            "INNER JOIN member g ON t.guide_id = g.id " +
            "WHERE (:title IS NULL OR t.title LIKE CONCAT('%', :title, '%')) " +
            "AND (:month IS NULL OR MONTH(r.created_day) = :month) " +
            "AND (:year IS NULL OR YEAR(r.created_day) = :year) " +
            "AND (:name IS NULL OR m.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:state IS NULL OR r.state = :state) " +
            "AND (:memberId IS NULL OR r.member_id = :memberId)", nativeQuery = true)
    Page<Object[]> findReviewsByConditions(@Param("title") String title,
                                           @Param("month") Integer month,
                                           @Param("year") Integer year,
                                           @Param("name") String name,
                                           @Param("state") Integer state,
                                           @Param("memberId") Integer memberId,
                                           Pageable pageable);

    @Query(value = "SELECT count(r.id) " +
            "FROM reviews r " +
            "JOIN payment p ON r.payment_id = p.id " +
            "JOIN tours t ON r.tour_id = t.id " +
            "JOIN member m ON r.member_id = m.id " +
            "WHERE (:title IS NULL OR t.title LIKE CONCAT('%', :title, '%')) " +
            "AND (:month IS NULL OR MONTH(r.created_day) = :month) " +
            "AND (:year IS NULL OR YEAR(r.created_day) = :year) " +
            "AND (:name IS NULL OR m.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:state IS NULL OR r.state =:state) ", nativeQuery = true)
    long findReviewsCount(@Param("title") String title,
                          @Param("month") Integer month,
                          @Param("year") Integer year,
                          @Param("name") String name,
                          @Param("state") Integer state);



    @Query(value = "SELECT r.id AS reviewId,t.id, t.title, r.content, r.created_day, p.id AS paymentId, m.name, r.state, r.member_id AS memberId, g.id AS guideId " +
            "FROM reviews r " +
            "JOIN payment p ON r.payment_id = p.id " +
            "JOIN tours t ON r.tour_id = t.id " +
            "JOIN member m ON r.member_id = m.id " +
            "JOIN member g ON t.guide_id = g.id " , nativeQuery = true)
    Page<Object[]> findAllReviews(Pageable pageable);


    @Query(value = "SELECT count(r.id) " +
            "FROM reviews r " +
            "JOIN payment p ON r.payment_id = p.id " +
            "JOIN tours t ON r.tour_id = t.id " +
            "JOIN member m ON r.member_id = m.id " , nativeQuery = true)
    long countAllReviews();


    @Modifying
    @Transactional
    @Query("UPDATE Reviews r SET r.content = :content WHERE r.id = :id AND r.state = 1")
    void updateReviewContent(@Param("id") int id, @Param("content") String content);

    @Modifying
    @Transactional
    @Query("UPDATE Reviews r SET r.state = 0 WHERE r.id = :id")
    void updateReviewState(@Param("id") int id);


    @Modifying
    @Transactional
    @Query("UPDATE Reviews r SET r.content = :content WHERE r.tours.id = :tourId AND r.id = :reviewId AND r.state = 1")
    void updateTourReviewContent(@Param("tourId") int tourId,@Param("reviewId") int reviewId, @Param("content") String content);

    @Modifying
    @Transactional
    @Query("UPDATE Reviews r SET r.state = 0 WHERE r.tours.id = :tourId AND r.id = :reviewId")
    void updateTourReviewState(@Param("tourId") int tourId,@Param("reviewId") int reviewId);

    @Query(value = "SELECT r.id AS reviewId,t.id, t.title, r.content, r.created_day, r.state, p.id AS paymentId, m.name, m.id AS memberId, g.id AS guideId " +
            "FROM reviews r " +
            "JOIN tours t ON r.tour_id = t.id " +
            "JOIN member m ON r.member_id = m.id " +
            "JOIN member g ON t.guide_id = g.id " +
            "JOIN payment p ON r.payment_id = p.id " +
            "WHERE (:tourId IS NULL OR t.id = :tourId) " +
            "AND (:state IS NULL OR r.state = :state)", nativeQuery = true)
    Page<Object[]> findByToursIdAndState(@Param("tourId") Integer tourId, @Param("state") Integer state, Pageable pageable);

    @Query(value = "SELECT r.id AS reviewId,t.id, t.title, r.content, r.created_day, r.state, p.id AS paymentId, m.name, m.id AS memberId, g.id AS guideId " +
            "FROM reviews r " +
            "JOIN tours t ON r.tour_id = t.id " +
            "JOIN member m ON r.member_id = m.id " +
            "JOIN member g ON t.guide_id = g.id " +
            "JOIN payment p ON r.payment_id = p.id", nativeQuery = true)
    Page<Object[]> findByToursId(Pageable pageable);




    @Query(value = "SELECT " +
            "r.id AS reviewId, " +
            "t.id,t.title , " +
            "guide.id AS guideId, guide.name , " +
            "buyer.id AS buyerId, buyer.name , " +
            "r.rating , " +
            "r.content  " +
            "FROM Reviews r " +
            "JOIN r.tours t " +
            "JOIN Member guide ON t.guide = guide AND guide.role = 2 " +
            "JOIN Member buyer ON r.member = buyer AND buyer.role = 0 " +
            "WHERE buyer.id = :memberId AND r.id = :reviewId")
    List<Object[]> findReviewsByBuyerId(@Param("memberId") int memberId,@Param("reviewId") int reviewId);

    @Modifying
    @Transactional
    @Query("UPDATE Reviews r SET r.content = :content,r.rating = :rating WHERE r.id = :id AND r.state = 1")
    void updateDetailReviewContent(@Param("id") int id, @Param("content") String content,@Param("rating") int rating);

    @Modifying
    @Transactional
    @Query("UPDATE Reviews r SET r.state = 0 WHERE r.id = :id")
    void updateDetailReviewState(@Param("id") int id);

    @Query(value = "SELECT COUNT(r) " +
            "FROM Reviews r " +
            "WHERE r.createdDay BETWEEN :startDay AND :endDay")
    long searchTotalCntByDate(@Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);

    @Query(value = "SELECT AVG(r.rating) " +
            "FROM Reviews r " +
            "WHERE r.createdDay BETWEEN :startDay AND :endDay")
    String searchTotalRatingByDate(@Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);


}
