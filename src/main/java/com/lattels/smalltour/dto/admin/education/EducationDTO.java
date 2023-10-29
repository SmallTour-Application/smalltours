package com.lattels.smalltour.dto.admin.education;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EducationDTO {
    private int count;
    private List<EducationListDTO> educationLogList;

    @Data
    @ApiModel(value = "")
    public static class EducationListDTO {
        @ApiModelProperty(value = "목록 ID", example = "")
        private int id;

        @ApiModelProperty(value = "강좌 제목", example = "")
        private String educationTitle;

        @ApiModelProperty(value = "교육시작일", example = "")
        private LocalDate startDay;

        @ApiModelProperty(value = "교육종료일", example = "")
        private LocalDate endDay;

        @ApiModelProperty(value = "완료율", example = "")
        private int progress;

        @ApiModelProperty(value = "수강상태", example = "")
        private int state;


        @Builder
        public EducationListDTO(int id, LocalDate startDay, LocalDate endDay,int progress, String educationTitle,int state) {
            this.id = id;
            this.startDay = startDay;
            this.endDay = endDay;
            this.progress = progress;
            this.educationTitle = educationTitle;
            this.state = state;
        }
    }


}
