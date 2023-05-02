package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Payment_Schedule;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentScheduleRepository extends JpaRepository<Payment_Schedule, Integer> {
  
 
}
