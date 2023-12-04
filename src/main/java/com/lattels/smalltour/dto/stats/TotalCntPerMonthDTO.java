package com.lattels.smalltour.dto.stats;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TotalCntPerMonthDTO {

    @ApiModelProperty(value = "날짜", example = "2023-04")
    private String month;

    @ApiModelProperty(value = "가입자 수", example = "2")
    private long totalCnt;

    public TotalCntPerMonthDTO(int year, int month, long memberCnt) {
        this.month = year + "-" + month;
//        this.month = month;
        this.totalCnt = memberCnt;
    }
    public TotalCntPerMonthDTO(LocalDateTime month, long memberCnt) {
        this.month = month.getYear() + "-" + month.getMonthValue();
        this.totalCnt = memberCnt;
    }
    public TotalCntPerMonthDTO(LocalDate month, long memberCnt) {
        this.month = month.getYear() + "-" + month.getMonthValue();
        this.totalCnt = memberCnt;
    }
}
