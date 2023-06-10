
package com.lattels.smalltour.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
        //UpperPayment에 값이 없는 tourId인경우 null로표시할게 아니라 아예 안보여주게 어노테이션사용
        //결과에 upperPaymentTourIdResponseDTO = null이거 자체가 안보임
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ItemDTO.UpperPaymentTourIdResponseDTO upperPaymentTourIdResponseDTO;
    }
}

