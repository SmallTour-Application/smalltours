package com.lattels.smalltour.dto.question;

import io.swagger.annotations.ApiParam;
import lombok.*;

import java.util.List;

/**
 * 질문 목록 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionListDTO {

    @ApiParam("질문 수")
    private int count;

    @ApiParam("질문 목록")
    private List<QuestionDTO> content;

}