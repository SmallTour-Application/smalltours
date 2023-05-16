package com.lattels.smalltour.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class ToursDTO {

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "투어 ID 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 ID", example = "1")
        private int id;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "투어 등록 요청 DTO")
    public static class AddRequestDTO {

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "투어 이름", example = "투어 이름입니다")
        private String title;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "투어 부제목", example = "투어 부제목입니다")
        private String subTitle;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "투어 설명", example = "투어 설명입니다")
        private String description;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "만나는 장소(시간)", example = "만나는 장소 (시간)")
        private String meetingPoint;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "투어 기간", example = "1")
        private int duration;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "투어 가격", example = "10000")
        private int price;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 최대 인원", example = "1")
        private int maxGroupSize;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 최소 인원", example = "1")
        private int minGroupSize;

        @ApiModelProperty(value = "생성일", example = "2023-03-03T09:23:00.000")
        private LocalDateTime createdDay;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "투어 기본 가격", example = "10000")
        private int defaultPrice;

        // 투어 썸네일 이미지
        private List<MultipartFile> thumb;

        // 투어 정보 이미지 리스트
        private List<MultipartFile> tourImages;

    }


}
