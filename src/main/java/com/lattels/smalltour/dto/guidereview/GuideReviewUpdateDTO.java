package com.lattels.smalltour.dto.guidereview;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 가이드 리뷰 수정 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideReviewUpdateDTO {

    @ApiParam("리뷰 ID")
    private int id;

    @ApiParam("리뷰 평점")
    private int rating;

    @ApiParam("리뷰 내용")
    private String content;

}
