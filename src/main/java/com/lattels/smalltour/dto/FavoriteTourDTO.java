package com.lattels.smalltour.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class FavoriteTourDTO {
    private int tourId;
    private String tourName;
    private String tourThumb;
    private int favorite; //해당 상품의 좋아요 수


}
