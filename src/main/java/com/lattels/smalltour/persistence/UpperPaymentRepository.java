package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.UpperPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UpperPaymentRepository extends JpaRepository<UpperPayment, Integer> {

    /*
    * Member에 맞는 Entity List 가져오기
    */
    Page<UpperPayment> findAllByGuideOrderByPayDay(Member member, Pageable pageable);




    @Query(value = "SELECT u FROM UpperPayment u " +
            "JOIN u.guide g JOIN u.tours t JOIN u.item i " +
            "WHERE g.id = t.guide.id AND g.role = 2 AND i.type = 1 AND t.approvals = 1 AND t.id = :tourId")
    Optional<UpperPayment> findByTourIdAndGuideRoleAndItemTypeAndApprovals(@Param("tourId") int tourId);


}
