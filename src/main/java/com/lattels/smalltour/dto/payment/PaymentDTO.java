package com.lattels.smalltour.dto.payment;

import com.lattels.smalltour.model.Payment;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 결제 정보 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {




    @ApiParam("결제 ID")
    private int paymentId;
    
    @ApiParam("패키지 썸네일 이미지 경로")
    private String thumb;

    @ApiParam("패키지 ID")
    private int packageId;

    @ApiParam("패키지 이름")
    private String packageName;

    @ApiParam("패키지 부제목")
    private String packageSubTitle;

    @ApiParam("패키지 가격")
    private int price;

    @ApiParam("인원 수")
    private int people;

    @ApiParam("여행 출발일")
    private LocalDate departureDay;

    @ApiParam("결제 상태 (0: 미결제, 1: 결제)")
    private int state;

    @ApiParam("가이드 ID")
    private int guideId;

    @ApiParam("가이드 이름")
    private String guideName;

    @ApiParam("결제일")
    private LocalDateTime payDay;

    @ApiParam("구매자 닉네임")
    private String memberNickname;

    @ApiParam("구매자 전화번호")
    private String memberTel;

    @ApiParam("리뷰 작성 가능 여부")
    private boolean canReview;

    @ApiParam("가이드 리뷰 작성 가능 여부")
    private boolean canGuideReview;

    public PaymentDTO(Payment payment, String thumbUrl,boolean canReview, boolean canGuideReview) {
        this.paymentId = payment.getId();
        this.thumb = thumbUrl;
        this.packageId = payment.getTours().getId();
        this.packageName = payment.getTours().getTitle();
        this.packageSubTitle = payment.getTours().getSubTitle();
        this.price = payment.getPrice();
        this.people = payment.getPeople();
        this.departureDay = payment.getDepartureDay();
        this.state = payment.getState();
        this.payDay = payment.getPaymentDay();
        this.guideId = payment.getTours().getGuide().getId();
        this.guideName = payment.getTours().getGuide().getName();
        this.memberNickname = payment.getMember().getNickname();
        this.memberTel = payment.getMember().getTel();
        this.canReview = canReview;
        this.canGuideReview = canGuideReview;
    }

}
