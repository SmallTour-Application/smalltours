package com.lattels.smalltour.dto;

import com.lattels.smalltour.model.ScheduleItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@Data
@Builder
public class ScheduleItemDTO {

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "일정 옵션 ID 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "일정 옵션 ID", example = "1")
        private int id;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "일정 옵션 등록 요청 DTO")
    public static class AddRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "여행 일정 ID", example = "1")
        private int scheduleId;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "옵션 이름", example = "옵션 이름입니다")
        private String title;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "옵션 설명", example = "옵션 설명입니다")
        private String content;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "가격", example = "1")
        private int price;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "고정 옵션 (0: 고정, 1: X)", example = "1")
        private int defaultItem;

        @ApiModelProperty(value = "경도", example = "12.12")
        private double locationX;

        @ApiModelProperty(value = "위도", example = "12.12")
        private double locationY;

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "일정 옵션 수정 요청 DTO")
    public static class UpdateRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "일정 옵션 ID", example = "1")
        private int id;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "옵션 이름", example = "옵션 이름입니다")
        private String title;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "옵션 설명", example = "옵션 설명입니다")
        private String content;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "가격", example = "1")
        private int price;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "고정 옵션 (0: 고정, 1: X)", example = "1")
        private int defaultItem;

        @ApiModelProperty(value = "경도", example = "12.12")
        private double locationX;

        @ApiModelProperty(value = "위도", example = "12.12")
        private double locationY;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "일정 옵션 정보 응답 DTO")
    public static class ViewResponseDTO {

        @ApiModelProperty(value = "일정 옵션 ID", example = "1")
        private int id;

        @ApiModelProperty(value = "옵션 이름", example = "옵션 이름입니다")
        private String title;

        @ApiModelProperty(value = "옵션 설명", example = "옵션 설명입니다")
        private String content;

        @ApiModelProperty(value = "가격", example = "1")
        private int price;

        @ApiModelProperty(value = "고정 옵션 (0: 고정, 1: X)", example = "1")
        private int defaultItem;

        @ApiModelProperty(value = "경도", example = "12.12")
        private double locationX;

        @ApiModelProperty(value = "위도", example = "12.12")
        private double locationY;

        public ViewResponseDTO(ScheduleItem scheduleItem) {
            this.id = scheduleItem.getId();
            this.title = scheduleItem.getTitle();
            this.content = scheduleItem.getContent();
            this.price = scheduleItem.getPrice();
            this.defaultItem = scheduleItem.getDefaultItem();
            this.locationX = scheduleItem.getLocationX();
            this.locationY = scheduleItem.getLocationY();
        }
    }


}
