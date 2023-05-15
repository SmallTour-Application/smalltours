package com.lattels.smalltour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Positive;
import java.time.LocalTime;

@AllArgsConstructor
@Data
@Builder
public class ScheduleDTO {

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "여행 일정 ID 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "여행 일정 ID", example = "1")
        private int id;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "여행 일정 등록 요청 DTO")
    public static class AddRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 ID", example = "1")
        private int tourId;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "일정 날짜 (1 : 1일차)", example = "1")
        private int tourDay;

        @JsonFormat(pattern = "kk:mm", shape = JsonFormat.Shape.STRING)
        @ApiModelProperty(value = "일정 시작 시간", example = "23:30")
        private LocalTime startTime;

        @JsonFormat(pattern = "kk:mm", shape = JsonFormat.Shape.STRING)
        @ApiModelProperty(value = "일정 종료 시간", example = "23:30")
        private LocalTime endTime;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "여행 일정 수정 요청 DTO")
    public static class UpdateRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "일정 ID", example = "1")
        private int id;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "일정 날짜 (1 : 1일차)", example = "1")
        private int tourDay;

        @ApiModelProperty(value = "일정 시작 시간", example = "23:30")
        private LocalTime startTime;

        @ApiModelProperty(value = "일정 종료 시간", example = "23:30")
        private LocalTime endTime;

    }


}
