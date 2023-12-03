package com.lattels.smalltour.persistence;




import com.lattels.smalltour.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;


public interface BannerRepository extends JpaRepository<Banner, Integer> {

    @Query(value = "SELECT SUM(b.item.price) " +
            "FROM Banner b " +
            "WHERE b.payDay >= :startDate AND b.payDay <= :endDate ")
    String sumPriceByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
