
package com.lattels.smalltour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class MemberAndGuideProfileDTO {

    private int id;

    private String password;

    private String name;

    private String nickname;

    private String email;

    private String tel;

    private int role;

    private int state;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime joinDay;


    private int gender;


    private String profile; // 프로필 이미지가 들어있는 경로

    private List<MultipartFile> profileImgRequest;

    public boolean checkProfileImgRequestNull() {
        return this.profileImgRequest != null;
    }

    private int guideId;
    private String resume; //이력서 경로
    private String introduce; // 자기소개
    private String portfolioPath;// 포트폴리오 경로

    private List<MultipartFile> resumeFile;
    private List<MultipartFile> portfolioFile;
    public boolean checkResumeFileRequestNull() {
        return this.resumeFile != null;
    }
    public boolean checkPortfolioFileRequestNull() {
        return this.portfolioFile != null;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SignUp{


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
        private String resume; //이력서 경로
        private String introduce; // 자기소개
        private String portfolioPath;// 포트폴리오 경로

        private List<MultipartFile> resumeFile;
        private List<MultipartFile> portfolioFile;
        public boolean checkResumeFileRequestNull() {
            return this.resumeFile != null;
        }
        public boolean checkPortfolioFileRequestNull() {
            return this.portfolioFile != null;
        }
    }
    @Getter
    @NoArgsConstructor
    public static class loginDTO{
        private String email;
        private String password;
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
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateResumePortfolio{
        private String resume; //이력서 경로
        private String portfolioPath;// 포트폴리오 경로

        private List<MultipartFile> resumeFile;
        private List<MultipartFile> portfolioFile;
        public boolean checkResumeFileRequestNull() {
            return this.resumeFile != null;
        }
        public boolean checkPortfolioFileRequestNull() {
            return this.portfolioFile != null;
        }
    }



}

