package com.lattels.smalltour.dto.admin.payment;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminGuidePaymentListDTO {

    // 총 갯수
    private int totalCnt;
    // 결제 정보 목록
    private List<GuideDurationDTO> guideDurationDTOS;


    @Builder
    @Getter
    @Setter
    public static class GuideDurationDTO {
        private int memberId;
        private String memberName;
        private String tel;
        private int people;
        private LocalDate departureDay;
        private String email;
        private int guideId;
        private String guideName;
        private int price;
        //결제일
        private LocalDateTime paymentDay;
        //가이드 startDay~ endDAy
        private LocalDate startDay;
        private LocalDate endDay;
        // 1:예약 그 외 미예약
        private String state;
        //상품명
        private String toursTitle;
        //상품아이디
        private int toursId;
    }


}
