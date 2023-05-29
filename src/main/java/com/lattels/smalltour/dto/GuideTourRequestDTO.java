
package com.lattels.smalltour.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//unauth/profile/guide/tour부분, 해당 가이드 투어 가져오기
public class GuideTourRequestDTO {
    private int count;
    private List<contents> contents;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class contents {
        private int tourId;
        private String thumb;
        private String title;
        private String subTitle;
    }
}

