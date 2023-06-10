package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.UpperPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UpperPaymentRepository extends JpaRepository<UpperPayment, Integer> {

    /*
    * Member에 맞는 Entity List 가져오기
    */
    Page<UpperPayment> findAllByGuideOrderByPayDay(Member member, Pageable pageable);
}
