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
@Table(name = "log_member")
public class LogMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Column(name = "login_date_time")
    private LocalDateTime loginDateTime; //로그인 시간
 
    @Column(name = "connection_type")
    private String connectionType;  //접속유형(웹,앱..)

    @Column(name = "os")
    private String os; //운영체제 환경 (윈도우 ,리눅스..)

    @Column(name = "browser")
    private String browser; //접속 브라우저(환경)

    @Column(name = "ip")
    private String ip;

    @Column(name = "state")
    private int state; //0:성공 1:실패

    @Column(name = "region")
    private String region;
}
