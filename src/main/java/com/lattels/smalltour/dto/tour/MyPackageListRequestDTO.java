package com.lattels.smalltour.dto.tour;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 내 패키지 목록 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPackageListRequestDTO {

    @ApiParam("페이지")
    private int page;

    @ApiParam("정렬 (0: 최신순, 1: 상태순(수락, 미수락))")
    private int sort;

    public static class Sort {

        // 최신순
        public static final int LATEST = 0;

        // 상태순 (수락, 미수락)
        public static final int STATE = 1;

    }

}
