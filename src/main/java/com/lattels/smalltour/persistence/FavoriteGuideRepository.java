package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.FavoriteGuide;

import com.lattels.smalltour.model.FavoriteTour;
import com.lattels.smalltour.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface FavoriteGuideRepository extends JpaRepository<FavoriteGuide, FavoriteGuide.FavoriteGuideID> {


    //특정 guide_id에 대해 연결된 모든 member_id의 수를 셈
    //좋아요(가이드에대해)
    @Query("SELECT COUNT(DISTINCT fg.member.id) FROM FavoriteGuide fg WHERE fg.guide.id = :guideId AND fg.guide.role = 2 AND fg.member.role = 0")
    long countByGuideId(@Param("guideId") int guideId);

    @Query("SELECT fg FROM FavoriteGuide fg WHERE fg.member = :member AND fg.guide.role = 2 AND fg.state = 1")
    Page<FavoriteGuide> findByMemberAndGuideRole(@Param("member")Member member, Pageable pageable);


    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FavoriteGuide f WHERE f.member = :member AND  f.guide = :guide AND f.datePressed !=null")
    boolean existsByMemberAndGuide(@Param("member") Member member, @Param("guide") Member guide);

    @Query("SELECT f FROM FavoriteGuide f WHERE f.member = :member AND f.guide = :guide")
    FavoriteGuide findByMemberAndGuide(@Param("member") Member member, @Param("guide") Member guide);

    @Query(value = "SELECT count(distinct fg.guide) FROM FavoriteGuide fg")
    Integer findByCountGuide();

    /**
     *년,월을 입력하면  해당 월 의 좋아요 수 - 그 전의 월 좋아요 수 를 구해서 증감값 확인
     */
    @Query(value = "SELECT " +
            "fg.guide_id, " +
            "IFNULL(m.name, 'Unknown') AS name, " +
            "SUM(CASE WHEN fg.state = 1 THEN 1 ELSE 0 END) AS total_likes, " +
            "SUM(CASE WHEN fg.state = 0 THEN 1 ELSE 0 END) AS total_cancel, " +
            "COUNT(fg.guide_id) AS total_count, " +
            "(SUM(CASE WHEN fg.state = 1 AND MONTH(fg.date_pressed) = :month AND YEAR(fg.date_pressed) = :year THEN 1 ELSE 0 END) - " +
            " SUM(CASE WHEN fg.state = 1 AND MONTH(fg.date_pressed) = :previousMonth AND YEAR(fg.date_pressed) = :previousYear THEN 1 ELSE 0 END)) " +
            "AS likes_difference " +
            "FROM favorite_guide fg " +
            "JOIN member m ON fg.guide_id = m.id " +
            "WHERE m.name LIKE %:name% " +
            "GROUP BY fg.guide_id, m.name " , nativeQuery = true)
    List<Object[]> findLikesDifferenceAndTotalByMonthAndYear(
            @Param("month") int month,
            @Param("year") int year,
            @Param("previousMonth") int previousMonth,
            @Param("previousYear") int previousYear,
            @Param("name") String name,
            Pageable pageable);
}

