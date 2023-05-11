package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.PaymentSchedule;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Integer> {
  
 
}
