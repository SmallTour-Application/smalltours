package com.lattels.smalltour.persistence;




import com.lattels.smalltour.model.BannerPayment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BannerPaymentRepository extends JpaRepository<BannerPayment, Integer> {
  
 
}
