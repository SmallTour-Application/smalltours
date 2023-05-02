package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @Column(name = "member_id")
    private int memberId;

    @Column(name = "tour_id")
    private int tourId;

    @Column(name = "price")
    private int price;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "state")
    private String state;

    @Column(name = "departure_day")
    private LocalDate departureDay;

    @Column(name = "people")
    private int people;

  

}
