package com.lattels.smalltour.persistence;




import com.lattels.smalltour.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BannerPaymentRepository extends JpaRepository<Banner, Integer> {

 
}
