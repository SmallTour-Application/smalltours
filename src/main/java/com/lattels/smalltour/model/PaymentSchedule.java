package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(PaymentSchedule.PaymentScheduleID.class)
@Table(name = "payment_schedule")
public class PaymentSchedule {

    @Id
    @Column(name = "payment_id")
    private int paymentId;

    @Id
    @Column(name = "schedule_item_id")
    private int scheduleItemId;

    @Column(name = "price")  
    private int price;


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentScheduleID implements Serializable {

        private Integer paymentId;

        private Integer scheduleItemId;

    }

}