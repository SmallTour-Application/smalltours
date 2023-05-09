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
@Table(name = "guide_review")
public class GuideReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

//    @Column(name = "guide_id") //가이드아이디
//    private int guideId;
//
//    @Column(name = "reviewer_id") //memberId랑 동일,작성자
//    private int reviewerId;

    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Member guide;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private Member reviewer;

    @Column(name = "rating")
    private int rating;

    @Column(name = "content")
    private String content;

    @Column(name = "created_day")
    private LocalDateTime createdDay;

}