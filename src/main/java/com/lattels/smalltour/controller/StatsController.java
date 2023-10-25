package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.StatsDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.service.StatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

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
    /*@PostMapping(value = "/member-per-month")
    @ApiOperation(value = "월별 가입 수")
    public ResponseEntity<StatsDTO.MemberPerMonthDTO> getMemberPerMonth(@ApiIgnore Authentication authentication) {

        try {
            StatsDTO.MemberPerMonthDTO MemberPerMonthDTO = statsService.getMemberPerMonth(authentication);
            return ResponseEntity.ok().body(MemberPerMonthDTO);
        } catch (Exception e) {
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }*/

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
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }



}
