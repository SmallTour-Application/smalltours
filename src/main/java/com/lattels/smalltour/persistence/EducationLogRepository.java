package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.EducationLog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EducationLogRepository extends JpaRepository<EducationLog, EducationLog.EducationLogID> {

    /*
    * educationId와 guideId에 맞는 로그 가져오기
    */
    EducationLog findByEducationIdAndGuideId(int educationId, int guideId);


}

