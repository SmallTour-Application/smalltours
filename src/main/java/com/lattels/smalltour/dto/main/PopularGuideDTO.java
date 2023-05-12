package com.lattels.smalltour.dto.main;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularGuideDTO {

    private int count;
    private List<ReviewInfo> content;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewInfo {
        private String profileImg;
        private String guideName;
        private float rating; //GuideReview 테이블에서 rating 끌고올것

    }
}