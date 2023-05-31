package com.lattels.smalltour.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class favoriteGuideDTO {
    private int guideId;
    private String guideName;
    private String guideImg;
    private int favorite;  //해당 가이드의 좋아요 수

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    //가이드에 대해 좋아요 누르기 혹은 취소하기
    public static class favoriteDTO{
        private int memberId;
        private int guideId;
    }
}
