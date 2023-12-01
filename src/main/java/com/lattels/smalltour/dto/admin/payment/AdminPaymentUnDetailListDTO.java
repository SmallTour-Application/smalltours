package com.lattels.smalltour.dto.admin.payment;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPaymentUnDetailListDTO {
    private int id;
    private int memberId;
    private int tourId;
    private String tourName; //title
    private String email;
    private int price;
    private String memberName;
    private int people;
    private LocalDate departureDay;
    private int state;

}
