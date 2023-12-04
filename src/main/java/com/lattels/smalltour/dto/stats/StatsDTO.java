package com.lattels.smalltour.dto.stats;

import com.lattels.smalltour.model.Tours;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Date;
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

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "현재 총 회원 수 응답 DTO")
    public static class TotalMembersDTO {

        @PositiveOrZero(message = "양수와 0만 가능합니다.")
        @ApiModelProperty(value = "여행자 수", example = "1")
        private int traveler;

        @PositiveOrZero(message = "양수와 0만 가능합니다.")
        @ApiModelProperty(value = "미등록 가이드 수", example = "1")
        private int unregisteredGuide;

        @PositiveOrZero(message = "양수와 0만 가능합니다.")
        @ApiModelProperty(value = "가이드 수", example = "1")
        private int guide;

        @PositiveOrZero(message = "양수와 0만 가능합니다.")
        @ApiModelProperty(value = "총합", example = "1")
        private int total;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "판매 비율 응답 DTO")
    public static class TotalVolumePercentageResponseDTO {

        @ApiModelProperty(value = "총 판매량", example = "1")
        private int totalSalesVolume;

        List<TotalVolumePercentageDTO> totalVolumePercentageDTOList;

    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "가이드 통계 검색 요청 DTO")
    public static class  SearchGuideSalesRequestDTO{

        @ApiModelProperty(value = "시작일", example = "2023-04-13")
        private LocalDate startDate;

        @ApiModelProperty(value = "종료일", example = "2023-04-13")
        private LocalDate endDate;

        @ApiModelProperty(value = "판매량 (몇 개 이상)", example = "1")
        private int sales;

        @ApiModelProperty(value = "상태", example = "0 : 미결제 / 1 : 결제")
        private int state;

        @ApiModelProperty(value = "패키지 이름", example = "패키지명")
        private String toursTitle;

    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "가이드 통계 응답 DTO")
    public static class SearchGuideSalesResponseDTO {

        @ApiModelProperty(value = "총 판매량", example = "1")
        private int totalSalesVolume;

        @ApiModelProperty(value = "총 매출액", example = "1")
        private int totalSales;

        List<DatePerSalesDTO> datePerSalesDTOS;

    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "가이드 날짜별 매출")
    public static class DatePerSalesDTO {

        @ApiModelProperty(value = "날짜", example = "")
        private Date date;

        @ApiModelProperty(value = "판매량", example = "1")
        private int salesVolume;

        @ApiModelProperty(value = "매출액", example = "1")
        private int sales;

        public DatePerSalesDTO(Object[] objects) {
            this.date = (Date) objects[0];
            this.salesVolume = Integer.parseInt(String.valueOf(objects[1]));
            this.sales = Integer.parseInt(String.valueOf(objects[2]));
        }

    }

}
