package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface QuestionRepository extends JpaRepository<Question, Integer> {

    /**
     * 페이지에 맞는 질문들을 최근순으로 불러옵니다.
     */
    Page<Question> findAllByOrderByCreatedDayDesc(Pageable pageable);

    /**
     * 회원의 페이지에 맞는 질문들을 불러옵니다.
     */
    Page<Question> findAllByMemberIdOrderByCreatedDayDesc(int memberId, Pageable pageable);

    /**
     * 회원의 질문 개수를 불러옵니다.
     */
    long countByMemberId(int memberId);

    @Query(value = "SELECT * FROM question q JOIN member m on q.member_id = m.id order by q.created_day desc",nativeQuery = true)
    Page<Question> findMemberQuestion(Pageable pageable);

}
