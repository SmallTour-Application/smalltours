package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
    @Query(value = "SELECT t.* FROM Tours t JOIN Locations l ON t.id = l.tour_id JOIN Guide_Lock g ON t.guide_id = g.guide_id WHERE (l.location_name LIKE %:location% OR l.country LIKE %:location% OR l.region LIKE %:location%) AND t.min_group_size <= :people AND t.max_group_size >= :people AND (:start BETWEEN g.start_day AND g.end_day AND :end BETWEEN g.start_day AND g.end_day) AND t.approvals = 1", nativeQuery = true)
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

}
