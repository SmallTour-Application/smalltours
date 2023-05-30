package com.lattels.smalltour.controller;


import com.lattels.smalltour.dto.GuideScheduleDTO;
import com.lattels.smalltour.service.GuideScheduleService;
import com.lattels.smalltour.service.MainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
@Api(tags = "scheduleGuide", description = "가이드 스케줄러 컨트롤러")
//schedule/guide,해당 가이드가 내 상품을 산 사람 정보 가져오는 서비스, memberNickname, memberTel
public class GuideScheduleController {
    private final GuideScheduleService guideScheduleService;

    @ApiOperation(value = "가이드스케줄")
    @GetMapping("/guide")
    public List<GuideScheduleDTO> getGuideSchedules(
            @ApiIgnore Authentication authentication,
            @RequestParam(value = "startDay") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDay,
            @RequestParam(value = "endDay") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDay
    ) {
        int guideId = Integer.parseInt(authentication.getPrincipal().toString());
        return guideScheduleService.getGuideSchedules(guideId, startDay, endDay);
    }

}