package com.lattels.smalltour.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirlineDTO {

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "항공사 ID 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "항공사 ID", example = "1")
        private int id;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "항공사 등록 요청 DTO")
    public static class AddRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 ID", example = "1")
        private int tourId;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "항공사 이름", example = "항공사 이름입니다")
        private String name;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "항공사 국가", example = "항공사 국가입니다")
        private String country;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "항공사 이메일", example = "항공사 이메일입니다")
        private String contactEmail;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "항공사 전화번호", example = "항공사 전화번호입니다")
        private String contactTel;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "항공사 수정 요청 DTO")
    public static class UpdateRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "항공사 ID", example = "1")
        private int id;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "항공사 이름", example = "항공사 이름입니다")
        private String name;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "항공사 국가", example = "항공사 국가입니다")
        private String country;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "항공사 이메일", example = "항공사 이메일입니다")
        private String contactEmail;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "항공사 전화번호", example = "항공사 전화번호입니다")
        private String contactTel;

    }


}
