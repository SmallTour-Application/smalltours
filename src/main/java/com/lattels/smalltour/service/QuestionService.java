package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.question.QuestionDTO;
import com.lattels.smalltour.dto.question.QuestionListDTO;
import com.lattels.smalltour.dto.question.QuestionUpdateRequestDTO;
import com.lattels.smalltour.dto.question.QuestionWriteRequestDTO;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Question;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.AnswerRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.QuestionRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import com.lattels.smalltour.util.MultipartUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 패키지 질문 Service
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    private final MemberRepository memberRepository;
    private final ToursRepository toursRepository;

    @Value("${file.path.question}")
    private String filePath;

    public File getQuestionDirectoryPath() {
        File file = new File(filePath);
        file.mkdirs();

        return file;
    }

    /**
     * 질문 목록을 반환합니다.
     * memberId가 null이 아닐 경우 해당 회원이 작성한 질문은 공개 처리한 뒤 반환합니다.
     * @param authentication 로그인 정보
     * @param page 페이지
     * @param countPerPage 페이지당 질문 개수
     * @return 내 질문 목록
     */
    public QuestionListDTO getQuestionList(Integer memberId, int page, int countPerPage) {
        // 페이지
        Pageable pageable = PageRequest.of(page, countPerPage);

        // 질문 개수
        int questionCount = Long.valueOf(questionRepository.count()).intValue();

        // 질문 불러오기
        Page<Question> questions = questionRepository.findAllByOrderByCreatedDayDesc(pageable);
        List<QuestionDTO> questionDTOS = questions.stream()
                .map(question -> new QuestionDTO(question))
                .collect(Collectors.toList());

        // 로그인 했을 경우
        if (memberId != null) {
            // 내 질문들은 공개 처리
            questionDTOS.stream()
                    .filter(questionDTO -> questionDTO.getMemberId() == memberId)
                    .forEach(questionDTO -> questionDTO.setPublic(true));
        }

        // 비공개 질문 내용 비우기
        questionDTOS.stream()
                .filter(questionDTO -> !questionDTO.isPublic())
                .forEach(questionDTO -> {
                    questionDTO.setContent(null);
                    questionDTO.setAnswer(null);
                });

        // DTO 반환
        return QuestionListDTO.builder()
                .count(questionCount)
                .content(questionDTOS)
                .build();
    }

    /**
     * 내 질문 목록을 반환합니다.
     * 내가 작성한 질문은 공개 처리한 후 반환합니다.
     * @param authentication 로그인 정보
     * @param page 페이지
     * @param countPerPage 페이지당 질문 개수
     * @return 내 질문 목록
     */
    public QuestionListDTO getMyQuestionList(Authentication authentication, int page, int countPerPage) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 페이지
        Pageable pageable = PageRequest.of(page, countPerPage);

        // 질문 개수
        int questionCount = Long.valueOf(questionRepository.countByMemberId(memberId)).intValue();

        // 질문 불러오기
        Page<Question> questions = questionRepository.findAllByMemberIdOrderByCreatedDayDesc(memberId, pageable);
        List<QuestionDTO> questionDTOS = questions.stream()
                .map(question -> new QuestionDTO(question))
                .collect(Collectors.toList());

        // 비공개 질문 내용 비우기
        questionDTOS.stream()
                .filter(questionDTO -> !questionDTO.isPublic())
                .forEach(questionDTO -> {
                    questionDTO.setContent(null);
                    questionDTO.setAnswer(null);
                });

        // DTO 반환
        return QuestionListDTO.builder()
                .count(questionCount)
                .content(questionDTOS)
                .build();
    }

    /**
     * 질문을 작성합니다.
     * @param authentication 로그인 정보
     * @param questionWriteRequestDTO 질문 작성 요청 DTO
     * @param imageFiles 이미지 파일
     * @return 생성된 질문 ID
     */
    public int writeQuestion(Authentication authentication, QuestionWriteRequestDTO questionWriteRequestDTO, List<MultipartFile> imageFiles) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원 불러오기
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 패키지 불러오기
        int tourId = questionWriteRequestDTO.getTourId();
        Tours tours = toursRepository.findById(tourId).orElse(null);
        Preconditions.checkNotNull(member, "패키지를 찾을 수 없습니다. (패키지 ID: %s)", tourId);

        // 질문 저장
        Question question = Question.builder()
                .member(member)
                .tours(tours)
                .title(questionWriteRequestDTO.getTitle())
                .content(questionWriteRequestDTO.getContent())
                .isPublic(questionWriteRequestDTO.isPublic())
                .image(null)
                .createdDay(LocalDateTime.now())
                .build();

        questionRepository.save(question);

        // 생성된 질문 ID
        int questionId = question.getId();

        // 이미지가 있을 경우 저장
        if (imageFiles != null && !imageFiles.isEmpty()) {
            // 이미지 저장 한개만 지원 (DB에 이미지 경로가 하나임)
            MultipartFile imageFile = imageFiles.get(0);

            // 이미지 저장
            saveQuestionImage(question, memberId, imageFile);
        }

        // 질문 ID 반환
        return questionId;
    }

    /**
     * 질문 내용을 수정합니다.
     * @param authentication 로그인 정보
     * @param questionUpdateRequestDTO 질문 수정 요청 DTO
     * @param imageFiles 이미지 파일
     */
    public void updateQuestion(Authentication authentication, QuestionUpdateRequestDTO questionUpdateRequestDTO, List<MultipartFile> imageFiles) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원 불러오기
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 질문 불러오기
        int questionId = questionUpdateRequestDTO.getQuestionId();
        Question oldQuestion = questionRepository.findById(questionId).orElse(null);
        Preconditions.checkNotNull(member, "질문을 찾을 수 없습니다. (질문 ID: %s)", questionId);

        // 질문 데이터 갱신
        Question question = Question.builder()
                .id(questionId)
                .member(member)
                .tours(oldQuestion.getTours())
                .title(questionUpdateRequestDTO.getTitle())
                .content(questionUpdateRequestDTO.getContent())
                .isPublic(questionUpdateRequestDTO.isPublic())
                .image(oldQuestion.getImage())
                .createdDay(LocalDateTime.now())
                .build();

        // 이미지가 있을 경우 저장
        if (imageFiles != null && !imageFiles.isEmpty()) {
            // 이미지 저장 한개만 지원 (DB에 이미지 경로가 하나임)
            MultipartFile imageFile = imageFiles.get(0);

            // 이미지 저장
            saveQuestionImage(question, memberId, imageFile);
        }

        // 질문 데이터 저장
        questionRepository.save(question);
    }

    /**
     * 질문을 삭제합니다.
     * 이미지 파일도 같이 삭제됩니다.
     * @param authentication 로그인 정보
     * @param questionId 질문 ID
     */
    public void deleteQuestion(Authentication authentication, int questionId) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원 불러오기
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 질문 불러오기
        Question question = questionRepository.findById(questionId).orElse(null);
        Preconditions.checkNotNull(member, "질문을 찾을 수 없습니다. (질문 ID: %s)", questionId);

        // 기존 이미지 삭제
        deleteQuestionImage(question);

        // 질문 삭제
        questionRepository.deleteById(question.getId());
    }

    /**
     * 질문의 이미지를 저장하고, 이미지 경로를 DB에 저장합니다.
     */
    public void saveQuestionImage(Question question, int memberId, MultipartFile multipartFile) {
        // 기존 이미지 삭제
        if (question.getImage() != null) {
            deleteQuestionImage(question);
        }

        // 질문 이미지 폴더 가져오기
        File directoryPath = getQuestionDirectoryPath();

        // 이미지 저장 (파일명: "질문 ID-회원 ID.확장자")
        File newFile = MultipartUtils.saveImage(multipartFile, directoryPath, question.getId() + "-" + memberId);

        // 이미지 파일명 DB에 저장
        question.setImage(newFile.getName());
        questionRepository.save(question);
    }

    /**
     * 질문의 이미지를 삭제합니다.
     */
    public void deleteQuestionImage(Question question) {
        // 질문 이미지 폴더 가져오기
        File directoryPath = getQuestionDirectoryPath();

        // 이미지 삭제
        String imagePath = question.getImage();
        if (imagePath != null) {
            File oldImageFile = new File(directoryPath, imagePath);
            oldImageFile.delete();
        }
    }

}
