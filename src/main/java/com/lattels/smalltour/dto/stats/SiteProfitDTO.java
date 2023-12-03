package com.lattels.smalltour.dto.stats;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SiteProfitDTO {

    @ApiModelProperty(value = "총 수익", example = "2")
    private int totalProfit;

    @ApiModelProperty(value = "배너 수익", example = "2")
    private int bannerProfit;

    @ApiModelProperty(value = "패키지 수익 (수수료)", example = "2")
    private int toursProfit;

    @ApiModelProperty(value = "상위노출 상품 수익", example = "2")
    private int upperPaymentProfit;

    public SiteProfitDTO(int toursProfit, int upperPaymentProfit, int bannerProfit) {
        this.bannerProfit = bannerProfit;
        this.toursProfit = toursProfit;
        this.upperPaymentProfit = upperPaymentProfit;
        this.totalProfit = bannerProfit + toursProfit + upperPaymentProfit;
    }
}
