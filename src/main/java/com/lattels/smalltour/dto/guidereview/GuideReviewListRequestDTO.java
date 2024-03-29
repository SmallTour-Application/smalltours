package com.lattels.smalltour.dto.guidereview;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 가이드 리뷰 목록 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideReviewListRequestDTO {

    @ApiParam("가이드 ID")
    private int guideId;

    @ApiParam("조회할 페이지 (1부터 시작)")
    private int page;

}
