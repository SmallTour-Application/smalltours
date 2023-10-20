package com.lattels.smalltour.dto.admin.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAddMemberDTO {
    private AddMember addMember;
    private LocalDateTime joinDay;
    private int state;
    private int id;
    private String email;
    private String password;
    private String name;
    private String nickName;
    private String tel;
    private LocalDate birthDay;
    private int gender;
    private int role;
    //profile은 null로들어가게하고 본인이 직접 수정할수있게
    private String profile;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddMember {
        private String email;
        private String password;
        private String name;
        private String nickName;
        private String tel;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthDay;
        private int gender;
        private int role;

        private String profile; // 프로필 이미지가 들어있는 경로

        private List<MultipartFile> profileImgRequest;

        public boolean checkProfileImgRequestNull() {
            return this.profileImgRequest != null;
        }
    }


}