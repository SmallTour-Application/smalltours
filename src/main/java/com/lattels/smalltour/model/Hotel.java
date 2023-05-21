package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tours tours;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "hotel_tel")
    private String hotelTel;

    @Column(name = "description")
    private String description;

    @Column(name = "hotel_location_x")
    private double hotelLocationX;

    @Column(name = "hotel_location_y")
    private double hotelLocationY;

}
