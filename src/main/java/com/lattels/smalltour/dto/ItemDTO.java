package com.lattels.smalltour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lattels.smalltour.model.Item;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.model.UpperPayment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class ItemDTO {

    @Data
    @NoArgsConstructor
    @ApiModel(value = "Item Id 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "아이템 아이디", example = "1")
        private int id;

    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "Item 등록 요청 DTO")
    public static class AddRequestDTO {

        @NotBlank(message = "필수 입력값입니다.")
        @ApiModelProperty(value = "상품 이름", example = "상품 이름")
        private String title;

        @PositiveOrZero(message = "양수 또는 0만 가능합니다.")
        @ApiModelProperty(value = "상품 가격", example = "100000")
        private int price;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "기간(달)", example = "1")
        private int period;

        @PositiveOrZero(message = "양수 또는 0만 가능합니다.")
        @ApiModelProperty(value = "상품 타입", example = "0 : 상위노출 / 1 : 배너")
        private int type;

        @PositiveOrZero(message = "양수 또는 0만 가능합니다.")
        @ApiModelProperty(value = "상품 상태", example = "0 : 공개 / 1 : 비공개")
        private int state;

    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "Item 수정 요청 DTO")
    public static class UpdateRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "아이템 아이디", example = "1")
        private int id;

        @NotBlank(message = "필수 입력값입니다.")
        @ApiModelProperty(value = "상품 이름", example = "상품 이름")
        private String title;

        @PositiveOrZero(message = "양수 또는 0만 가능합니다.")
        @ApiModelProperty(value = "상품 가격", example = "100000")
        private int price;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "기간(달)", example = "1")
        private int period;

        @PositiveOrZero(message = "양수 또는 0만 가능합니다.")
        @ApiModelProperty(value = "상품 타입", example = "0 : 상위노출 / 1 : 배너")
        private int type;

        @PositiveOrZero(message = "양수 또는 0만 가능합니다.")
        @ApiModelProperty(value = "상품 상태", example = "0 : 공개 / 1 : 비공개")
        private int state;

    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "리스트 응답 DTO")
    public static class ListResponseDTO {

        @ApiModelProperty(value = "상품 이름", example = "상품 이름")
        private String title;

        @ApiModelProperty(value = "상품 가격", example = "100000")
        private int price;

        public ListResponseDTO(Item item) {
            this.title = item.getTitle();
            this.price = item.getPrice();
        }

    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "상품 정보 응답 DTO")
    public static class ViewResponseDTO {

        @ApiModelProperty(value = "아이템 아이디", example = "1")
        private int id;

        @ApiModelProperty(value = "상품 이름", example = "상품 이름")
        private String title;

        @ApiModelProperty(value = "이미지 경로", example = "이미지 경로")
        private String imagePath;

        @ApiModelProperty(value = "상품 가격", example = "100000")
        private int price;

        @ApiModelProperty(value = "기간", example = "30")
        private int period;

        @ApiModelProperty(value = "상품 타입", example = "1")
        private int type;

        public ViewResponseDTO(Item item) {
            this.id = item.getId();
            this.title = item.getTitle();
            this.imagePath = item.getImagePath();
            this.price = item.getPrice();
            this.period = item.getPeriod();
            this.type = item.getType();
        }
    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "상품 결제 화면 응답 DTO")
    public static class PaymentViewResponseDTO {

        @ApiModelProperty(value = "아이템 아이디", example = "1")
        private int id;

        @ApiModelProperty(value = "상품 이름", example = "상품 이름")
        private String title;

        @ApiModelProperty(value = "상품 가격", example = "100000")
        private int price;

        @ApiModelProperty(value = "기간", example = "30")
        private int period;

        @ApiModelProperty(value = "상품 타입", example = "1")
        private int type;

        public PaymentViewResponseDTO(Item item) {
            this.id = item.getId();
            this.title = item.getTitle();
            this.price = item.getPrice();
            this.period = item.getPeriod();
            this.type = item.getType();
        }
    }


    @Data
    @NoArgsConstructor
    @ApiModel(value = "가이드의 결제 리스트 응답 DTO")
    public static class PaymentListResponseDTO {

        @ApiModelProperty(value = "결제 아이디", example = "1")
        private int paymentId;

        @ApiModelProperty(value = "상품 이름", example = "상품 이름")
        private String itemTitle;

        @ApiModelProperty(value = "상품 가격", example = "100000")
        private int itemPrice;

        @ApiModelProperty(value = "결제일", example = "")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime payDay;

        @ApiModelProperty(value = "만료일", example = "")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime ExpirationDate;

        @ApiModelProperty(value = "상태. 0 : 이용 중, 1: 만료", example = "1")
        private int state;

        @ApiModelProperty(value = "투어 아이디", example = "1")
        private int toursId;

        @ApiModelProperty(value = "투어 이름", example = "투어 이름입니다")
        private String toursTitle;

        public PaymentListResponseDTO(UpperPayment upperPayment) {
            this.paymentId = upperPayment.getId();
            this.payDay = upperPayment.getPayDay();
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "상위노출 tourId만가져오는 DTO")
    public static class UpperPaymentTourIdResponseDTO {

        @ApiModelProperty(value = "투어 아이디", example = "1")
        private int toursId;

        public UpperPaymentTourIdResponseDTO(UpperPayment upperPayment) {
            this.toursId = upperPayment.getTours().getId();
        }
    }



    public static class ItemType {

        // 상위노출 상품
        public static final int UPPER_PAYMENT = 0;
        // 배너 상품
        public static final int BANNER = 1;

    }

    public static class ItemState {

        // 등록됨
        public static final int PUBLIC = 0;
        // 비공개
        public static final int NONDISCLOSURE = 1;
        // 삭제
        public static final int DELETE = 2;

    }
}
