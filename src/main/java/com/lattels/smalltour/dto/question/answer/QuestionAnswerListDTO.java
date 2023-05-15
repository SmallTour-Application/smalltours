package com.lattels.smalltour.dto.question.answer;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 질문 답변 목록 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerListDTO {

    @ApiParam("질문 수")
    private int count;

    @ApiParam("질문 목록")
    private List<QuestionAnswerDTO> content;

}