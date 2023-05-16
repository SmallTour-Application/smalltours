package com.lattels.smalltour.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "tel")
    private String tel;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    @Column(name = "join_day")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDay;

    @Column(name = "gender")
    private int gender;
    @Column(name = "profile")
    private String profile; // 프로필 이미지가 들어있는 경로

    @Column(name = "role")
    private int role; // 0:학생, 1:선생, 2:관리자

    @Column(name = "state")
    private int state;


    public void changePassword(String password){
        this.password = password;
    }

}
