package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Integer> {
  
 
}
