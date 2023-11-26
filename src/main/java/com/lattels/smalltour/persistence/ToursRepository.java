package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ToursRepository extends JpaRepository<Tours, Integer> {

    //findByMemberId with pageable
    //해당 멤버가 올린 tour 리스트 가져오기
    @Query("SELECT t FROM Tours t JOIN t.guide m WHERE m.id = :memberId AND m.role = 0")
    List<Tours> findByMemberId(@Param("memberId") int memberId, Pageable pageable);



    List<Tours> findAll();

    /**
     * 해당 페이지에 맞는 가이드의 패키지 목록을 최신순으로 불러옵니다.
     */
    Page<Tours> findAllByGuideIdOrderByCreatedDayDesc(int guideId, Pageable pageable);

    /**
     * 해당 페이지에 맞는 가이드의 패키지 목록을 등록여부순으로 불러옵니다.
     */
    Page<Tours> findAllByGuideIdOrderByApprovalsDesc(int guideId, Pageable pageable);

    /**
     * 가이드의 패키지 개수를 불러옵니다.
     */
    long countAllByGuideId(int guideId);


    @Query(value = "SELECT distinct t.* FROM Tours t JOIN Locations l ON t.id = l.tour_id LEFT JOIN Guide_Lock g ON t.guide_id = g.guide_id WHERE (l.location_name LIKE %:location% OR l.country LIKE %:location% OR l.region LIKE %:location%) AND t.min_group_size <= :people AND t.max_group_size >= :people AND" +
            "((g.guide_id IS NULL AND (DATEDIFF(:end, :start) + 1 = t.duration)) OR (g.guide_id IS NOT NULL AND ((DATEDIFF(:end, :start) + 1 = t.duration) AND (:start > g.end_day OR :end < g.start_day)))) AND t.approvals = 1", nativeQuery = true)
    Page<Tours> findToursBySearchParameters(@Param("location") String location, @Param("people") int people, @Param("start") LocalDate start, @Param("end") LocalDate end, Pageable pageable);



    // id로 tours Entity 가져오기
    @Query(value = "SELECT * FROM tours WHERE id = :id", nativeQuery = true)
    Tours findByToursId(@Param("id") int id);


    Tours findByIdAndGuideId(int id, int guideId);



    // guideId와 role 2에 해당하는 가이드가 진행하는 모든 투어(Tours)를 찾기
    @Query("SELECT t FROM Tours t JOIN t.guide m WHERE m.id = :guideId AND m.role = 2")
    List<Tours> findAllByGuideId(@Param("guideId") int guideId);

    @Query("SELECT t FROM Tours t WHERE t.guide.id = :guideId AND t.approvals = 1")
    List<Tours> findByGuideAndApprovals(@Param("guideId") int guideId);


    //가이드 role2, 상품은 승인받아야함(1)
    @Query("SELECT t FROM Tours t JOIN t.guide g WHERE g.id = :guideId AND g.role = 2 AND t.approvals = 1")
    Page<Tours> findByGuideIdAndRoleAndApproval(@Param("guideId") int guideId, Pageable pageable);

    //해당 가이드가 올린 상품 갯수

    @Query("SELECT COUNT(t) FROM Tours t WHERE t.guide.id = :guideId AND t.guide.role = 2")
    long countByGuideId(@Param("guideId") int guideId);

    /*
     * 가이드가 올린 투어 모든 상태 가져오기
     */
    Page<Tours> findAllByGuideOrderByCreatedDayDesc(Member guide, Pageable pageable);



/*
    @Query(value = "SELECT distinct t.* FROM Tours t JOIN Locations l ON t.id = l.tour_id LEFT JOIN Guide_Lock g ON t.guide_id = g.guide_id WHERE (l.location_name LIKE %:location% OR l.country LIKE %:location% OR l.region LIKE %:location% OR t.title LIKE %:title%)" +
                    " AND t.min_group_size <= :people AND t.max_group_size >= :people AND ((g.guide_id IS NULL AND (DATEDIFF(:end, :start) + 1 = t.duration)) OR (g.guide_id IS NOT NULL AND ((:start NOT BETWEEN g.start_day AND g.end_day) OR (:end NOT BETWEEN g.start_day AND g.end_day)) AND (DATEDIFF(:end, :start) + 1 = t.duration))) AND t.approvals = 1 AND (:startDay >= t.created_day)", nativeQuery = true)
    Page<Tours> findToursKeywordBySearchParameters(@Param("title") String title,@Param("location") String location, @Param("people") int people, @Param("start") LocalDate start, @Param("end") LocalDate end, @Param("startDay") LocalDateTime startDay, Pageable pageable);
*/

/*
    @Query(value = "SELECT t.* FROM Tours t JOIN Locations l ON t.id = l.tour_id LEFT JOIN Guide_Lock g ON t.guide_id = g.guide_id WHERE (l.location_name LIKE %:location% OR l.country LIKE %:location% OR l.region LIKE %:location% OR t.title LIKE %:title%) AND t.min_group_size <= :people AND t.max_group_size >= :people AND ((g.guide_id IS NULL)" +
            " OR (g.id IS NOT NULL AND ((:start NOT BETWEEN g.start_day AND g.end_day) OR (:end NOT BETWEEN g.start_day AND g.end_day)))) AND t.approvals = 1 AND (:startDay >= t.created_day)", nativeQuery = true)
    Page<Tours> findToursKeywordBySearchParameters(@Param("title") String title,@Param("location") String location, @Param("people") int people, @Param("start") LocalDate start, @Param("end") LocalDate end, @Param("startDay") LocalDateTime startDay, Pageable pageable);
*/

    @Query(value = "SELECT distinct t.* FROM Tours t JOIN Locations l ON t.id = l.tour_id LEFT JOIN Guide_Lock g ON t.guide_id = g.guide_id WHERE (l.location_name LIKE %:location% OR l.country LIKE %:location% OR l.region LIKE %:location% OR t.title LIKE %:keyword%) AND t.min_group_size <= :people AND t.max_group_size >= :people AND" +
            "((g.guide_id IS NULL AND (DATEDIFF(:end, :start) + 1 = t.duration)) OR (g.guide_id IS NOT NULL AND ((DATEDIFF(:end, :start) + 1 = t.duration) AND (:start > g.end_day OR :end < g.start_day)))) AND t.approvals = 1", nativeQuery = true)
    Page<Tours> findToursBySearchParameters(@Param("keyword") String keyword, @Param("location") String location, @Param("people") int people, @Param("start") LocalDate start, @Param("end") LocalDate end, Pageable pageable);

    @Query(value = "SELECT count(*) FROM tours ",nativeQuery = true)
    Integer countTourId();





    @Query(value = "SELECT t.id,t.title,t.created_day,t.update_day,t.approvals FROM tours t " +
            "WHERE (:month IS NULL OR MONTH(t.created_day) = :month) " +
            "AND (:year IS NULL OR YEAR(t.created_day) = :year) " +
            "AND (:tourId IS NULL OR t.id = :tourId) " +
            "AND (:state IS NULL OR t.approvals = :state) " +
            "AND (:title IS NULL OR t.title LIKE CONCAT('%', :title, '%')) ", nativeQuery = true)
    Page<Object[]> findByConditions(
            @Param("month") Integer month,
            @Param("year") Integer year,
            @Param("tourId") Integer tourId,
            @Param("state") Integer state,
            @Param("title") String title,
            Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM tours t " +
            "WHERE (:month IS NULL OR MONTH(t.created_day) =:month) " +
            "AND (:year IS NULL OR YEAR(t.created_day) =:year) " +
            "AND (:tourId IS NULL OR t.id =:tourId) " +
            "AND (:state IS NULL OR t.approvals =:state) " +
            "AND (:title IS NULL OR t.title LIKE CONCAT('%', :title, '%'))", nativeQuery = true)
    long countByConditions(
            @Param("month") Integer month,
            @Param("year") Integer year,
            @Param("tourId") Integer tourId,
            @Param("state") Integer state,
            @Param("title") String title);



    @Query(value = "SELECT t.id,t.title,t.created_day,t.update_day,t.approvals FROM tours t",nativeQuery = true)
    Page<Object[]> findByConditionALL(
            @Param("month") Integer month,
            @Param("year") Integer year,
            @Param("tourId") Integer tourId,
            @Param("state") Integer state,
            @Param("title") String title,
            Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM tours t " ,nativeQuery = true)
    long countByAllConditions(
            @Param("month") Integer month,
            @Param("year") Integer year,
            @Param("tourId") Integer tourId,
            @Param("state") Integer state,
            @Param("title") String title);

    // 타이틀만 업데이트하는 메서드
    @Modifying
    @Transactional
    @Query("UPDATE Tours t SET t.title = :title WHERE t.id = :id")
    void updateTourTitle(@Param("id") int id, @Param("title") String title);

    // 서브타이틀만 업데이트하는 메서드
    @Modifying
    @Transactional
    @Query("UPDATE Tours t SET t.subTitle = :subtitle WHERE t.id = :id")
    void updateTourSubtitle(@Param("id") int id, @Param("subtitle") String subtitle);

    // 설명만 업데이트하는 메서드
    @Modifying
    @Transactional
    @Query("UPDATE Tours t SET t.description = :description WHERE t.id = :id")
    void updateTourDescription(@Param("id") int id, @Param("description") String description);

    @Modifying
    @Transactional
    @Query("UPDATE Tours t SET t.meetingPoint = :meetingPoint WHERE t.id = :id")
    void updateTourMeetingPoing(@Param("id") int id, @Param("meetingPoint") String meetingPoint);

    @Modifying
    @Transactional
    @Query("UPDATE Tours t SET t.maxGroupSize = :maxGroupSize WHERE t.id = :id")
    void updateTourMaxGroupSize(@Param("id") int id, @Param("maxGroupSize") Integer maxGroupSize);
    @Modifying
    @Transactional
    @Query("UPDATE Tours t SET t.minGroupSize = :minGroupSize WHERE t.id = :id")
    void updateTourMinGroupSize(@Param("id") int id, @Param("minGroupSize") Integer minGroupSize);

    @Modifying
    @Transactional
    @Query("UPDATE Tours t SET t.title = :title, t.subTitle = :subtitle, t.description = :description, t.meetingPoint = :meetingPoint,t.maxGroupSize = :maxGroupSize,t.minGroupSize = :minGroupSize WHERE t.id = :id")
    void updateTourDetails(@Param("id") int id,
                           @Param("title") String title,
                           @Param("subtitle") String subtitle,
                           @Param("description") String description,
                           @Param("meetingPoint") String meetingPoint,
                           @Param("maxGroupSize") Integer maxGroupSize,
                           @Param("minGroupSize") Integer minGroupSize);

    @Modifying
    @Transactional
    @Query("UPDATE Tours t SET t.approvals = 3 WHERE t.id = :id")
    void deleteTourById(@Param("id") int id);

}
