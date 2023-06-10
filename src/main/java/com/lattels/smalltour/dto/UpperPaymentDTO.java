package com.lattels.smalltour.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@AllArgsConstructor
@Data
@Builder
public class UpperPaymentDTO {

    @Data
    @NoArgsConstructor
    @ApiModel(value = "결제 요청 DTO")
    public static class AddRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "아이템 아이디", example = "1")
        private int itemId;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 아이디", example = "1")
        private int toursId;


    }

}
