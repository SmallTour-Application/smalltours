package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "locations")
public class Locations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @Column(name = "tour_id")
    private int tourId;
    
    @Column(name = "location_name")
    private String locationName;

    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @Column(name = "location_x")
    private double locationX;

    @Column(name = "location_y")
    private double locationY;


}
