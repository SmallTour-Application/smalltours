package com.lattels.smalltour.dto.admin.education;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EducationGuideDTO {
    private int count;
    private List<EducationGuideListDTO> educationGuideLogList;

    @Data
    @ApiModel(value = "")
    public static class EducationGuideListDTO {
        @ApiModelProperty(value = "목록 ID", example = "")
        private int id;

        @ApiModelProperty(value = "강좌 제목", example = "")
        private String educationTitle;

        @ApiModelProperty(value = "교육시작일", example = "")
        private LocalDate startDay;

        @ApiModelProperty(value = "교육종료일", example = "")
        private LocalDate endDay;

        @ApiModelProperty(value = "완료 날짜", example = "")
        private LocalDate completeDate;

        @ApiModelProperty(value = "수강상태", example = "")
        private int state;


        @Builder
        public EducationGuideListDTO(int id, LocalDate startDay, LocalDate endDay,LocalDate completeDate, String educationTitle,int state) {
            this.id = id;
            this.startDay = startDay;
            this.endDay = endDay;
            this.completeDate = completeDate;
            this.educationTitle = educationTitle;
            this.state = state;
        }
    }


}
