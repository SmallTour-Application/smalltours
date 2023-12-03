package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepositoryCustom {
    List<Object[]> searchSalesByConditions(LocalDate startDay, LocalDate endDay, Integer sales, Integer state, String toursTitle);

}
