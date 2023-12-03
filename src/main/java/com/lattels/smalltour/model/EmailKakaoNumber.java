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
@Table(name = "email_kakao_number")
public class EmailKakaoNumber {
    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L; // 이메일 토큰 만료 시간

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id;

    @Column(name = "email_number_id")
    private int emailNumberId;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate; // 만료시간

    @Column(name = "expired")
    private int expired; // 만료여부


    @Column(name = "member_id")
    private int memberId;



    // 이메일 인증 번호 생성
    public static EmailKakaoNumber createEmailNumber(int memberId) {
        EmailKakaoNumber emailKakaoNumberEntity = new EmailKakaoNumber();
        emailKakaoNumberEntity.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE); // 5분 후 만료
        emailKakaoNumberEntity.expired = 0; //0:아직 유요함, 1: 만료됨
        emailKakaoNumberEntity.memberId = memberId;

        return emailKakaoNumberEntity;
    }

    // 토큰 만료
    public void setNumberToUsed() {
        this.expired = 1;
    }

    // 만료 시간 재설정 메서드
    public void resetExpiration() {
        this.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE); // 현재 시간으로부터 5분 후로 만료 시간 재설정
    }


}
