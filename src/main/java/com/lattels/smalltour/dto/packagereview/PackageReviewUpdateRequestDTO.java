package com.lattels.smalltour.dto.packagereview;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 패키지 리뷰 수정 요청 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageReviewUpdateRequestDTO {

    @ApiParam("리뷰 ID")
    private int reviewId;

    @ApiParam("리뷰 내용")
    private String content;

    @ApiParam("평점")
    private int rating;

}
