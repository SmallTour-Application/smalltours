package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Tours;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


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
    //+ 투어 생성일이 요청된 시작일과 종료일 사이에 있는 투어를 찾음
    @Query("SELECT t FROM Tours t, Locations l WHERE t.id = l.tours.id AND (l.locationName LIKE %:location% OR l.country LIKE %:location% OR l.region LIKE %:location%) AND t.minGroupSize <= :people AND t.maxGroupSize >= :people AND t.createdDay BETWEEN :start AND :end")
    Page<Tours> findToursBySearchParameters(@Param("location") String location, @Param("people") int people, @Param("start") LocalDate start, @Param("end") LocalDate end, Pageable pageable);

    // id로 tours Entity 가져오기
    Tours findById(int id);

    Tours findByIdAndGuideId(int id, int guideId);

}
