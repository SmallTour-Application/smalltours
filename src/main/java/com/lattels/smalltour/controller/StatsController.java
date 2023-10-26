package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.stats.SiteProfitDTO;
import com.lattels.smalltour.dto.stats.TotalCntPerMonthDTO;
import com.lattels.smalltour.dto.stats.StatsDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.service.StatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
@Api(tags = "Stats Controller", description = "가이드 통계 컨트롤러")
public class StatsController {

    private final StatsService statsService;


    /*
    * 강사 통계 보기
    */
    @PostMapping(value = "/view")
    @ApiOperation(value = "통계 보기")
    public ResponseEntity<StatsDTO.StatsResponseDTO> viewStats(@ApiIgnore Authentication authentication, @RequestBody StatsDTO.DateRequestDTO dateRequestDTO) {

        StatsDTO.StatsResponseDTO statsResponseDTO = statsService.viewStats(Integer.parseInt(authentication.getPrincipal().toString()), dateRequestDTO);
        return ResponseEntity.ok().body(statsResponseDTO);

    }

    /*
    * 월별 가입 수 가져오기
    */
    @PostMapping(value = "/member-per-month")
    @ApiOperation(value = "월별 가입 수")
    public ResponseEntity<List<TotalCntPerMonthDTO>> getMemberPerMonth(@ApiIgnore Authentication authentication) {

        try {
            List<TotalCntPerMonthDTO> responseDTOList = statsService.getMemberPerMonth(authentication);
            return ResponseEntity.ok().body(responseDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }

    /*
    * 월별 예약 수 가져오기
    */
    @PostMapping(value = "/payment-per-month")
    @ApiOperation(value = "월별 가입 수")
    public ResponseEntity<List<TotalCntPerMonthDTO>> getPaymentPerMonth(@ApiIgnore Authentication authentication) {

        try {
            List<TotalCntPerMonthDTO> responseDTOList = statsService.getPaymentPerMonth(authentication);
            return ResponseEntity.ok().body(responseDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }

    /*
    * 현재 총 회원 수
    */
    @PostMapping(value = "/total-members")
    @ApiOperation(value = "현재 총 회원 수")
    public ResponseEntity<StatsDTO.TotalMembersDTO> getTotalMembers(@ApiIgnore Authentication authentication) {

        try {
            StatsDTO.TotalMembersDTO totalMembers = statsService.getTotalMembers(authentication);
            return ResponseEntity.ok().body(totalMembers);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }

    /*
    * 기간 동안의 판매 비율 가져오기
    */
    @PostMapping(value = "/total-volume-percentage")
    @ApiOperation(value = "기간 동안의 판매 비율 가져오기")
    public ResponseEntity<StatsDTO.TotalVolumePercentageResponseDTO> getTotalVolumePercentageList(@ApiIgnore Authentication authentication, @RequestBody StatsDTO.DateRequestDTO requestDTO) {

        try {
            StatsDTO.TotalVolumePercentageResponseDTO responseDTO = statsService.getTotalVolumePercentageList(authentication, requestDTO);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }

    /*
    * 기간 동안의 사이트 수익 가져오기
    */
    @PostMapping(value = "/site-profit")
    @ApiOperation(value = "기간 동안의 사이트 수익 가져오기")
    public ResponseEntity<SiteProfitDTO> getSiteProfit(@ApiIgnore Authentication authentication, @RequestBody StatsDTO.DateRequestDTO requestDTO) {

        try {
            SiteProfitDTO responseDTO = statsService.getSiteProfit(authentication, requestDTO);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }



}
