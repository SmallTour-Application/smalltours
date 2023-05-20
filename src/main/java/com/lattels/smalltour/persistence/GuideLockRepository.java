package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.GuideLock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface GuideLockRepository extends JpaRepository<GuideLock, Integer> {


    //해당 가이드 특정 기간 날짜 가져오기
    //다른 회원들은 특정 기간동안 해당 가이드 사용 불가하기 위한 목적
    @Query("SELECT gl FROM GuideLock gl WHERE gl.guideId = :guideId AND gl.startDay >= :startDay AND gl.endDay <= :endDay")
    GuideLock findGuideLockPeriod(
            @Param("guideId") int guideId,
            @Param("startDay") LocalDate startDay,
            @Param("endDay") LocalDate endDay
    );
}
