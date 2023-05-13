package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flight")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @Column(name = "airline_id")
    private int airlineId;

    @Column(name = "depart_datetime")
    private LocalDateTime departDateTime;

    @Column(name = "arrival_datetime")
    private LocalDateTime arrivalDateTime;

    @Column(name = "depart_city")
    private String departCity;

    @Column(name = "arrival_city")
    private String arrivalCity;

    @Column(name = "duration")
    private int duration;

    @Column(name = "price")
    private int price;

}
