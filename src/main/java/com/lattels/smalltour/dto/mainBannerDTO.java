package com.lattels.smalltour.dto;

import com.lattels.smalltour.dto.main.PopularGuideDTO;
import com.lattels.smalltour.model.Hotel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class mainBannerDTO {

    private int count;
    private List<mainBannerDTO.BannerDTO> content;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BannerDTO {
        private String bannerImg;
        private int tourId;

    }

}
