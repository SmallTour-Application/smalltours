package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.GuideProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface GuideProfileRepository extends JpaRepository<GuideProfile, Integer> {


    // guideId와 role 2에 해당하는 GuideProfile을 찾기
    //GuideProfile 테이블과 Member 테이블을 guide_id 및 id로 조인
    @Query("SELECT DISTINCT g FROM GuideProfile g JOIN Member m ON g.guideId = m.id WHERE m.id = :guideId AND m.role = 2")
    GuideProfile findByGuideIdAndRole(@Param("guideId") int guideId);


    @Query(value = "SELECT gp FROM GuideProfile gp, Member m WHERE gp.guideId = m.id AND m.id = :memberId AND m.role = 2 AND m.state = 1")
   GuideProfile findByGuideId(@Param("memberId")int memberId);
 
}
