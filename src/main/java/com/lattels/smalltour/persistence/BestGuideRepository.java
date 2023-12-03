package com.lattels.smalltour.persistence;


import com.lattels.smalltour.dto.bestguide.BestGuideCountDTO;
import com.lattels.smalltour.model.BestGuide;
import com.lattels.smalltour.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BestGuideRepository extends JpaRepository<BestGuide, Integer> {

    /**
     * 해당 가이드가 우수 가이드에 선정된 기록이 있는지
     * @param guideId 가이드 ID
     * @return boolean
     */
    boolean existsBestGuideByGuideId(int guideId);

    /**
     * ID로 Entity 찾기
     * @param id BestGuide ID
     * @return BestGuide Entity
     */
    BestGuide findById(int id);

    /**
     * 가이드별 우수 가이드 선정 횟수
     * @return Member Entity, 선정 횟수
     */
    @Query(value = "SELECT new com.lattels.smalltour.dto.bestguide.BestGuideCountDTO(bg.guide, COUNT(bg)) " +
            "FROM BestGuide bg " +
            "GROUP BY bg.guide " +
            "ORDER BY COUNT(bg) DESC ")
    Page<BestGuideCountDTO> countPerGuide(Pageable pageable);

    long countAllByGuide(Member guide);

}
