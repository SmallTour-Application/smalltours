
package com.lattels.smalltour.dto;

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
public class GuideTourReviewDTO {

    private float avgRating;
    private int count;
    private List<Review> reviews;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Review {

        private int packageId;
        private String packageName;
        private String nickname;
        private int rating;
        private String content;
        private String createdDay;
    }


}

