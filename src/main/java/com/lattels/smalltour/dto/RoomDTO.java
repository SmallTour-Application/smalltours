package com.lattels.smalltour.dto;

import com.lattels.smalltour.model.Room;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Data
@Builder
public class RoomDTO {

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "호텔 방 ID 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "호텔 방 ID", example = "1")
        private int id;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "호텔 방 등록 요청 DTO")
    public static class AddRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "호텔 ID", example = "1")
        private int hotelId;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "방 이름", example = "방 이름입니다")
        private String name;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "방 가격", example = "10000")
        private int price;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "최소 수용인원", example = "2")
        private int minPeople;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "최대 수용인원", example = "4")
        private int maxPeople;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "방 설명", example = "방 설명입니다")
        private String description;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "추가 금액", example = "10000")
        private int addPrice;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "호텔 방 수정 요청 DTO")
    public static class UpdateRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "방 ID", example = "1")
        private int id;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "방 이름", example = "방 이름입니다")
        private String name;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "방 가격", example = "10000")
        private int price;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "최소 수용인원", example = "2")
        private int minPeople;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "최대 수용인원", example = "4")
        private int maxPeople;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "방 설명", example = "방 설명입니다")
        private String description;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "추가 금액", example = "10000")
        private int addPrice;

    }


    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "호텔 방 정보 응답 DTO")
    public static class ViewResponseDTO {

        @ApiModelProperty(value = "호텔 방 ID", example = "1")
        private int id;

        @ApiModelProperty(value = "방 이름", example = "방 이름입니다")
        private String name;

        @ApiModelProperty(value = "방 가격", example = "10000")
        private int price;

        @ApiModelProperty(value = "최소 수용인원", example = "2")
        private int minPeople;

        @ApiModelProperty(value = "최대 수용인원", example = "4")
        private int maxPeople;

        @ApiModelProperty(value = "방 설명", example = "방 설명입니다")
        private String description;

        @ApiModelProperty(value = "방 이미지", example = "방 이미지입니다")
        private String image;

        @ApiModelProperty(value = "추가 금액", example = "10000")
        private int addPrice;

        public ViewResponseDTO(Room room) {
            this.id = room.getId();
            this.name = room.getName();
            this.price = room.getPrice();
            this.minPeople = room.getMinPeople();
            this.maxPeople = room.getMaxPeople();
            this.description = room.getDescription();
            this.image = room.getImage();
            this.addPrice = room.getAddPrice();
        }

    }


}
