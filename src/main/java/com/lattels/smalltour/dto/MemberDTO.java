package com.lattels.smalltour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lattels.smalltour.model.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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

    @Data
    @NoArgsConstructor
    @ApiModel(value = "이름 요청 DTO")
    public static class NameRequestDTO {

        @NotBlank(message = "필수 입력값입니다.")
        @ApiModelProperty(value = "이름", example = "name")
        private String name;

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

    @Data
    @NoArgsConstructor
    @ApiModel(value = "투어 정보에 들어가는 프로필 응답 DTO")
    public static class ProfileResponseDTO{

        @ApiModelProperty(value = "가이드 ID", example = "1")
        private int guideId;

        @ApiModelProperty(value = "가이드 닉네임", example = "닉네임입니다.")
        private String nickname;

        @ApiModelProperty(value = "프로필 이미지", example = "프로필 이미지입니다")
        private String profile;

        public ProfileResponseDTO(Member member) {
            this.guideId = member.getId();
            this.nickname = member.getNickname();
        }
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

    private String bestGuide;

    private String kakaoConnect;


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

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "Member ID 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "유저 ID", example = "1")
        private int memberId;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "권한 업데이트 요청 DTO")
    public static class RoleUpdateRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "유저 ID", example = "1")
        private int memberId;

        @PositiveOrZero(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "권한", example = "1")
        private int role;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "권한설정 페이지 회원 리스트 DTO")
    public static class RoleSettingResponseDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "유저 ID", example = "1")
        private int memberId;

        @PositiveOrZero(message = "0과 양수만 가능합니다.")
        @ApiModelProperty(value = "권한", example = "1")
        private int role;

        @NotBlank(message = "필수 입력값입니다.")
        @ApiModelProperty(value = "이름", example = "name")
        private String name;

        @NotBlank(message = "필수 입력값입니다.")
        @ApiModelProperty(value = "닉네임", example = "nickname")
        private String nickname;

        @NotBlank(message = "필수 입력값입니다.")
        @ApiModelProperty(value = "이메일", example = "ex@gmail.com")
        private String email;

        @NotBlank(message = "필수 입력값입니다.")
        @ApiModelProperty(value = "전화번호", example = "010-0000-0000")
        private String tel;

        @ApiModelProperty(value = "가입일", example = "")
        private LocalDateTime joinDay;

        @PositiveOrZero(message = "0과 양수만 가능합니다.")
        @ApiModelProperty(value = "상태", example = "1")
        private int state;

        public RoleSettingResponseDTO(Member member) {
            this.memberId = member.getId();
            this.role = member.getRole();
            this.name = member.getName();
            this.nickname = member.getNickname();
            this.email = member.getEmail();
            this.tel = member.getTel();
            this.joinDay = member.getJoinDay();
            this.state = member.getState();
        }
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
