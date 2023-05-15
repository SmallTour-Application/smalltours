package com.lattels.smalltour.dto.question.answer;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 질문 답변 수정 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerUpdateRequestDTO {

    @ApiParam("답변 ID")
    private int answerId;

    @ApiParam("답변 내용")
    private String content;

}
