package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ToursRepository extends JpaRepository<Tours, Integer> {

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

    @Query("SELECT t FROM Tours t WHERE t.guide = :guide")
    List<Tours> findByGuide(@Param("guide") Member guide);


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


    @Query(value = "SELECT t.* FROM Tours t JOIN Locations l ON t.id = l.tour_id LEFT JOIN Guide_Lock g ON t.guide_id = g.guide_id WHERE (l.location_name LIKE %:location% OR l.country LIKE %:location% OR l.region LIKE %:location% OR t.title LIKE %:title%) AND t.min_group_size <= :people AND t.max_group_size >= :people AND ((g.guide_id IS NULL) OR (g.id IS NOT NULL AND ((:start NOT BETWEEN g.start_day AND g.end_day) OR (:end NOT BETWEEN g.start_day AND g.end_day)))) AND t.approvals = 1 AND (:startDay >= t.created_day)", nativeQuery = true)
    Page<Tours> findToursKeywordBySearchParameters(@Param("title") String title,@Param("location") String location, @Param("people") int people, @Param("start") LocalDate start, @Param("end") LocalDate end, @Param("startDay") LocalDateTime startDay, Pageable pageable);




}
