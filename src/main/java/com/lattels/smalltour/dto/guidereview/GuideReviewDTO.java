package com.lattels.smalltour.dto.guidereview;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lattels.smalltour.model.GuideReview;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 가이드 리뷰 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideReviewDTO {

    @ApiParam("리뷰 ID")
    private int reviewId;

    @ApiParam("가이드 ID")
    private int guideId;

    @ApiParam("가이드 이름")
    private String guideName;

    @ApiParam("닉네임")
    private String nickname;

    @ApiParam("평점")
    private int rating;

    @ApiParam("리뷰 내용")
    private String content;

    @ApiParam("작성일시")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDay;

    public GuideReviewDTO(GuideReview guideReview) {
        this.reviewId = guideReview.getId();
        this.guideId = guideReview.getGuide().getId();
        this.guideName = guideReview.getGuide().getName();
        this.nickname = guideReview.getReviewer().getNickname();
        this.rating = guideReview.getRating();
        this.content = guideReview.getContent();
        this.createdDay = guideReview.getCreatedDay();
    }

}
