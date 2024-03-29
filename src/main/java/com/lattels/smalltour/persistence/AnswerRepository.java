package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    /**
     * 회원의 질문 답변 개수를 불러옵니다.
     */
    long countByMemberId(int memberId);

    /**
     * 회원의 페이지에 맞는 답변들을 불러옵니다.
     */
    Page<Answer> findAllByMemberIdOrderByCreatedDayDesc(int memberId, Pageable pageable);

    @Query(value = "SELECT * FROM answer a JOIN question q ON a.question_id = q.id WHERE q.id =: questionId", nativeQuery = true)
    List<Answer> findByQuestionId(@Param("questionId") int questionId);
}
