package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.EducationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EducationLogRepository extends JpaRepository<EducationLog, EducationLog.EducationLogID> {

    /*
    * educationId와 guideId에 맞는 로그 가져오기
    */
    EducationLog findByEducationIdAndGuideId(int educationId, int guideId);




    /*
        object사용법
        @Query(value = "SELECT el.state,m.name, e.title,e.start_day,e.end_day FROM education_log el " +
            "JOIN education e on el.education_id = e.id " +
            "JOIN member m on el.guide_id = m.id " +
            "WHERE m.role = 2 AND el.state = 1", nativeQuery = true)
        List<Object[]> findByGuideEducationLog(Pageable pageable);

    */


    @Query(value = "SELECT e.title,e.start_day,e.end_day FROM education_log el " +
            "JOIN education e on el.education_id = e.id " +
            "JOIN member m on el.guide_id = m.id " +
            "WHERE m.role = 2 AND el.state = 1", nativeQuery = true)
    List<Object[]> findByGuideEducationLog(Pageable pageable);

}

