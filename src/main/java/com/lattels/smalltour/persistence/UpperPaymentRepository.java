package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.UpperPayment;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UpperPaymentRepository extends JpaRepository<UpperPayment, Integer> {


}
