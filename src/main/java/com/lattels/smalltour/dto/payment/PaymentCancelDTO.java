package com.lattels.smalltour.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 결제 취소 결과 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelDTO {

    private String message;

    public static class CancelType {

        // 취소할 수 있는 기간이 아님
        public static final String TIME_OVER = "time over";

        // 이미 떠난 여행
        public static final String ARRIVED = "arrived";

        // 이미 결제한 건
        public static final String ALREADY_PAID = "already paid";

        // 취소 완료
        public static final String SUCCESS = "success";

    }

}
