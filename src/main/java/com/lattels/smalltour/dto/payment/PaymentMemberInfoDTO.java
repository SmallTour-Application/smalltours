package com.lattels.smalltour.dto.payment;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 결제 정보 목록 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMemberInfoDTO {

    private int memberId;
    private String memberName;
    private String tel;
    private int people;
    private LocalDate departureDay;

}
