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

import java.util.Optional;


public interface FavoriteGuideRepository extends JpaRepository<FavoriteGuide, FavoriteGuide.FavoriteGuideID> {


    //특정 guide_id에 대해 연결된 모든 member_id의 수를 셈
    //좋아요(가이드에대해)
    @Query("SELECT COUNT(DISTINCT fg.member.id) FROM FavoriteGuide fg WHERE fg.guide.id = :guideId AND fg.guide.role = 2 AND fg.member.role = 0")
    long countByGuideId(@Param("guideId") int guideId);

    @Query("SELECT fg FROM FavoriteGuide fg WHERE fg.member = :member AND fg.guide.role = 2")
    Page<FavoriteGuide> findByMemberAndGuideRole(@Param("member")Member member, Pageable pageable);


    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FavoriteGuide f WHERE f.member = :member AND f.guide = :guide")
    boolean existsByMemberAndGuide(@Param("member") Member member, @Param("guide") Member guide);

    @Query("SELECT f FROM FavoriteGuide f WHERE f.member = :member AND f.guide = :guide")
    Optional<FavoriteGuide> findByMemberAndGuide(@Param("member") Member member, @Param("guide") Member guide);

}
