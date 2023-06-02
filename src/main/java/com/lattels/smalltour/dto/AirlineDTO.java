package com.lattels.smalltour.dto;

import com.lattels.smalltour.model.Airline;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Data
@Builder
//@NoArgsConstructor
public class AirlineDTO {

    @Getter
    @Setter
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

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "항공사 정보 응답 DTO")
    public static class ViewResponseDTO {

        @ApiModelProperty(value = "항공사 ID", example = "1")
        private int id;

        @ApiModelProperty(value = "항공사 이름", example = "항공사 이름입니다")
        private String name;

        @ApiModelProperty(value = "항공사 국가", example = "항공사 국가입니다")
        private String country;

        @ApiModelProperty(value = "항공사 이메일", example = "항공사 이메일입니다")
        private String contactEmail;

        @ApiModelProperty(value = "항공사 전화번호", example = "항공사 전화번호입니다")
        private String contactTel;

        FlightDTO.ViewResponseDTO flightDTO;

        public ViewResponseDTO(Airline airline) {
            this.id = airline.getId();
            this.name = airline.getName();
            this.country = airline.getCountry();
            this.contactEmail = airline.getContactEmail();
            this.contactTel = airline.getContactTel();
        }
    }


}
