package com.lattels.smalltour.dto.admin.payment;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminPaymentListDTO {
    @Builder
    @Getter
    @Setter
    public static class MemberPaymentDTO{
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

    @Builder
    @Getter
    @Setter
    public static class MemberPaymentListDTO{
        // 총 갯수
        private int totalCnt;
        // 결제 정보 목록
        private List<MemberPaymentDTO> paymentList;
    }

}
