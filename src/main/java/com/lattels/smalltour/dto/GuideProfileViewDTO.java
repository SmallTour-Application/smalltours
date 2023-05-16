
package com.lattels.smalltour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lattels.smalltour.model.Member;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideProfileViewDTO {
    private String name;
    private String tel;
    private String introduce;// 자기 소개
    private LocalDateTime joinDay;
    private int gender;
    private String profileImg;
    private List<TourDTO> tours;// 해당 가이드의 투어들
    private int favoriteCount; // 가이드 좋아요 갯수

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TourDTO {
        private String thumb;// 썸네일 이미지 경로
        private String title;
        private String guideName;
        private String guideProfileImg;// 가이드 썸네일 이미지
        private float rating;// 여행평점
        private int price;
    }





}

