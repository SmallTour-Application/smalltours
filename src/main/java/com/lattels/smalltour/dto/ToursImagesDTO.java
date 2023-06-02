package com.lattels.smalltour.dto;

import com.lattels.smalltour.model.ToursImages;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
public class ToursImagesDTO {

    @Data
    @NoArgsConstructor
    @ApiModel(value = "투어 이미지 응답 DTO")
    public static class ViewResponseDTO {

        @ApiModelProperty(value = "투어 썸네일", example = "투어 썸네일입니다")
        private String imagePath;

    }
}
