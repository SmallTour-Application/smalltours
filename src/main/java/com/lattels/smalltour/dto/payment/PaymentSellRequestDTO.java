package com.lattels.smalltour.dto.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 결제 판매 내역 조회 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSellRequestDTO {

    @ApiParam("검색 내용")
    private String keyword;

    @ApiParam("검색 기간 종료일")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDay;

    @ApiParam("검색 기간 종료일")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDay;

    @ApiParam("검색 타입 (0: 패키지명, 1: 가이드명)")
    private int type;

    public static class SearchType {

        // 패키지명으로 검색
        public static final int BY_PACKAGE_NAME = 0;

        // 가이드명으로 검색
        public static final int BY_GUIDE_NAME = 1;

    }

}
