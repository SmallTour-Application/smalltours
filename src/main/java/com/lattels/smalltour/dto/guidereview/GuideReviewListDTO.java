package com.lattels.smalltour.dto.guidereview;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 가이드 리뷰 조회 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideReviewListDTO {

    @ApiParam("전체 평점 평균")
    private float avgRating;

    @ApiParam("리뷰 개수")
    private int count;

    @ApiParam("리뷰들")
    private List<GuideReviewDTO> reviews;

}
