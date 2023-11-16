package com.lattels.smalltour.dto.payment;

import io.swagger.annotations.ApiParam;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 결제 정보 목록 DTO
 */
@Data @AllArgsConstructor @Builder @NoArgsConstructor
public class PaymentMemberInfoDTO {
    private int memberId;
    private String memberName;
    private String tel;
    private int people;
    private LocalDate departureDay;
    private String email;
    private String name;
    private int price;
    //결제일
    private LocalDateTime paymentDay;
    //결과
    private int result;
    //state
    private int state;
    //상품명
    private String toursTitle;
    //상품아이디
    private int toursId;
}
