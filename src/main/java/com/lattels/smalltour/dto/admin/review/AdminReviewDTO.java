package com.lattels.smalltour.dto.admin.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminReviewDTO {
    private long count; // 검색결과 갯수
    private List<ContentReview> contentReviews;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentReview {
        private int id; //해당 멤버 id값
        private String tourName;
        private String reviewContent;
        private LocalDateTime createdDay;
        private String status;
        private int memberId;
        private String memeberName;
    }


}