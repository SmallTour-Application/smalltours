package com.lattels.smalltour.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class favoriteTourDTO {
    private int tourId;
    private String tourName;
    private String tourThumb;
}
