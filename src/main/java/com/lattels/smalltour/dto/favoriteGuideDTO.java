package com.lattels.smalltour.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class favoriteGuideDTO {
    private int guideId;
    private String guideName;
    private String guideImg;
}
