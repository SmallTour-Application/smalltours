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
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;
 
    @Column(name = "min_people")
    private int minPeople;

    @Column(name = "max_people")
    private int maxPeople;

    @Column(name = "description")
    private String description;


    @Column(name = "image")
    private String image;


    @Column(name = "add_price")
    private int addPrice;

}
