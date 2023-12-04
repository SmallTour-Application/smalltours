package com.lattels.smalltour.controller.admin;

import com.lattels.smalltour.dto.admin.payment.AdminGuidePaymentListDTO;
import com.lattels.smalltour.dto.admin.payment.AdminPaymentListDTO;
import com.lattels.smalltour.service.admin.AdminPackageService;
import com.lattels.smalltour.service.admin.AdminPaymentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/payment")
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    @ApiOperation("payment 목록 조회, page는 1페이지부터 시작")
    @GetMapping("/list")
    public ResponseEntity<AdminGuidePaymentListDTO> getToursList(@ApiIgnore Authentication authentication,
                                                                                 @RequestParam(required = false)String name,
                                                                                 @RequestParam(required = false) String tourName,
                                                                                 // 시작일
                                                                                 @RequestParam(required = false) String startDate,
                                                                                 // 종료일
                                                                                 @RequestParam(required = false)String endDate,
                                                                                int page)
    {
        try{
            // String으로 들어온 날짜를 LocalDate로 변환
            LocalDate startDay = startDate != null ? LocalDate.parse(startDate) : null;
            LocalDate endDay = endDate != null ? LocalDate.parse(endDate) : null;
            log.info("startDay : " + startDay + ", endDay : " + endDay);
            // 질문 목록 조회
            AdminGuidePaymentListDTO adminPackageDTO = adminPaymentService.getToursList(authentication,name,tourName,startDay,endDay,page);
            return ResponseEntity.ok(adminPackageDTO);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}