package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.question.answer.QuestionAnswerListDTO;
import com.lattels.smalltour.dto.question.answer.QuestionAnswerUpdateRequestDTO;
import com.lattels.smalltour.dto.question.answer.QuestionAnswerWriteRequestDTO;
import com.lattels.smalltour.service.QuestionAnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "패키지 질문 답변 API 컨트롤러")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/package/question/answer")
public class QuestionAnswerController {

    // 페이지당 조회할 답변 수
    private static final int NUMBER_OF_ANSWER_PER_PAGE = 10;

    private final QuestionAnswerService questionAnswerService;

    @ApiOperation("내 답변 목록 조회")
    @PostMapping("/mylist")
    public ResponseEntity<QuestionAnswerListDTO> getMyAnswerList(@ApiIgnore Authentication authentication, int page) {
        QuestionAnswerListDTO questionAnswerListDTO = questionAnswerService.getMyAnswerList(authentication, page - 1, NUMBER_OF_ANSWER_PER_PAGE);

        return ResponseEntity.ok(questionAnswerListDTO);
    }

    @ApiOperation("답변 작성")
    @PostMapping("/write")
    public ResponseEntity<Integer> writeAnswer(@ApiIgnore Authentication authentication, QuestionAnswerWriteRequestDTO questionAnswerWriteRequestDTO) {
        int answerId = questionAnswerService.writeAnswer(authentication, questionAnswerWriteRequestDTO);

        return ResponseEntity.ok(answerId);
    }

    @ApiOperation("답변 수정")
    @PostMapping("/update")
    public ResponseEntity<?> updateAnswer(@ApiIgnore Authentication authentication, QuestionAnswerUpdateRequestDTO questionAnswerWriteRequestDTO) {
        questionAnswerService.updateAnswer(authentication, questionAnswerWriteRequestDTO);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("답변 삭제")
    @PostMapping("/delete")
    public ResponseEntity<?> deleteAnswer(@ApiIgnore Authentication authentication, int answerId) {
        questionAnswerService.deleteAnswer(authentication, answerId);

        return ResponseEntity.ok().build();
    }

}
