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
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "member_id")
    private int memberId;

    @Column(name = "content")
    private String content;

    @Column(name = "created_day")
    private LocalDateTime createdDay;

    @Column(name = "updated_day")
    private LocalDateTime UpdateDay;

}
