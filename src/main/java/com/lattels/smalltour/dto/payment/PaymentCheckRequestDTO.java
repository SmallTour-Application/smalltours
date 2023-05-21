package com.lattels.smalltour.dto.payment;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 결제 전 최종 가격 체크 요청 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCheckRequestDTO {

    @ApiParam("패키지 ID")
    private int packageId;

    @ApiParam("여행 일정 ID 목록")
    private List<Integer> items;

    @ApiParam("인원 수")
    private int people;

}
