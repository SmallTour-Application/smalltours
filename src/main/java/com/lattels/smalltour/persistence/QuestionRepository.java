package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;


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



    @Query(value = "SELECT q.* FROM question q JOIN member m ON q.member_id = m.id " +
            "WHERE q.id IN (SELECT a.question_id FROM answer a) " +
            "AND (:title IS NULL OR q.title LIKE CONCAT('%', :title, '%')) " +
            "AND (:month IS NULL OR MONTH(q.created_day) =:month) " +
            "AND (:year IS NULL OR YEAR(q.created_day) =:year) " +
            "ORDER BY q.created_day DESC", nativeQuery = true)
    Page<Question> findMemberQuestionAnswer(Pageable pageable, @Param("title") String title,Integer month, Integer year);


    @Query(value = "SELECT q.* FROM question q JOIN member m on q.member_id = m.id " +
            "WHERE q.id NOT IN (SELECT a.question_id FROM answer a) " +
            "AND (:title IS NULL OR q.title LIKE CONCAT('%', :title, '%')) " +
            "AND (:month IS NULL OR MONTH(q.created_day) =:month) " +
            "AND (:year IS NULL OR YEAR(q.created_day) =:year) " +
            "order by q.created_day desc",nativeQuery = true)
    Page<Question> findMemberQuestion(Pageable pageable,@Param("title") String title,Integer month, Integer year);

}
