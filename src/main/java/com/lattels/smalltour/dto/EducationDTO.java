package com.lattels.smalltour.dto;

import com.lattels.smalltour.model.Education;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@Data
@Builder
public class EducationDTO {

    @Data
    @NoArgsConstructor
    @ApiModel(value = "교육 ID 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "교육 ID", example = "1")
        private int id;

    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "교육 리스트 응답 DTO")
    public static class ListResponseDTO {

        @ApiModelProperty(value = "교육 ID", example = "1")
        private int id;

        @ApiModelProperty(value = "교육 제목", example = "제목입니다")
        private String title;

        @ApiModelProperty(value = "교육 시작일", example = "")
        private LocalDate startDay;

        @ApiModelProperty(value = "교육 종료일", example = "")
        private LocalDate endDay;

        @ApiModelProperty(value = "0 : 교육 중, 1 : 완료", example = "0")
        private int state;

        public ListResponseDTO(Education education) {
            this.id = education.getId();
            this.title = education.getTitle();
            this.startDay = education.getStartDay();
            this.endDay = education.getEndDay();
        }

    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "교육 정보 응답 DTO")
    public static class ViewResponseDTO {

        @ApiModelProperty(value = "교육 ID", example = "1")
        private int id;

        @ApiModelProperty(value = "재생 시간", example = "")
        private LocalTime playTime;

        @ApiModelProperty(value = "동영상 경로", example = "제목입니다")
        private String videoPath;

        private EducationLogDTO.InfoResponseDTO educationLogDTO;


        public ViewResponseDTO(Education education) {
            this.id = education.getId();
            this.playTime = education.getPlayTime();
        }

    }

}
