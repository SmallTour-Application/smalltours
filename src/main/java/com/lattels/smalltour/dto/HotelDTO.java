package com.lattels.smalltour.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Data
@Builder
public class HotelDTO {

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "호텔 ID 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "호텔 ID", example = "1")
        private int id;

    }


    @Getter
    @NoArgsConstructor
    @ApiModel(value = "호텔 등록 요청 DTO")
    public static class AddRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 ID", example = "1")
        private int tourId;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "호텔 이름", example = "호텔 이름입니다")
        private String name;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "호텔 주소", example = "호텔 주소입니다")
        private String address;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "호텔 전화번호", example = "호텔 전화번호입니다")
        private String hotelTel;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "호텔 설명", example = "호텔 설명입니다")
        private String description;

        @ApiModelProperty(value = "호텔 경도", example = "12.12")
        private double hotelLocationX;

        @ApiModelProperty(value = "호텔 위도", example = "12.12")
        private double hotelLocationY;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "호텔 수정 요청 DTO")
    public static class UpdateRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "호텔 ID", example = "1")
        private int id;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "호텔 이름", example = "호텔 이름입니다")
        private String name;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "호텔 주소", example = "호텔 주소입니다")
        private String address;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "호텔 전화번호", example = "호텔 전화번호입니다")
        private String hotelTel;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "호텔 설명", example = "호텔 설명입니다")
        private String description;

        @ApiModelProperty(value = "호텔 경도", example = "12.12")
        private double hotelLocationX;

        @ApiModelProperty(value = "호텔 위도", example = "12.12")
        private double hotelLocationY;

    }


}
