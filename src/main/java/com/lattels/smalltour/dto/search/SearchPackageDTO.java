package com.lattels.smalltour.dto.search;

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
public class SearchPackageDTO {
    private int count; // 검색결과 갯수
    private List<PackageContent> content; //package로 검색할경우


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackageContent {
        private int tourId; // 투어 번호
        private String thumb; // 썸네일 이미지 경로,투어이미지
        private String title; //투어 제목
        private String guideName; //가이드이름, guide_id를 member id랑 조인해서 name을 가져와야함
        private String guideProfileImg;// 가이드 썸네일 이미지
        private float rating; //평점(review테이블에서 가져와야함)
        private int price;//가격
        //UpperPayment에 값이 없는 tourId인경우 null로표시할게 아니라 아예 안보여주게 어노테이션사용
        //결과에 upperPaymentTourIdResponseDTO = null이거 자체가 안보임
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ItemDTO.UpperPaymentTourIdResponseDTO upperPaymentTourIdResponseDTO;

    }


}