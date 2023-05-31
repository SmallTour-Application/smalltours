package com.lattels.smalltour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lattels.smalltour.model.Member;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    @Getter
    @NoArgsConstructor
    public static class loginDTO{
        private String email;
        private String password;

    }

    @Getter
    @NoArgsConstructor
    public static class UpdateTel{
        private String tel;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdatePw{
        private String curPw;
        private String chgPw;

    }


    @Getter
    @NoArgsConstructor
    public static class UpdateNickName{
        private String nickname;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateProfile{
        private String profile; // 프로필 이미지가 들어있는 경로

        private List<MultipartFile> profileImgRequest;
        public boolean checkProfileImgRequestNull() {
            return this.profileImgRequest != null;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CheckEmail{
        private String email;
    }

    @Getter
    @NoArgsConstructor
    public static class ViewConfirmEmail{
        private int id;
        private String email;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Sign{


        private String password;

        private String name;

        private String nickname;

        private String email;

        private String tel;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate birthDay;


        private int gender;

        private String profile; // 프로필 이미지가 들어있는 경로

        private List<MultipartFile> profileImgRequest;

        public boolean checkProfileImgRequestNull() {
            return this.profileImgRequest != null;
        }


    }

    private String token;

    private int id; // 사용자에게 고유하게 부여되는 값

    private String password;

    private String name;

    private String nickname;

    private String email;

    private String tel;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthDay;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime joinDay;

    private int gender;

    private String profile; // 프로필 이미지가 들어있는 경로

    private int role; // 0:학생, 1:미등록가이드, 2:가이드 3:관리자

    private int state;

    private List<MultipartFile> profileImgRequest;

    private String profileImg;


    public MemberDTO(final Member member) {

        this.id = member.getId();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.tel = member.getTel();
        this.birthDay= member.getBirthDay();
        this.joinDay = member.getJoinDay();
        this.gender = member.getGender();
        this.profile = member.getProfile();
        this.role = member.getRole();
        this.state = member.getState();


    }

    // 파일 null 체크
    public boolean checkProfileImgRequestNull() {
        return this.profileImgRequest != null;
    }

    /**
     * 회원 역할
     */
    public static class MemberRole {

        // 일반 회원 (여행자)
        public static final int TRAVELER = 0;

        // 미등록 가이드
        public static final int UNREGISTERED_GUIDE = 1;

        // 가이드
        public static final int GUIDE = 2;

        // 관리자
        public static final int ADMIN = 3;

    }

}
