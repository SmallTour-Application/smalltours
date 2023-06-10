package com.lattels.smalltour.dto;

import com.lattels.smalltour.model.EducationLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Data
@Builder
public class EducationLogDTO {

    @Data
    @NoArgsConstructor
    @ApiModel(value = "기록 정보 응답 DTO")
    public static class InfoResponseDTO {

        @ApiModelProperty(value = "마지막 시청 시간", example = "")
        private LocalTime lastView;

        @ApiModelProperty(value = "0 : 시청 중, 1 : 시청 완료, 2 : 기한 지남, 3 : 시청 전 (DB에 값이 없음)", example = "1")
        private int state;

        @ApiModelProperty(value = "마지막 시청 시간", example = "")
        private LocalDate completedDate;

        public InfoResponseDTO(EducationLog educationLog) {
            this.lastView = educationLog.getLastView();
            this.state = educationLog.getState();
            this.completedDate = educationLog.getCompletedDate();
        }
    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "기록 정보 응답 DTO")
    public static class ViewedResultRequestDTO {

        @ApiModelProperty(value = "교육 아이디", example = "1")
        private int educationId;

        @ApiModelProperty(value = "마지막 시청 시간", example = "")
        private LocalTime lastView;

    }

    public static class EducationLogState {

        // 교육 중
        public static final int IN_TRAINING = 0;

        // 교육 완료
        public static final int COMPLETED = 1;

        // 교육 완료
        public static final int LATE = 2;

        // 교육 전 ( DB에는 저장되지 않음)
        public static final int DO_NOT = 3;

    }
}
