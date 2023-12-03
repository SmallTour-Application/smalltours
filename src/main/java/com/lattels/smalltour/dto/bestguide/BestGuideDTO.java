package com.lattels.smalltour.dto.bestguide;

import com.lattels.smalltour.model.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class BestGuideDTO {

    @Data
    @NoArgsConstructor
    @ApiModel(value = "가이드 ID DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "BestGuide ID", example = "1")
        private int id;

    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "가이드 검색 DTO")
    public static class SearchRequestDTO {

        @ApiModelProperty(value = "search type", example = "0 : 없음 / 1 : 이름 / 2 : 닉네임 / 3 : 이메일")
        private int type;

        @ApiModelProperty(value = "검색어", example = "aaa")
        private String word;

        /**
         * 검색 타입
         */
        public static class SearchType {

            public static final int NONE = 0;
            public static final int BY_NAME = 1;
            public static final int BY_NICKNAME = 2;
            public static final int BY_EMAIL = 3;

        }
    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "가이드 정보 응답 DTO")
    public static class GuideInfoResponseDTO {

        @NotBlank(message = "필수 입력값입니다.")
        @ApiModelProperty(value = "이메일", example = "a@.com")
        private String email;

        @NotBlank(message = "필수 입력값입니다.")
        @ApiModelProperty(value = "닉네임", example = "닉네임")
        private String nickname;

        @NotBlank(message = "필수 입력값입니다.")
        @ApiModelProperty(value = "전화번호", example = "010-0606")
        private String tel;

        @ApiModelProperty(value = "생일", example = "2023-02-02")
        private LocalDate birthDay;

        @NotBlank(message = "필수 입력값입니다.")
        @ApiModelProperty(value = "성별", example = "1")
        private int gender;

        @ApiModelProperty(value = "가입일", example = "2023-02-02")
        private LocalDateTime joinDay;

        @PositiveOrZero(message = "0과 양수만 입력가능합니다.")
        @ApiModelProperty(value = "우수가이드", example = "1")
        private boolean bestGuide;

        public GuideInfoResponseDTO(Member member) {
            this.email = member.getEmail();
            this.nickname = member.getNickname();
            this.tel = member.getTel();
            this.birthDay = member.getBirthDay();
            this.gender = member.getGender();
            this.joinDay = member.getJoinDay();
        }

    }

}
