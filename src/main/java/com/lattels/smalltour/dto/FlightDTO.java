package com.lattels.smalltour.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
//@NoArgsConstructor
public class FlightDTO {

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "비행기 ID 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "비행기 ID", example = "1")
        private int id;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "비행기 정보 등록 요청 DTO")
    public static class AddRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "항공사 ID", example = "1")
        private int airlineId;

        @ApiModelProperty(value = "출발 일자, 시간", example = "2023-03-03 09:23:00.000")
        private LocalDateTime departDateTime;

        @ApiModelProperty(value = "도착 일자, 시간", example = "2023-03-03 09:23:00.000")
        private LocalDateTime arrivalDateTime;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "출발 도시", example = "출발 도시입니다")
        private String departCity;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "도착 공항", example = "도착 공항입니다")
        private String arrivalAirport;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "비행 시간", example = "1")
        private int duration;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "항공편 가격", example = "10000")
        private int price;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "항공기 이름", example = "항공기 이름입니다")
        private String flightName;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "좌석 타입", example = "좌석 타입입니다")
        private String seatType;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "비행기 정보 수정 요청 DTO")
    public static class UpdateRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "비행기 ID", example = "1")
        private int id;

        @ApiModelProperty(value = "출발 일자, 시간", example = "2023-03-03 09:23:00.000")
        private LocalDateTime departDateTime;

        @ApiModelProperty(value = "도착 일자, 시간", example = "2023-03-03 09:23:00.000")
        private LocalDateTime arrivalDateTime;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "출발 도시", example = "출발 도시입니다")
        private String departCity;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "도착 공항", example = "도착 공항입니다")
        private String arrivalAirport;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "비행 시간", example = "1")
        private int duration;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "항공편 가격", example = "10000")
        private int price;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "항공기 이름", example = "항공기 이름입니다")
        private String flightName;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "좌석 타입", example = "좌석 타입입니다")
        private String seatType;

    }



}
