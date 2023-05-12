package com.lattels.smalltour.dto.guidereview;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.model.GuideReview;
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

    // 리뷰 ID
    private long id;

    // 리뷰 대상 가이드
    private GuideReviewMemberDto guide;

    // 리뷰한 회원
    private GuideReviewMemberDto reviewer;

    // 평점
    private int rating;

    // 리뷰 내용
    private String content;

    // 리뷰 생성일
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDay;

    public GuideReviewDTO(GuideReview guideReview) {
        this.id = guideReview.getId();
        this.guide = new GuideReviewMemberDto(new MemberDTO(guideReview.getGuide()));
        this.reviewer = new GuideReviewMemberDto(new MemberDTO(guideReview.getReviewer()));
        this.rating = guideReview.getRating();
        this.content = guideReview.getContent();
        this.createdDay = guideReview.getCreatedDay();
    }

}
