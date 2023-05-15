package com.lattels.smalltour.dto.question.answer;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 질문 답변 작성 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerWriteRequestDTO {

    @ApiParam("질문 ID")
    private int questionId;

    @ApiParam("답변 내용")
    private String content;

}