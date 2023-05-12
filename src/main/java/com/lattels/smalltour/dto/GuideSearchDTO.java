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
public class GuideSearchDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GuideSearchResult {
        private int count;
        private List<ReviewInfo> content;


    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewInfo {
        private String profileImg;
        private String guideName;
        private float rating;

    }
}