package com.lattels.smalltour.persistence;



import com.lattels.smalltour.model.Education;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


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
 
}
