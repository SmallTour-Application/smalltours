package com.lattels.smalltour.dto.main;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lattels.smalltour.dto.ItemDTO;
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
        //UpperPayment에 값이 없는 tourId인경우 null로표시할게 아니라 아예 안보여주게 어노테이션사용
        //결과에 upperPaymentTourIdResponseDTO = null이거 자체가 안보임
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ItemDTO.UpperPaymentTourIdResponseDTO upperPaymentTourIdResponseDTO;
    }

}