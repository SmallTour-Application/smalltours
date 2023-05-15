package com.lattels.smalltour.dto.packagereview;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 패키지 리뷰 목록 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageReviewListRequestDTO {

    @ApiParam("패키지 ID")
    private int packageId;

    @ApiParam("페이지")
    private int page;


}
