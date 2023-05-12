package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    //tourId결제하는데 member가 회원인지 아닌지 확인(role = 0)
    //
    @Query("SELECT COUNT(p) > 0 FROM Payment p WHERE p.tours.id = :tourId AND p.member.id = :memberId AND p.member.role = 0")
    boolean existsByMemberIdAndTourId(@Param("memberId") int memberId, @Param("tourId") int tourId);

}
