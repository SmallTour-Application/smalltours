package com.lattels.smalltour.service.admin;


import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.admin.question.AdminQuestionDTO;
import com.lattels.smalltour.dto.admin.question.AdminQuestionListDTO;
import com.lattels.smalltour.model.Answer;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Question;
import com.lattels.smalltour.persistence.AnswerRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminQuestionService {
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${file.path}")
    private String filePath;

    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;

    @Value("${file.path.tours.images}")
    private String filePathToursImages;

    public File getQuestionDirectoryPath() {
        File file = new File(filePath);
        file.mkdirs();

        return file;
    }
    /**
     * 질문의 답변은 정환씨가 했던 QuestionAnswerService를 사용하면 됨
     */

    /**
     * 관리자 인지 체크
     */
    private void checkAdmin(final int adminId){
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자를 찾을수없습니다."));
        if(admin.getRole() != 3){
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }
    }

    /**
     * 전체 질문 게시글 가져오기
     */
    public AdminQuestionListDTO getQuestionList(int adminId, int isAnswer,int page, int count,String title,Integer month, Integer year){
        checkAdmin(adminId);
        Pageable pageable = PageRequest.of(page,count);

        // month와 year 유효성 검사
        if (month != null && (month < 1 || month > 12)) {
            throw new IllegalArgumentException("월의 범위를 벗어났습니다. (1~12 사이의 값을 입력해주세요)");
        }
        if (year != null && (year < 1900 || year > 2100)) {
            throw new IllegalArgumentException("연도의 범위를 벗어났습니다. (1900~2100 사이의 값을 입력해주세요)");
        }

        if(isAnswer == 0){ //답변이 존재하는 질문내역
            int questionCount = Long.valueOf(questionRepository.count()).intValue();


            Page<Question> questions = questionRepository.findMemberQuestionAnswer(pageable,title,month,year);

            if (questions.isEmpty()) {
                throw new IllegalArgumentException("해당 제목을 가진 질문이 없습니다.");
            }

            List<AdminQuestionDTO> questionDTOS = questions.stream()
                    .map(question -> new AdminQuestionDTO(question))
                    .collect(Collectors.toList());

            return AdminQuestionListDTO.builder()
                    .count(questionCount)
                    .content(questionDTOS)
                    .build();
        }else{ //답변이 존재하지않음
            int questionCount = Long.valueOf(questionRepository.count()).intValue();

            Page<Question> questions = questionRepository.findMemberQuestion(pageable,title,month,year);
            List<AdminQuestionDTO> questionDTOS = questions.stream()
                    .map(question -> new AdminQuestionDTO(question))
                    .collect(Collectors.toList());

            return AdminQuestionListDTO.builder()
                    .count(questionCount)
                    .content(questionDTOS)
                    .build();
        }

    }

    /**
     * 질문을 삭제합니다.
     * 이미지 파일도 같이 삭제됩니다.
     */
    public void deleteQuestion(int adminId, int questionId) {
        checkAdmin(adminId);

        Question question = questionRepository.findById(questionId).orElse(null);
        Preconditions.checkNotNull(question,"질문을 찾을 수 없습니다.",questionId);

        // 기존 이미지 삭제
        deleteQuestionImage(question);
        // 질문 삭제
        questionRepository.deleteById(question.getId());
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