package com.lattels.smalltour.dto.payment;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 결제 정보 목록 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentListDTO {

    @ApiParam("총 결제내역 개수")
    private int count;

    @ApiParam("결제내역 목록")
    private List<PaymentDTO> content;

}
