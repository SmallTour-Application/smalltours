package com.lattels.smalltour.dto.admin.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDetailReviewDTO {
    private long count; // 검색결과 갯수
    private List<detailedReview> deatilReviews;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class detailedReview {
        private int id; //해당 멤버 id값
        private String tourName;
        private String guideName;
        private String buyName;
        private float rating;
        private String reviewContent;
    }
}