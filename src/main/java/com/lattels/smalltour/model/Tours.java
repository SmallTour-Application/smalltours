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
@Table(name = "tours")
public class Tours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값


    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Member guide;

    @Column(name = "title")
    private String title;

    @Column(name = "subtitle")
    private String subTitle;

    @Column(name = "description")
    private String description;

    @Column(name = "meeting_point")
    private String meetingPoint;

    @Column(name = "duration")
    private int duration;

    @Column(name = "price")
    private int price;

    @Column(name = "max_group_size")
    private int maxGroupSize;

    @Column(name = "min_group_size")
    private int minGroupSize;

    @Column(name = "created_day")
    private LocalDate createdDay;

    @Column(name = "update_day")
    private LocalDate updateDay;

    @Column(name = "approvals")
    private int approvals;

    @Column(name = "default_price")
    private int defaultPrice;

    @Column(name = "thumb")
    private String thumb;



}
