package com.lattels.smalltour.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 결제 정보 목록 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMemberListDTO {

    private int id;
    //private int memberId;
    private String tourName; //title
    private String email;
    private int price;
    private String memberName;
    private int people;
    private LocalDate departureDay;
    private int state;

}
