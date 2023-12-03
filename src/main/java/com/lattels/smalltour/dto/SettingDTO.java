package com.lattels.smalltour.dto;

import com.lattels.smalltour.model.Setting;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SettingDTO {

    @ApiModelProperty(value = "아이디", example = "1")
    private int id;

    @ApiModelProperty(value = "패키지 수수료", example = "0.0")
    private double packageCommission;

    public SettingDTO(Setting setting) {
        this.id = setting.getId();
        this.packageCommission = setting.getPackageCommission();
    }

}
