package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;

import org.springframework.data.domain.Page;
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

    // 지역명(도시,지역,나라)이(가) 포함된 투어 검색(location테이블이랑 조인)
    //+ 그룹 사이즈가 요청된 사람 수 내에 있는 투어 찾음
    //+ guide_lock 에 start_day, end_day 에 적혀있는 날짜 외인경우에만 검색가능
    //5.29일 수정.
    //guideLock기간 이 포함되면 패키지 검색 안됨.(5/12~5/15일 락걸림->5/12~5/16일 이렇게 검색하면 패키지 검색돼서 이 부분 수정)
    //가이드 락을 피해도 해당 상품이 5월 31일날 만들어졌는데 가이드 락만 피하면 검색이 되는 문제 발생(31일날 만들어진 상품 5/16~5/17로 기간 정하고검색하면 패키지나옴)
    // -> 가이드 락 피하고 + Tours에 createdDay를 기준으로 start되어야 해당 패키지상품이 검색 되도록 상품 검색 하게 최종수정
    //end는 딱히 기준이 없어서 안함, guideLock이 정해지면 그기간 동안 해당 패키지 검색 안되게 하면되니까 상관없을것같음.

/*    @Query(value = "SELECT t.* FROM Tours t JOIN Locations l ON t.id = l.tour_id LEFT JOIN Guide_Lock g ON t.guide_id = g.guide_id WHERE (l.location_name LIKE %:location% OR l.country LIKE %:location% OR l.region LIKE %:location%) AND t.min_group_size <= :people AND t.max_group_size >= :people AND (g.guide_id IS NULL OR NOT ((:start <= g.end_day AND :end >= g.start_day) OR (:end <= g.end_day AND :start >= g.start_day))) AND t.approvals = 1 AND (:startDay <= t.created_day)", nativeQuery = true)
    Page<Tours> findToursBySearchParameters(@Param("location") String location, @Param("people") int people, @Param("start") LocalDate start, @Param("end") LocalDate end, @Param("startDay") LocalDateTime startDay, Pageable pageable);*/

    @Query(value = "SELECT t.* FROM Tours t JOIN Locations l ON t.id = l.tour_id LEFT JOIN Guide_Lock g ON t.guide_id = g.guide_id WHERE (l.location_name LIKE %:location% OR l.country LIKE %:location% OR l.region LIKE %:location%) AND t.min_group_size <= :people AND t.max_group_size >= :people AND (g.guide_id IS NULL OR (:start > g.end_day OR :end < g.start_day)) AND t.approvals = 1 AND NOT (:startDay < t.created_day)", nativeQuery = true)
    Page<Tours> findToursBySearchParameters(@Param("location") String location, @Param("people") int people, @Param("start") LocalDate start, @Param("end") LocalDate end, @Param("startDay") LocalDateTime startDay, Pageable pageable);





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

}
