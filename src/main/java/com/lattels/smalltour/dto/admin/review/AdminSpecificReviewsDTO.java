package com.lattels.smalltour.dto.admin.review;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSpecificReviewsDTO {
    private long count; // 검색결과 갯수
    private List<ContentReview> contentReviews;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentReview {
        private int id; //해당 멤버 id값
        private int tourId;
        private String tourName;
        private String reviewContent;
        private LocalDateTime createdDay;
        private String status;
        private int paymentId;
        private String memeberName;
        private int memberId;
        private int guideId;

    }
}