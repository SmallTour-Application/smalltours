package com.lattels.smalltour.dto;

import com.lattels.smalltour.model.Tours;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class StatsDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "기간 요청 DTO")
    public static class DateRequestDTO {

        @ApiModelProperty(value = "시작일", example = "2023-04-13")
        private LocalDate startDate;

        @ApiModelProperty(value = "종료일", example = "2023-04-13")
        private LocalDate endDate;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "통계 응답 DTO")
    public static class StatsResponseDTO {

        List<StatsToursDTO> statsToursDTOList;

        @ApiModelProperty(value = "판매금액", example = "1")
        private int salesRevenue;

        @ApiModelProperty(value = "판매량", example = "1")
        private int salesVolume;

        @ApiModelProperty(value = "가이드 리뷰 평점", example = "1")
        private double guideReviewRating;

        @ApiModelProperty(value = "투어 리뷰 평점", example = "1")
        private double tourReviewRating;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class StatsToursDTO {

        @ApiModelProperty(value = "투어 ID", example = "1")
        private int id;

        @ApiModelProperty(value = "투어 이름", example = "투어 이름입니다")
        private String title;

        @ApiModelProperty(value = "투어 가격", example = "10000")
        private int price;

        @ApiModelProperty(value = "투어 생성일", example = "")
        private LocalDateTime createdDay;

        @ApiModelProperty(value = "투어 썸네일", example = "투어 썸네일입니다")
        private String thumb;

        @ApiModelProperty(value = "총 판매금액", example = "10000")
        private int salesRevenue;

        @ApiModelProperty(value = "판매량", example = "1")
        private int salesVolume;

        public StatsToursDTO(Tours tours) {
            this.id = tours.getId();
            this.title = tours.getTitle();
            this.price = tours.getPrice();
            this.createdDay = tours.getCreatedDay();
        }

    }
}
