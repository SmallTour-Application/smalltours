package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.LogMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface LogMemberRepository extends JpaRepository<LogMember, Integer> {


    @Query(value = "SELECT lm.region, count(lm.region) FROM LogMember lm WHERE MONTH(lm.loginDateTime) =:month AND YEAR(lm.loginDateTime) =:year GROUP BY lm.region")
    List<Object[]> findBySearchRegionDay(@Param("month") int month,@Param("year") int year);

    @Query(value = "SELECT count(lm.region) FROM LogMember lm WHERE MONTH(lm.loginDateTime) =:month AND YEAR(lm.loginDateTime) =:year")
    Integer findByCountRegion(@Param("month") int month,@Param("year") int year);


    @Query(value = "SELECT lm.browser, count(lm.browser) FROM LogMember lm WHERE MONTH(lm.loginDateTime) =:month AND YEAR(lm.loginDateTime) =:year GROUP BY lm.browser")
    List<Object[]> findBySearchBrowserDay(@Param("month") int month,@Param("year") int year);

    @Query(value = "SELECT count(lm.browser) FROM LogMember lm WHERE MONTH(lm.loginDateTime) =:month AND YEAR(lm.loginDateTime) =:year")
    Integer findByCountBrowser(@Param("month") int month,@Param("year") int year);







}
