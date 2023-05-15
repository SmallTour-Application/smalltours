package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.question.answer.QuestionAnswerDTO;
import com.lattels.smalltour.dto.question.answer.QuestionAnswerListDTO;
import com.lattels.smalltour.dto.question.answer.QuestionAnswerUpdateRequestDTO;
import com.lattels.smalltour.dto.question.answer.QuestionAnswerWriteRequestDTO;
import com.lattels.smalltour.model.Answer;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Question;
import com.lattels.smalltour.persistence.AnswerRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QuestionAnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    private final MemberRepository memberRepository;

    /**
     * 내 질문 답변 목록을 반환합니다.
     * @param authentication 로그인 정보
     * @param page 페이지
     * @param countPerPage 페이지당 답변 개수
     * @return 내 답변 목록
     */
    public QuestionAnswerListDTO getMyAnswerList(Authentication authentication, int page, int countPerPage) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 페이지
        Pageable pageable = PageRequest.of(page, countPerPage);

        // 답변 개수
        int answerCount = Long.valueOf(answerRepository.countByMemberId(memberId)).intValue();

        // 답변 불러오기
        Page<Answer> answers = answerRepository.findAllByMemberIdOrderByCreatedDayDesc(memberId, pageable);
        List<QuestionAnswerDTO> questionAnswerDTOS = answers.stream()
                .map(answer -> new QuestionAnswerDTO(answer))
                .collect(Collectors.toList());

        // DTO 반환
        return QuestionAnswerListDTO.builder()
                .count(answerCount)
                .content(questionAnswerDTOS)
                .build();
    }

    /**
     * 답변을 작성합니다.
     * @param authentication 로그인 정보
     * @param questionAnswerWriteRequestDTO 답변 작성 요청 DTO
     * @return 생성된 답변 ID
     */
    public int writeAnswer(Authentication authentication, QuestionAnswerWriteRequestDTO questionAnswerWriteRequestDTO) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원 불러오기
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 질문 불러오기
        int questionId = questionAnswerWriteRequestDTO.getQuestionId();
        Question question = questionRepository.findById(questionId).orElse(null);
        Preconditions.checkNotNull(member, "질문을 찾을 수 없습니다. (질문 ID: %s)", questionId);

        // 답변 생성
        Answer answer = Answer.builder()
                .question(question)
                .memberId(memberId)
                .content(questionAnswerWriteRequestDTO.getContent())
                .createdDay(LocalDateTime.now())
                .build();

        // 답변 저장
        answerRepository.save(answer);

        // 생성된 답변 ID 반환
        return answer.getId();
    }

    /**
     * 답변을 수정합니다.
     * @param authentication 로그인 정보
     * @param questionAnswerUpdateRequestDTO 답변 수정 요청 DTO
     */
    public void updateAnswer(Authentication authentication, QuestionAnswerUpdateRequestDTO questionAnswerUpdateRequestDTO) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원 불러오기
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 답변 불러오기
        int answerId = questionAnswerUpdateRequestDTO.getAnswerId();
        Answer oldAnswer = answerRepository.findById(answerId).orElse(null);
        Preconditions.checkNotNull(oldAnswer, "답변 찾을 수 없습니다. (답변 ID: %s)", answerId);

        // 본인 체크
        Preconditions.checkArgument(memberId == oldAnswer.getMemberId(), "본인의 답변만 수정할 수 있습니다. (답변 ID: %s, 회원 ID: %s)", oldAnswer.getMemberId(), memberId);

        // 수정된 답변 생성
        Answer newAnswer = Answer.builder()
                .id(answerId)
                .question(oldAnswer.getQuestion())
                .memberId(oldAnswer.getMemberId())
                .content(questionAnswerUpdateRequestDTO.getContent())
                .createdDay(oldAnswer.getCreatedDay())
                .updateDay(LocalDateTime.now())
                .build();

        // 저장
        answerRepository.save(newAnswer);
    }

    /**
     * 답변을 삭제합니다.
     * @param authentication 로그인 정보
     * @param answerId 답변 ID
     */
    public void deleteAnswer(Authentication authentication, int answerId) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원 불러오기
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 답변 불러오기
        Answer answer = answerRepository.findById(answerId).orElse(null);
        Preconditions.checkNotNull(answer, "답변 찾을 수 없습니다. (답변 ID: %s)", answerId);

        // 본인 체크
        int answerWriterId = answer.getMemberId();
        Preconditions.checkArgument(memberId == answerWriterId || member.getRole() == MemberDTO.MemberRole.ADMIN, "해당 답변의 작성자가 아닙니다. (답변 ID: %s, 답변 작성자 ID: %s, 현재 회원 ID: %s)", answerId, answerWriterId, memberId);

        // 삭제
        answerRepository.delete(answer);
    }

}
