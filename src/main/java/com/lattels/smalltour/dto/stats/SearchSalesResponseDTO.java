package com.lattels.smalltour.dto.stats;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@AllArgsConstructor
@Data
@Builder
public class SearchSalesResponseDTO {

    @ApiModelProperty(value = "전체 판매량", example = "1")
    private int totalSalesVolume;

    @ApiModelProperty(value = "전체 매출액", example = "1")
    private int totalSales;

    @ApiModelProperty(value = "전체 패키지 수", example = "1")
    private int totalPackageCnt;

    @ApiModelProperty(value = "전체 가이드 리뷰 수", example = "1")
    private int totalGuideReviewCnt;

    @ApiModelProperty(value = "전체 가이드 평점", example = "1.1")
    private double totalGuideRating;

    @ApiModelProperty(value = "전체 패키지 리뷰 수", example = "1")
    private int totalPackageReviewCnt;

    @ApiModelProperty(value = "전체 패키지 평점", example = "1.1")
    private double totalPackageReviewRating;



    public SearchSalesResponseDTO(Object[] objects) {
        this.totalSalesVolume = objects[0] != null ? Integer.parseInt(String.valueOf(objects[0])) : 0;
        this.totalSales = objects[1] != null ? Integer.parseInt(String.valueOf(objects[1])) : 0;
        this.totalPackageCnt = objects[2] != null ? Integer.parseInt(String.valueOf(objects[2])) : 0;
        this.totalGuideReviewCnt = objects[3] != null ? Integer.parseInt(String.valueOf(objects[3])) : 0;
        this.totalGuideRating =  objects[4] != null ? Double.parseDouble(String.valueOf(objects[4])) : 0;
        this.totalPackageReviewCnt = objects[5] != null ? Integer.parseInt(String.valueOf(objects[5])) : 0;
        this.totalPackageReviewRating = objects[6] != null ? Double.parseDouble(String.valueOf(objects[6])) : 0;
    }
}
