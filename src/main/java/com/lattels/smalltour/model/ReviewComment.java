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
@Table(name = "review_comment")
public class ReviewComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @Column(name = "review_id")
    private int reviewId;

    @Column(name = "member_id")
    private int memberId;

    @Column(name = "content")
    private String content;

    @Column(name = "comment_day")
    private LocalDateTime commentDay;

    @Column(name = "step")
    private int step;

    @Column(name = "group_num")
    private int groupNum;

    @Column(name = "modified")
    private int modified;
  
}
