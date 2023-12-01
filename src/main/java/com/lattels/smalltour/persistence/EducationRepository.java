package com.lattels.smalltour.persistence;



import com.lattels.smalltour.model.Education;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface EducationRepository extends JpaRepository<Education, Integer> {


    /*
    * 가이드의 가입일 이후에 등록된 교육만 가져오기
    */
    @Query("SELECT e FROM Education e, Member m WHERE e.startDay > m.joinDay AND m.id = :memberId ORDER BY e.startDay")
    Page<Education> findAllByGuideJoinDay(@Param("memberId") int memberId, Pageable pageable);

    /*
     * id로 엔티티 가져오기
     */
    Education findById(int id);


    @Query("SELECT count(e) FROM Education e")
    long countByVideoId();

    @Query("SELECT e.id FROM Education e")
    List<Education> findByEducationId(int educationId);

    @Query("SELECT e.id FROM Education e WHERE e.id =:id")
    Education findEducationId(@Param("id") int id);

    @Query("SELECT e.id FROM Education e")
    List<Education> findByEducations();



    //강좌당 가이드가 얼마나 들었는지에 대한 통계
    @Query(value = "SELECT (COUNT(el.guide_id) * 100.0 / (SELECT COUNT(m.id) FROM member m WHERE m.role = 2)) AS completePercent " +
            "FROM education e " +
            "JOIN education_log el ON el.education_id = e.id " +
            "WHERE el.state = 1 AND el.education_id =:educationId",nativeQuery = true)
    int findCompletedEducation(@Param("educationId") int educationId);

    @Query(value = "SELECT e.title,e.start_day,e.end_day,e.id,e.state FROM education e WHERE e.state =:state AND (e.id = :educationId OR :educationId IS NULL)"
            , nativeQuery = true)
    List<Object[]> findByGuideEducationContent(Pageable pageable,@Param("state") int state,@Param("educationId") Integer educationId);


    @Query(value = "SELECT count(*) FROM education e WHERE e.state =:state AND (e.id = :educationId OR :educationId IS NULL)",nativeQuery = true)
    int countState(@Param("state") int state,@Param("educationId") Integer educationId);


    @Query(value = "SELECT e.title,e.start_day,e.end_day,el.state,el.completed_date FROM education e JOIN education_log el ON el.education_id = e.id JOIN member m ON el.guide_id = m.id WHERE el.state =:state AND el.guide_id=:guideId"
            , nativeQuery = true)
    List<Object[]> findByGuideEducationGuideContent(Pageable pageable,@Param("state") int state,@Param("guideId") int guideId);

    @Query(value = "SELECT count(*) FROM education_log el JOIN member m ON el.guide_id = m.id WHERE el.state =:state AND el.guide_id =:guideId",nativeQuery = true)
    int countStateEducationLog(@Param("state") int state,@Param("guideId") int memberId);


    /**
     * 이름, 기간으로 education 조회
     * */
    List<Education> findByTitleContainingAndStartDayGreaterThanEqualAndEndDayLessThanEqualOrderByEndDayDesc(String title, LocalDate startDay, LocalDate endDay, Pageable pageable);

    int countByTitleContainingAndStartDayGreaterThanEqualAndEndDayLessThanEqual(String title, LocalDate startDay, LocalDate endDay);

    /**
     * 이름으로만 조회
     * */
    List<Education> findByTitleContainingOrderByEndDayDesc(String title, Pageable pageable);

    int countByTitleContaining(String title);


}
