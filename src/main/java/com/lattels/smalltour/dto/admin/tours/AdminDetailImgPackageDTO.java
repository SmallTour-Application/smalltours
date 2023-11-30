package com.lattels.smalltour.dto.admin.tours;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 질문 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDetailImgPackageDTO {

    private List<AdminToursImgDetailList> adminToursImg;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminToursImgDetailList {
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

        @ApiParam("이미지 경로")
        private String profile; // 프로필 이미지가 들어있는 경로

    }

}