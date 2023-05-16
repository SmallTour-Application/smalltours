package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tours tours;

    @Column(name = "price")
    private int price;

    @Column(name = "payment_day")
    private LocalDateTime paymentDay;

    @Column(name = "state")
    private String state; // 0: 미결제, 1: 결제

    @Column(name = "departure_day")
    private LocalDate departureDay;

    @Column(name = "people")
    private int people;

}
