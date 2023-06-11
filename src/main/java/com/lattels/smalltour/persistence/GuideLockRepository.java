package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.GuideLock;
import com.lattels.smalltour.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface GuideLockRepository extends JpaRepository<GuideLock, Integer> {


    @Query("SELECT g FROM GuideLock g WHERE g.guide.id = :guideId AND g.guide.role = 2 AND g.startDay <= :startDay AND g.endDay >= :endDay")
    GuideLock findGuideLockByGuideIdAndRoleAndPeriod(@Param("guideId") int guideId, @Param("startDay") LocalDate startDay, @Param("endDay") LocalDate endDay);


    //해당 가이드 특정 기간 날짜 가져오기
    //다른 회원들은 특정 기간동안 해당 가이드 사용 불가하기 위한 목적
    @Query("SELECT gl FROM GuideLock gl WHERE gl.guide.id = :guideId AND gl.startDay >= :startDay AND gl.endDay <= :endDay")
    GuideLock findGuideLockPeriod(
            @Param("guideId") int guideId,
            @Param("startDay") LocalDate startDay,
            @Param("endDay") LocalDate endDay
    );

    /**
     * 해당 가이드와 해당 기간 내의 가이드락 목록을 불러옵니다.
     */
    @Query("SELECT gl" +
            " FROM GuideLock gl" +
            " WHERE gl.guide.id = :guideId" +
            " AND (gl.startDay BETWEEN :startDay AND :endDay OR gl.endDay BETWEEN :startDay And :endDay)")
    List<GuideLock> findByGuideIdAndStartDayBetweenOrEndDayBetween(int guideId, LocalDate startDay, LocalDate endDay);

    /**
     * 해당 가이드와 해당 기간의 가이드락을 삭제합니다.
     */
    void deleteByGuideIdAndStartDayAndEndDay(int guideId, LocalDate startDay, LocalDate endDay);

    List<GuideLock> findAllByGuideOrderByStartDay(Member member);

    @Query("select g.guide from GuideLock g where g.guide =: guide")
    List<GuideLock> findByGuide(Member guide);

    List<GuideLock> findAllByGuideId(int guideId);
}
