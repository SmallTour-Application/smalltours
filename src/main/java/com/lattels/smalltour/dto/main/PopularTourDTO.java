package com.lattels.smalltour.dto.main;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularTourDTO {
    private int count;
    private List<PopularTourDTO.TourInfo> content;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TourInfo {
        private String thumb;
        private String title;
        private String subTitle;
        private int price;
        private int minPeople;
        private int maxPeople;
        private float rating;  //Review테이블에서 rating끌어올것
    }

}