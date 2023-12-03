package com.lattels.smalltour.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PaymentRepositoryImpl implements PaymentRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> searchSalesByConditions(LocalDate startDay, LocalDate endDay, Integer sales, Integer state, String toursTitle) {
        StringBuilder queryString = new StringBuilder("SELECT DATE(p.paymentDay) AS paymentDate, COUNT(p) AS salesCount, SUM(p.price) FROM Payment p WHERE 1 = 1");

        if (startDay != null && endDay != null) {
            queryString.append(" AND p.paymentDay BETWEEN :startDay AND :endDay");
        }

        if (state != null) {
            queryString.append(" AND p.state = :state");
        }

        if (toursTitle != null && !toursTitle.equals("")) {
            queryString.append(" AND p.tours.title = :toursTitle");
        }

        if (sales != null) {
            queryString.append(" GROUP BY paymentDate HAVING COUNT(p) > :sales");
        }

        Query query = entityManager.createQuery(queryString.toString());

        if (startDay != null && endDay != null) {
            query.setParameter("startDay", startDay.atStartOfDay());
            query.setParameter("endDay", endDay.atStartOfDay());
        }

        if (state != null) {
            query.setParameter("state", state);
        }

        if (toursTitle != null && !toursTitle.equals("")) {
            query.setParameter("toursTitle", toursTitle);
        }
        if (sales != null) {
            query.setParameter("sales", (long)sales);
        }


        return query.getResultList();
    }
}
