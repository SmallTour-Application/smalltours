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
public class GuideReviewWriteDto {

    @ApiParam("리뷰 대상 가이드 ID")
    private int guideId;

    @ApiParam("리뷰 평점")
    private int rating;

    @ApiParam("리뷰 내용")
    private String content;

}
