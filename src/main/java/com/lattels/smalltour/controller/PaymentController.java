package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.payment.*;
import com.lattels.smalltour.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "결제 API 컨트롤러")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class PaymentController {

    // 페이지당 조회할 결제 내역 수
    private static final int NUMBER_OF_PAYMENT_PER_PAGE = 10;

    private final PaymentService paymentService;

    @ApiOperation("결제")
    @PostMapping("/ok")
    public ResponseEntity<?> paymentOk(@ApiIgnore Authentication authentication, PaymentOkRequestDTO paymentOkRequestDTO) {
        paymentService.paymentOk(authentication, paymentOkRequestDTO);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("결제 취소")
    @PostMapping("/cancel")
    public ResponseEntity<PaymentCancelDTO> paymentCancel(@ApiIgnore Authentication authentication, int paymentId) {
        PaymentCancelDTO paymentCancelDTO = paymentService.paymentCancel(authentication, paymentId);

        return ResponseEntity.ok(paymentCancelDTO);
    }

    @ApiOperation("내 결제 목록")
    @PostMapping("/myPayment")
    public ResponseEntity<PaymentListDTO> getMyPayment(@ApiIgnore Authentication authentication, int page) {
        PaymentListDTO paymentListDTO = paymentService.getMyPayment(authentication, page - 1, NUMBER_OF_PAYMENT_PER_PAGE);

        return ResponseEntity.ok(paymentListDTO);
    }

    @PostMapping("/info")
    public ResponseEntity<PaymentMemberInfoDTO> getPaymentMemberInfo(@ApiIgnore Authentication authentication, @RequestParam int paymentId) {
        PaymentMemberInfoDTO paymentMemberInfoDTO = paymentService.getPaymentMemberInfo(authentication, paymentId);
        return ResponseEntity.ok(paymentMemberInfoDTO);
    }

    @ApiOperation("결제 가격 확인")
    @PostMapping("/check")
    public ResponseEntity<Integer> paymentCheck(@ApiIgnore Authentication authentication, PaymentCheckRequestDTO paymentCheckRequestDTO) {
        int price = paymentService.checkPrice(paymentCheckRequestDTO);

        return ResponseEntity.ok(price);
    }

    @ApiOperation("내 결제 내역 검색")
    @PostMapping("/search")
    public ResponseEntity<PaymentListDTO> paymentSearch(@ApiIgnore Authentication authentication, PaymentSearchRequestDTO paymentSearchRequestDTO) {
        paymentSearchRequestDTO.setPage(paymentSearchRequestDTO.getPage() - 1);

        if (paymentSearchRequestDTO.getKeyword() == null) {
            paymentSearchRequestDTO.setKeyword("");
        }

        PaymentListDTO paymentListDTO = paymentService.searchMyPayment(authentication, paymentSearchRequestDTO, NUMBER_OF_PAYMENT_PER_PAGE);

        return ResponseEntity.ok(paymentListDTO);
    }

    @ApiOperation("패키지 판매 내역 조회")
    @PostMapping("/sell")
    public ResponseEntity<PaymentListDTO> paymentSell(@ApiIgnore Authentication authentication, PaymentSearchRequestDTO paymentSearchRequestDTO) {
        paymentSearchRequestDTO.setPage(paymentSearchRequestDTO.getPage() - 1);

        if (paymentSearchRequestDTO.getKeyword() == null) {
            paymentSearchRequestDTO.setKeyword("");
        }

        PaymentListDTO paymentListDTO = paymentService.searchGuidePaymentSell(authentication, paymentSearchRequestDTO, NUMBER_OF_PAYMENT_PER_PAGE);

        return ResponseEntity.ok(paymentListDTO);
    }

}