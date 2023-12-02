package com.lattels.smalltour.persistence;

import com.lattels.smalltour.dto.admin.payment.AdminInterfacePaymentDetail;
import com.lattels.smalltour.dto.admin.payment.AdminInterfacePaymentTourList;
import com.lattels.smalltour.dto.stats.SiteProfitDTO;
import com.lattels.smalltour.dto.stats.TotalVolumePercentageDTO;
import com.lattels.smalltour.dto.stats.TotalCntPerMonthDTO;
import com.lattels.smalltour.model.Payment;
import com.lattels.smalltour.model.Tours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    //countAllByToursId
    int countAllByToursId(int toursId);

    //findByMemberId
    List<Payment> findByMemberId(int memberId, Pageable pageable);

    //findByMemberIdAndState
    List<Payment> findByMemberIdAndState(int memberId, int state, Pageable pageable);

    //countByMemberIdAndState
    int countByMemberIdAndState(int memberId, int state);

    //tourId결제하는데 member가 회원인지 아닌지 확인(role = 0)
    @Query("SELECT COUNT(p) > 0 FROM Payment p WHERE p.tours.id = :tourId AND p.member.id = :memberId AND p.member.role = 0")
    boolean existsByMemberIdAndTourId(@Param("memberId") int memberId, @Param("tourId") int tourId);

    //일정 기간 동안 일반 사용자(role:0)가 어떤 상품을 결제했는지(어떤가이드가 만든 상품을 결제했는지 가져옴)
    @Query("SELECT p FROM Payment p WHERE p.tours.guide.id = :guideId AND p.tours.id = :tourId AND p.member.role = 0 AND  p.departureDay BETWEEN :startDay AND :endDay")
    List<Payment> findPaymentsByGuideAndTourAndDepartureDay(
            @Param("guideId") int guideId,
            @Param("tourId") int tourId,
            @Param("startDay") LocalDate startDay,
            @Param("endDay") LocalDate endDay
    );

    //투어 상품이 결제가 되었는지 확인
    @Query("SELECT COUNT(p) > 0 FROM Payment p WHERE p.tours.id = :tourId AND p.state = :state")
    boolean existsByTourIdAndState(@Param("tourId") int tourId, @Param("state") int state);

    /**
     * 회원의 모든 결제 내역 개수를 불러옵니다.
     */
    long countAllByMemberId(int memberId);

    /**
     * 해당 날짜에 맞는 결제 내역 개수를 불러옵니다.
     */
    long countAllByPaymentDayBetween(LocalDateTime startPaymentDay, LocalDateTime endPaymentDay);

    /**
     * 해당 회원과 날짜에 맞는 결제 내역을 패키지명으로 검색하고 개수를 불러옵니다.
     */
    long countAllByMemberIdAndToursTitleContainsAndPaymentDayBetween(int memberId, String toursTitle, LocalDateTime startPaymentDay, LocalDateTime endPaymentDay);

    /**
     * 해당 회원과 날짜에 맞는 결제 내역을 가이드명으로 검색하고 개수를 불러옵니다.
     */
    long countAllByMemberIdAndToursGuideNameContainsAndPaymentDayBetween(int memberId, String toursGuideName, LocalDateTime startPaymentDay, LocalDateTime endPaymentDay);

    /**
     * 해당 가이드와 날짜에 맞는 결제 내역을 패키지명으로 검색하고 개수를 불러옵니다.
     */
    long countAllByToursGuideIdAndToursTitleContainsAndPaymentDayBetween(int toursGuideId, String toursTitle, LocalDateTime startPaymentDay, LocalDateTime endPaymentDay);

    /**
     * 해당 가이드와 날짜에 맞는 결제 내역을 가이드명으로 검색하고 개수를 불러옵니다.
     */
    long countAllByToursGuideIdAndToursGuideNameContainsAndPaymentDayBetween(int toursGuideId, String toursGuideName, LocalDateTime startPaymentDay, LocalDateTime endPaymentDay);

    /**
     * 페이지에 맞는 회원의 결제 내역을 최근순으로 불러옵니다.
     */
    List<Payment> findAllByMemberIdOrderByPaymentDayDesc(int memberId, Pageable pageable);

    /**
     * 해당 날짜에 맞는 결제 내역을 검색하고 최근순으로 불러옵니다.
     */
    List<Payment> findAllByPaymentDayBetweenOrderByPaymentDayDesc(LocalDateTime startPaymentDay, LocalDateTime endPaymentDay, Pageable pageable);

    /**
     * 해당 회원과 날짜에 맞는 결제 내역을 패키지명으로 검색하고 최근순으로 불러옵니다.
     */
    List<Payment> findAllByMemberIdAndToursTitleContainsAndPaymentDayBetweenOrderByPaymentDayDesc(int memberId, String toursTitle, LocalDateTime startPaymentDay, LocalDateTime endPaymentDay, Pageable pageable);

    /**
     * 해당 회원과 날짜에 맞는 결제 내역을 가이드명으로 검색하고 최근순으로 불러옵니다.
     */
    List<Payment> findAllByMemberIdAndToursGuideNameContainsAndPaymentDayBetweenOrderByPaymentDayDesc(int memberId, String toursGuideName, LocalDateTime startPaymentDay, LocalDateTime endPaymentDay, Pageable pageable);

    /**
     * 해당 가이드와 날짜에 맞는 결제 내역을 패키지명으로 검색하고 최근순으로 불러옵니다.
     */
    List<Payment> findAllByToursGuideIdAndToursTitleContainsAndPaymentDayBetweenOrderByPaymentDayDesc(int toursGuideId, String toursTitle, LocalDateTime startPaymentDay, LocalDateTime endPaymentDay, Pageable pageable);

    /**
     * 해당 가이드와 날짜에 맞는 결제 내역을 가이드명으로 검색하고 최근순으로 불러옵니다.
     */
    List<Payment> findAllByToursGuideIdAndToursGuideNameContainsAndPaymentDayBetweenOrderByPaymentDayDesc(int toursGuideId, String toursGuideName, LocalDateTime startPaymentDay, LocalDateTime endPaymentDay, Pageable pageable);


    /*
     * 해당 기간에 결제된 총 금액
     */
    @Query("SELECT SUM(p.price) FROM Payment p " +
            "WHERE p.tours = :tours " +
            "AND p.paymentDay >= :startDate AND p.paymentDay <= :endDate")
    String getPriceByDate(@Param("tours") Tours tours,
                       @Param("startDate") LocalDateTime startDate,
                       @Param("endDate") LocalDateTime endDate);

    /*
     * 해당 기간에 결제된 총 횟수
     */
    @Query("SELECT count(p) FROM Payment p " +
            "WHERE p.tours = :tours " +
            "AND p.paymentDay >= :startDate AND p.paymentDay <= :endDate")
    long getVolumeByDate(@Param("tours") Tours tours,
                       @Param("startDate") LocalDateTime startDate,
                       @Param("endDate") LocalDateTime endDate);

    //memberId로 payment찾기 + 결제성공
    @Query(value = "SELECT * FROM payment p " +
            "JOIN member m ON p.member_id = m.id " +
            "WHERE m.role = 0 AND p.state = 1 AND (:memberId IS NULL OR m.id = :memberId)", nativeQuery = true)
    List<Payment> findByPaymentMemberId(@Param("memberId") Integer memberId,Pageable pageable);

    //memberId로 payment찾기 + 결제취소
    @Query(value = "SELECT * FROM payment p " +
            "JOIN member m ON p.member_id = m.id " +
            "WHERE m.role = 0 AND p.state = 2 AND (:memberId IS NULL OR m.id = :memberId)",nativeQuery = true)
    List<Payment> findByPaymentCancelMemberId(@Param("memberId") Integer memberId,Pageable pageable);

    //memberId로 payment찾기 + 결제환불
    @Query(value = "SELECT * FROM payment p " +
            "JOIN member m ON p.member_id = m.id " +
            "WHERE m.role = 0 AND p.state = 3 AND (:memberId IS NULL OR m.id = :memberId)",nativeQuery = true)
    List<Payment> findByPaymentRefundMemberId(@Param("memberId") Integer memberId,Pageable pageable);

    /*
     * 1년간의 월별 예약 수 가져오기
     */
    @Query(value = "SELECT new com.lattels.smalltour.dto.stats.TotalCntPerMonthDTO(p.paymentDay, count(p)) " +
            "FROM Payment p " +
            "WHERE p.paymentDay >= :date AND p.state = 1 " +
            "GROUP BY p.paymentDay ")
    List<TotalCntPerMonthDTO> countPaymentPerMonth(@Param("date") LocalDateTime date);

    /*
     * 기간 동안의 판매량 비율
     */
    @Query(value = "SELECT new com.lattels.smalltour.dto.stats.TotalVolumePercentageDTO(p.tours.id, p.tours.title, p.tours.guide.name, p.tours.guide.nickname, p.tours.price, count(p) ) " +
            "FROM Payment p " +
            "WHERE p.paymentDay >= :startDate AND p.paymentDay <= :endDate " +
            "AND p.state = 1 " +
            "GROUP BY p.tours.id ")
    List<TotalVolumePercentageDTO> totalVolumePercentage(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate );

    /*
     * 기간 동안의 판매 수
     */
    @Query(value = "SELECT count(p) " +
            "FROM Payment p " +
            "WHERE p.paymentDay >= :startDate AND p.paymentDay <= :endDate " +
            "AND p.state = 1 ")
    long totalCnt(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate );

    /*
    * 기간 동안의 사이트 수익
    */
    @Query(value = "SELECT new com.lattels.smalltour.dto.stats.SiteProfitDTO((SELECT sum(up.item.price) FROM UpperPayment up WHERE up.payDay >= :startDate AND up.payDay <= :endDate), SUM(p.price)) " +
            "FROM Payment p " +
            "WHERE p.paymentDay >= :startDate AND p.paymentDay <= :endDate " +
            "AND p.state = 1 ")
    SiteProfitDTO getSiteProfit(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT p.id AS paymentId, t.id AS tourId, t.title, m.id AS memberId,m.name AS memberName, g.id AS guideId,g.name AS guideName, p.price, p.state, p.departure_day AS departureDay, p.people, p.payment_day AS paymentDay " +
            "FROM payment p " +
            "JOIN tours t ON t.id = p.tour_id " +
            "JOIN member m ON p.member_id = m.id " +
            "JOIN member g ON t.guide_id = g.id " +
            "WHERE t.id = :tourId AND p.state = :state", nativeQuery = true)
    Page<AdminInterfacePaymentTourList> findPaymentTourList(@Param("tourId")int tourId, @Param("state")int state, Pageable pageable);

    //countAllByTourIdAndState
    int countByToursIdAndState(int tourId, int state);

    @Query(value = "SELECT m.id AS memberId, m.name AS memberName, m.tel AS memberTel, p.people, p.departure_day, m.email AS memberEmail, g.id AS guideId, g.name AS guideName, p.price AS price, p.payment_day, gl.start_day, gl.end_day, p.state, t.title, t.id AS tourId " +
            "FROM payment p " +
            "JOIN tours t ON p.tour_id = t.id " +
            "JOIN member m ON p.member_id = m.id " +
            "JOIN member g ON g.id = t.guide_id " +
            "JOIN guide_lock gl ON gl.guide_id = g.id " +
            "WHERE (:memberName IS NULL OR m.name LIKE %:memberName%) " +
            "AND (:tourName IS NULL OR t.title LIKE %:tourName%) " +
            "AND (:startDay IS NULL OR gl.start_day = :startDay) " +
            "AND (:endDay IS NULL OR gl.end_day = :endDay)",
            nativeQuery = true)
    Page<Object[]> findGuidePayment(@Param("memberName") String memberName,
                                    @Param("tourName") String tourName,
                                    @Param("startDay") LocalDate startDay,
                                    @Param("endDay") LocalDate endDay,
                                    Pageable pageable);

    // findGuidePayment에서 count만
    @Query(value = "SELECT COUNT(*) " +
            "FROM payment p " +
            "JOIN tours t ON p.tour_id = t.id " +
            "JOIN member m ON p.member_id = m.id " +
            "JOIN member g ON g.id = t.guide_id " +
            "JOIN guide_lock gl ON gl.guide_id = g.id " +
            "WHERE (:memberName IS NULL OR m.name LIKE %:memberName%) " +
            "AND (:tourName IS NULL OR t.title LIKE %:tourName%) " +
            "AND (:startDay IS NULL OR gl.start_day = :startDay) " +
            "AND (:endDay IS NULL OR gl.end_day = :endDay)",
            nativeQuery = true)
    int findGuidePaymentCount(@Param("memberName") String memberName,
                                    @Param("tourName") String tourName,
                                    @Param("startDay") LocalDate startDay,
                                    @Param("endDay") LocalDate endDay);


    // findGuidePayment에서 날짜 빼고 검색
    @Query(value = "SELECT m.id AS memberId, m.name AS memberName, m.tel AS memberTel, p.people, p.departure_day, m.email AS memberEmail, g.id AS guideId, g.name AS guideName, p.price AS price, p.payment_day, gl.start_day, gl.end_day, p.state, t.title, t.id AS tourId " +
            "FROM payment p " +
            "JOIN tours t ON p.tour_id = t.id " +
            "JOIN member m ON p.member_id = m.id " +
            "JOIN member g ON g.id = t.guide_id " +
            "JOIN guide_lock gl ON gl.guide_id = g.id " +
            "WHERE (:memberName IS NULL OR m.name LIKE %:memberName%) " +
            "AND (:tourName IS NULL OR t.title LIKE %:tourName%) ",
            nativeQuery = true)
    Page<Object[]> findGuidePaymentWithoutDate(@Param("memberName") String memberName,
                                    @Param("tourName") String tourName,
                                    Pageable pageable);

    //findGuidePaymentWithoutDate에서 count만
    @Query(value = "SELECT COUNT(*) " +
            "FROM payment p " +
            "JOIN tours t ON p.tour_id = t.id " +
            "JOIN member m ON p.member_id = m.id " +
            "JOIN member g ON g.id = t.guide_id " +
            "JOIN guide_lock gl ON gl.guide_id = g.id " +
            "WHERE (:memberName IS NULL OR m.name LIKE %:memberName%) " +
            "AND (:tourName IS NULL OR t.title LIKE %:tourName%) ",
            nativeQuery = true)
    int findGuidePaymentWithoutDateCount(@Param("memberName") String memberName,
                                    @Param("tourName") String tourName);


    @Query(value = "SELECT p.id AS paymentId, m.id AS memberId, m.name AS memberName, m.email, m.tel, " +
            "g.id AS guideId, g.name AS guideName, p.payment_day AS paymentDay, gl.start_day AS startDay, gl.end_day AS endDay, " +
            "p.people, p.state, t.id AS tourId, t.title, t.price, p.departure_day AS departureDay " +
            "FROM payment p " +
            "JOIN tours t ON p.tour_id = t.id " +
            "JOIN member m ON p.member_id = m.id " +
            "JOIN member g ON t.guide_id = g.id " +
            "JOIN guide_lock gl ON gl.guide_id = g.id " +
            "WHERE p.id = :paymentId", nativeQuery = true)
    Optional<AdminInterfacePaymentDetail> findByPaymentDetail(@Param("paymentId") int paymentId);
}
