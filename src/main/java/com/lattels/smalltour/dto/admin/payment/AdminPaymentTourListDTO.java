package com.lattels.smalltour.dto.admin.payment;

import com.lattels.smalltour.dto.admin.search.AdminSearchDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder

@AllArgsConstructor
public class AdminPaymentTourListDTO {
    private Integer paymentId;
    private Integer tourId;
    private String title;
    private Integer memberId;
    private String memberName;
    private Integer guideId;
    private String guideName;
    private Integer price;
    private Integer state;
    private LocalDate departureDay;
    private Integer people;

}
