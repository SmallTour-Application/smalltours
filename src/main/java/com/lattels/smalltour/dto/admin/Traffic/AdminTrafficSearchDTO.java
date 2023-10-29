package com.lattels.smalltour.dto.admin.Traffic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminTrafficSearchDTO {
    private int count; // 검색결과 갯수
    private List<Region> regions;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Region {
        private String region;
        private int count;//지역에서 로그인한 횟수
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class broswer {
        private String broswer;
    }

}