package com.lattels.smalltour.dto.admin.tours;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 질문 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPackageDTO {

    private long count; //총 결과물 갯수
    private List<AdminToursList> adminToursList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminToursList {
        //번호,패키지명,생성일시,수정일시,상태
        @ApiParam("패키지 ID")
        private int tourId;

        @ApiParam("패키지명")
        private String tourName;

        @ApiParam("생성일")
        private LocalDateTime createdDay;

        @ApiParam("수정일시")
        private LocalDateTime updateDay;

        @ApiParam("상태")
        private String approval;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminToursDetailList {
        //번호,패키지명,생성일시,수정일시,상태
        @ApiParam("패키지 ID")
        private int tourId;

        @ApiParam("패키지명")
        private String tourName;

        @ApiParam("판매자")
        private String tourSeller;

        @ApiParam("평점")
        private float rating;

        @ApiParam("기간")
        private int duration;

        @ApiParam("가격")
        private int price;

        @ApiParam("인원수")
        private int people;

        @ApiParam("예약 여부")
        private String status;
    }

}