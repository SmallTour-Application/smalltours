package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_message")
public class EmailMessage {
    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L; // 이메일 토큰 만료 시간

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 만료시간

    @Column(name = "title")
    private String title; // 만료여부


    @Column(name = "content")
    private String content;

    @Column(name = "admin_id")
    private int adminId;
}
