package com.lattels.smalltour.dto.admin.payment;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPaymentDetailDTO {
    private PaymentDetailDTO paymentDetailDTO;
    @Builder
    @Data
    @AllArgsConstructor
    public static class PaymentDetailDTO {
        private int id; //결제 아이디
        private int memberId; // 사용자 아이디(결제한 회원)
        private String memberName;
        private String email;
        private String tel;
        private int guideId; //가이드 아이디
        private String guideName;
        private LocalDateTime paymentDay;
        private LocalDate startDay;
        private LocalDate endDay;
        private int people;
        private String state;
        private int tourId;
        private String tourName; //title
        private int price;
        private LocalDate departureDay;
    }
}
