package com.lattels.smalltour.dto.packagereview;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 패키지 리뷰 작성 요청 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageReviewWriteRequestDTO {

    @ApiParam("패키지 ID")
    private int packageId;

    @ApiParam("리뷰 내용")
    private String content;

    @ApiParam("평점")
    private int rating;

}
