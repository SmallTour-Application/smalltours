package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    //tourId결제하는데 member가 회원인지 아닌지 확인(role = 0)
    //
    @Query("SELECT COUNT(p) > 0 FROM Payment p WHERE p.tours.id = :tourId AND p.member.id = :memberId AND p.member.role = 0")
    boolean existsByMemberIdAndTourId(@Param("memberId") int memberId, @Param("tourId") int tourId);

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

}
