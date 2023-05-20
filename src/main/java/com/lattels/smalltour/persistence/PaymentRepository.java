package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment, Integer> {

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





}
