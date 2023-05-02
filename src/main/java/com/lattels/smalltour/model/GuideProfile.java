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
@Table(name = "guide_profile")
public class GuideProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @Column(name = "guide_id")
    private int guideId;

    @Column(name = "resume") //이력서 경로
    private String resume;

    @Column(name = "introduce") //자기소개
    private String introduce;

    @Column(name = "portfolio_path") //포트폴리오 경로
    private String portfolioPath; 

}
