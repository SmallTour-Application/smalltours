package com.lattels.smalltour.dto.stats;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TotalVolumePercentageDTO {

    @ApiModelProperty(value = "투어 ID", example = "1")
    private int tourId;

    @ApiModelProperty(value = "투어명", example = "투어명")
    private String title;

    @ApiModelProperty(value = "가이드 이름", example = "이름")
    private String guideName;

    @ApiModelProperty(value = "가이드 닉네임", example = "닉네임")
    private String guideNickname;

    @ApiModelProperty(value = "가격", example = "1")
    private int price;

    @ApiModelProperty(value = "판매량", example = "1")
    private long salesVolume;

    @ApiModelProperty(value = "퍼센테이지", example = "30.22")
    private double percentage;

    public TotalVolumePercentageDTO(int tourId, String title, String guideName, String guideNickname, int price, long salesVolume) {
        this.tourId = tourId;
        this.title = title;
        this.guideName = guideName;
        this.guideNickname = guideNickname;
        this.price = price;
        this.salesVolume = salesVolume;
    }
}
