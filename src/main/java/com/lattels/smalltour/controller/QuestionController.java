package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.question.QuestionListDTO;
import com.lattels.smalltour.dto.question.QuestionUpdateRequestDTO;
import com.lattels.smalltour.dto.question.QuestionWriteRequestDTO;
import com.lattels.smalltour.security.TokenProvider;
import com.lattels.smalltour.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Api(description = "패키지 질문 API 컨트롤러")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/package/question")
public class QuestionController {

    // 페이지당 조회할 질문 수
    private static final int NUMBER_OF_QUESTION_PER_PAGE = 10;

    private final QuestionService questionService;
    private final TokenProvider tokenProvider;

    @ApiOperation("질문 목록 조회")
    @GetMapping("/unauth/list")
    public ResponseEntity<QuestionListDTO> getQuestionList(HttpServletRequest request, int page) {
        Integer userId = null;

        // 로그인 되어있을 경우 본인글 보이게 JWT로 userId 불러옴
        String token = request.getHeader("Authorization");
        if (token != null) {
            String userIdString = tokenProvider.validateAndGetUserId(token);
            if (userIdString != null) {
                userId = Integer.parseInt(userIdString);
            }
        }

        // 질문 목록 조회
        QuestionListDTO questionListDTO = questionService.getQuestionList(userId, page - 1, NUMBER_OF_QUESTION_PER_PAGE);

        return ResponseEntity.ok(questionListDTO);
    }

    @ApiOperation("내 질문 목록 조회")
    @PostMapping("/list")
    public ResponseEntity<QuestionListDTO> getMyQuestionList(@ApiIgnore Authentication authentication, int page) {
        QuestionListDTO questionListDTO = questionService.getMyQuestionList(authentication, page - 1, NUMBER_OF_QUESTION_PER_PAGE);

        return ResponseEntity.ok(questionListDTO);
    }

    @ApiOperation("패키지 질문 작성")
    @PostMapping("/write")
    public ResponseEntity<Integer> writeQuestion(
            @ApiIgnore Authentication authentication,
            QuestionWriteRequestDTO questionWriteRequestDTO,
            @RequestPart(value = "image", required = false) MultipartFile imageFiles
    ) {
        int questionId = questionService.writeQuestion(authentication, questionWriteRequestDTO, Arrays.asList(imageFiles));

        return ResponseEntity.ok(questionId);
    }

    @ApiOperation("패키지 질문 수정")
    @PostMapping("/update")
    public ResponseEntity<?> updateQuestion(
            @ApiIgnore Authentication authentication,
            QuestionUpdateRequestDTO questionUpdateRequestDTO,
            @RequestPart(value = "image", required = false) MultipartFile imageFiles
    ) {
        questionService.updateQuestion(authentication, questionUpdateRequestDTO, Arrays.asList(imageFiles));

        return ResponseEntity.ok().build();
    }

    @ApiOperation("패키지 질문 삭제")
    @PostMapping("/delete")
    public ResponseEntity<?> deleteQuestion(@ApiIgnore Authentication authentication, int questionId) {
        questionService.deleteQuestion(authentication, questionId);

        return ResponseEntity.ok().build();
    }

}