package com.lattels.smalltour.dto.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 질문 작성 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWriteRequestDTO {

    @ApiParam("패키지 ID")
    private int tourId;

    @ApiParam("질문 제목")
    private String title;

    @ApiParam("질문 내용")
    private String content;

    @ApiParam("공개 여부")
    @JsonProperty("public")
    private boolean isPublic;

}