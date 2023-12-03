package com.lattels.smalltour.dto.admin.education;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EducationDetailDTO {
    private List<EducationDTO> educationDTO;

    @Data
    @Builder
    @AllArgsConstructor
    @ApiModel(value = "")
    public static class EducationDTO {
        @ApiModelProperty(value = "교육 ID", example = "")
        private int id;

        @ApiModelProperty(value = "영상 경로", example = "")
        private String videoPath;

        @ApiModelProperty(value = "교육시작일", example = "")
        private LocalDate startDay;

        @ApiModelProperty(value = "교육종료일", example = "")
        private LocalDate endDay;

        @ApiModelProperty(value = "재생 시간", example = "")
        private LocalTime playTime;

        @ApiModelProperty(value = "영상제목", example = "")
        private String title;

        @ApiModelProperty(value = "업로드날짜", example = "")
        private LocalDate uploadDay;

        @ApiModelProperty(value = "수강상태", example = "")
        private int educationState;

        @ApiModelProperty(value = "교육로그아이디", example = "")
        private int educationLogId;

        @ApiModelProperty(value = "가이드 id", example = "")
        private int guideId;

        @ApiModelProperty(value = "가이드 이름", example = "")
        private String guideName;

        @ApiModelProperty(value = "영상 끝 시간", example = "")
        private LocalTime lastView;

        @ApiModelProperty(value = "해당 영상 수강상태 0:수강중 1:수강완료 2:미수강", example = "")
        private int educationLogState;

        @ApiModelProperty(value = "완강일", example = "")
        private LocalDate completedDate;


    }


}
