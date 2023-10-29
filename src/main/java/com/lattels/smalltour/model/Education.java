package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @Column(name = "video_path")
    private String videoPath;

    @Column(name = "start_day")
    private LocalDate startDay;
  
    @Column(name = "end_day")
    private LocalDate endDay;

    @Column(name = "play_time")
    private LocalTime playTime;

    @Column(name = "title")
    private String title;

    @Column(name = "upload_day")
    private LocalDate uploadDay;

    @Column(name = "state")
    private int state; //0:수강종료 1:수강가능
}
