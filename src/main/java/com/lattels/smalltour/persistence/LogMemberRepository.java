package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.LogMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface LogMemberRepository extends JpaRepository<LogMember, Integer> {


    @Query("SELECT lm.region, COUNT(lm.region) " +
            "FROM LogMember lm " +
            "WHERE (:startDay IS NULL OR lm.loginDateTime >= :startDay) " +
            "AND (:endDay IS NULL OR lm.loginDateTime <= :endDay) " +
            "GROUP BY lm.region")
    List<Object[]> findBySearchRegionDateRange(@Param("startDay") LocalDate startDay,
                                               @Param("endDay") LocalDate endDay);



    @Query(value = "SELECT COUNT(lm.region) FROM LogMember lm " +
            "WHERE (:startDay IS NULL OR lm.loginDateTime >= :startDay) " +
            "AND (:endDay IS NULL OR lm.loginDateTime <= :endDay)")
    Integer countByRegionBetweenDates(@Param("startDay") LocalDate startDay,
                                      @Param("endDay") LocalDate endDay);




    @Query(value = "SELECT COUNT(lm.browser) FROM LogMember lm " +
            "WHERE (:startDay IS NULL OR lm.loginDateTime >= :startDay) " +
            "AND (:endDay IS NULL OR lm.loginDateTime <= :endDay)")
    Integer countByBrowserBetweenDates(@Param("startDay") LocalDate startDay,
                                       @Param("endDay") LocalDate endDay);

    @Query(value = "SELECT lm.browser, COUNT(lm.region) " +
            "FROM LogMember lm " +
            "WHERE (:startDay IS NULL OR lm.loginDateTime >= :startDay) " +
            "AND (:endDay IS NULL OR lm.loginDateTime <= :endDay) " +
            "GROUP BY lm.browser")
    List<Object[]> findBrowserUsageBetweenDates(@Param("startDay") LocalDate startDay,
                                                @Param("endDay") LocalDate endDay);

}
