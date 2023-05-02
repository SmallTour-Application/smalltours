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
@Table(name = "schedule_item")
public class ScheduleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @Column(name = "schedule_id")
    private int scheduleId;
    
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "price")
    private int price;

    @Column(name = "default_item")
    private int defaultItem;

    @Column(name = "location_x")
    private double locationX;

    @Column(name = "location_y")
    private double locationY;

}
