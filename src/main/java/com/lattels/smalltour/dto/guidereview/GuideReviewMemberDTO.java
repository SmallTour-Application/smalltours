package com.lattels.smalltour.dto.guidereview;

import com.lattels.smalltour.dto.MemberDTO;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideReviewMemberDTO {

    @ApiParam("닉네임")
    private String nickname;

    @ApiParam("프로필 이미지 경로")

    private String profile;

    public GuideReviewMemberDTO(MemberDTO memberDTO) {
        this.nickname = memberDTO.getNickname();
        this.profile = memberDTO.getProfile();
    }

}
