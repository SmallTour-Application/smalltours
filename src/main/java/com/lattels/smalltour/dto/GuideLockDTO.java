package com.lattels.smalltour.dto;

import com.lattels.smalltour.model.GuideLock;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@Builder
public class GuideLockDTO {

    @Data
    @NoArgsConstructor
    @ApiModel(value = "투어 잠금 기간 가져오기 요청 DTO")
    public static class ViewResponseDTO {

        @ApiModelProperty(value = "잠금 시작 기간", example = "잠금 시작 기간입니다")
        private LocalDate startDay;

        @ApiModelProperty(value = "잠금 종료 기간", example = "잠금 종료 기간입니다")
        private LocalDate endDay;

        public ViewResponseDTO(GuideLock guideLock) {
            this.startDay = guideLock.getStartDay();
            this.endDay = guideLock.getEndDay();
        }
    }

}
