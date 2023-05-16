package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, PaymentSchedule.PaymentScheduleID> {

    /**
     * 결제 ID에 맞는 결제된 패키지 일정을 모두 삭제합니다.
     */
    void deleteAllByPaymentId(int paymentId);
 
}
