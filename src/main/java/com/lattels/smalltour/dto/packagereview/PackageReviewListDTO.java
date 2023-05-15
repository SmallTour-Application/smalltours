package com.lattels.smalltour.dto.packagereview;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 패키지 리뷰 목록 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageReviewListDTO {

    @ApiParam("전체 리뷰 개수")
    private int count;

    @ApiParam("리뷰 목록")
    private List<PackageReviewDTO> review;


}
