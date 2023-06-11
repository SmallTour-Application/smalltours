package com.lattels.smalltour.dto.packagereview;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lattels.smalltour.model.Reviews;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 패키지 리뷰 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageReviewDTO {

    @ApiParam("리뷰 ID")
    private int reviewId;

    @ApiParam("패키지 ID")
    private int packageId;

    @ApiParam("패키지 이름")
    private String packageName;

    @ApiParam("프로필 이미지")
    private String profileImg;

    @ApiParam("리뷰 작성자 닉네임")
    private String nickname;

    @ApiParam("리뷰 내용")
    private String content;

    @ApiParam("평점")
    private int rating;

    @ApiParam("작성일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDay;

    public PackageReviewDTO(Reviews reviews) {
        this.reviewId = reviews.getId();
        this.packageId = reviews.getTours().getId();
        this.packageName = reviews.getTours().getTitle();
        this.profileImg = reviews.getMember().getProfile();
        this.nickname = reviews.getMember().getNickname();
        this.content = reviews.getContent();
        this.rating = reviews.getRating();
        this.createdDay = reviews.getCreatedDay();
    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "리뷰 평점 응답 DTO")
    public static class RatingResponseDTO {

        @ApiModelProperty(value = "평점", example = "1")
        private double rating;

        public RatingResponseDTO(double rating) {
            this.rating = rating;
        }
    }

}
