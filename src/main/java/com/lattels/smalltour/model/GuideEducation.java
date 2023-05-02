package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "guide_education")
public class GuideEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @Column(name = "education_id")
    private String educationId;

    @Column(name = "guide_id")
    private int guideId;

    @Column(name = "last_view")
    private LocalTime lastView; //DB에선 타입이 TIME

    @Column(name = "is_finish")
    private int isFinish;

}
