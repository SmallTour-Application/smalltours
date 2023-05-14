package com.lattels.smalltour.dto.search;

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
    private List<Content> content; //package로 검색할경우
    private List<ContentGuide> contentGuides; //guide로 검색할경우

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private String thumb; // 썸네일 이미지 경로,투어이미지
        private String title; //투어 제목
        private String guideName; //가이드이름, guide_id를 member id랑 조인해서 name을 가져와야함
        private String guideProfileImg;// 가이드 썸네일 이미지
        private float rating; //평점(review테이블에서 가져와야함)
        private int price;//가격
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentGuide {
        private String guideName; //가이드이름, guide_id를 member id랑 조인해서 name을 가져와야함
        private String guideProfileImg;// 가이드 썸네일 이미지
        private float rating; //평점(review테이블에서 가져와야함)
    }

}