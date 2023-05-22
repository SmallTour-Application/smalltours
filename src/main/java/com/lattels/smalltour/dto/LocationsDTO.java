package com.lattels.smalltour.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Data
@Builder
public class LocationsDTO {

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "투어 위치 정보 아이디 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 위치 정보 ID", example = "1")
        private int id;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "투어 위치 정보 등록 요청 DTO")
    public static class AddRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 ID", example = "1")
        private int tourId;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "위치 정보 이름", example = "위치 정보 이름입니다")
        private String locationName;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "국가", example = "국가입니다")
        private String country;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "지역", example = "지역입니다")
        private String region;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "도시", example = "도시입니다")
        private String city;

        @ApiModelProperty(value = "경도", example = "12.12")
        private double locationX;

        @ApiModelProperty(value = "위도", example = "12.12")
        private double locationY;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "투어 위치 정보 수정 요청 DTO")
    public static class UpdateRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "위치 정보 ID", example = "1")
        private int Id;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "위치 정보 이름", example = "위치 정보 이름입니다")
        private String locationName;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "국가", example = "국가입니다")
        private String country;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "지역", example = "지역입니다")
        private String region;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "도시", example = "도시입니다")
        private String city;

        @ApiModelProperty(value = "경도", example = "12.12")
        private double locationX;

        @ApiModelProperty(value = "위도", example = "12.12")
        private double locationY;

    }

}
