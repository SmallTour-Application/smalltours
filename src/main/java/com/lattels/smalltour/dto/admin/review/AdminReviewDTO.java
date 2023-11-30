package com.lattels.smalltour.dto.admin.review;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminReviewDTO {
    @Builder
    @Getter
    @Setter
    public static class ReviewDTO{
        private int reviewId;
        private int memberId;
        private int guideId;
        private String content;
        private int score;
        // 패키지명
        private String packageName;
        // 리뷰내용
        private String reviewContent;
        // 작성일시
        private LocalDateTime reviewDate;
        // 결제id
        private int paymentId;
        // 가이드이름
        private String guideName;
        // 패키지아이디
        private int packageId;
        // state
        private int state;
        // 작성자명
        private String reviewerName;
    }

    @Builder
    @Getter
    @Setter
    public static class ReviewListDTO{
        // 총 갯수
        private int totalCnt;
        // 리뷰 정보 목록
        private List<ReviewDTO> reviewList;
    }
}