package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.HotelDTO;
import com.lattels.smalltour.dto.ScheduleDTO;
import com.lattels.smalltour.dto.ToursDTO;
import com.lattels.smalltour.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/package/schedule")
@Api(tags = "Schedule Controller", description = "여행 일정 컨트롤러")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 여행 일정 등록
    @PostMapping(value = "/add")
    @ApiOperation(value = "여행 일정 등록")
    public ResponseEntity<Object> addSchedule(@ApiIgnore Authentication authentication, @Valid @RequestBody ScheduleDTO.AddRequestDTO addRequestDTO) {

        scheduleService.addSchedule(Integer.parseInt(authentication.getPrincipal().toString()), addRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 여행 일정 수정
    @PostMapping(value = "/update")
    @ApiOperation(value = "여행 일정 수정")
    public ResponseEntity<Object> updateSchedule(@ApiIgnore Authentication authentication, @Valid @RequestBody ScheduleDTO.UpdateRequestDTO updateRequestDTO) {

        scheduleService.updateSchedule(Integer.parseInt(authentication.getPrincipal().toString()), updateRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 여행 일정 삭제
    @PostMapping(value = "/delete")
    @ApiOperation(value = "여행 일정 삭제")
    public ResponseEntity<Object> deleteSchedule(@ApiIgnore Authentication authentication, @Valid @RequestBody ScheduleDTO.IdRequestDTO idRequestDTO) {

        scheduleService.deleteSchedule(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 여행 일정 정보 불러오기
    @GetMapping(value = "/unauth/view")
    @ApiOperation(value = "여행 일정 정보 불러오기")
    public ResponseEntity<List<ScheduleDTO.ViewResponseDTO>> viewSchedule(@Valid ToursDTO.IdRequestDTO idRequestDTO) {

        List<ScheduleDTO.ViewResponseDTO> viewResponseDTOList = scheduleService.viewSchedule(idRequestDTO);
        return ResponseEntity.ok().body(viewResponseDTOList);

    }

}
