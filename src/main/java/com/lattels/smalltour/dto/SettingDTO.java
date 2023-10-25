package com.lattels.smalltour.dto;

import com.lattels.smalltour.model.Setting;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class SettingDTO {

    @ApiModelProperty(value = "아이디", example = "1")
    private int id;

    @ApiModelProperty(value = "상위노출 상품 가격", example = "1")
    private String upperPaymentPrice;

    @ApiModelProperty(value = "패키지 수수료", example = "1")
    private String packageCommission;

    @ApiModelProperty(value = "배너 수수료", example = "1")
    private String bannerCommission;

    public SettingDTO(Setting setting) {
        this.id = setting.getId();
        this.upperPaymentPrice = String.valueOf(setting.getUpperPaymentPrice());
        this.packageCommission = String.valueOf(setting.getPackageCommission());
        this.bannerCommission = String.valueOf(setting.getBannerCommission());
    }
}
