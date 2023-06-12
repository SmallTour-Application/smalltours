package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.ScheduleDTO;
import com.lattels.smalltour.dto.ScheduleItemDTO;
import com.lattels.smalltour.service.ScheduleItemService;
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

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/package/schedule/item")
@Api(tags = "Schedule Item Controller", description = "여행 일정 옵션 컨트롤러")
public class ScheduleItemController {

    private final ScheduleItemService scheduleItemService;


    // 일정 옵션 등록
    @PostMapping(value = "/add")
    @ApiOperation(value = "일정 옵션 등록")
    public ResponseEntity<Object> addScheduleItem(@ApiIgnore Authentication authentication, @Valid @RequestBody ScheduleItemDTO.AddRequestDTO addRequestDTO) {

        scheduleItemService.addScheduleItem(Integer.parseInt(authentication.getPrincipal().toString()), addRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 일정 옵션 수정
    @PostMapping(value = "/update")
    @ApiOperation(value = "일정 옵션 수정")
    public ResponseEntity<Object> updateScheduleItem(@ApiIgnore Authentication authentication, @Valid @RequestBody ScheduleItemDTO.UpdateRequestDTO updateRequestDTO) {

        scheduleItemService.updateScheduleItem(Integer.parseInt(authentication.getPrincipal().toString()), updateRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 일정 옵션 삭제
    @PostMapping(value = "/delete")
    @ApiOperation(value = "일정 옵션 삭제")
    public ResponseEntity<Object> deleteScheduleItem(@ApiIgnore Authentication authentication, @Valid @RequestBody ScheduleItemDTO.IdRequestDTO idRequestDTO) {

        scheduleItemService.deleteScheduleItem(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().build();

    }


    /*
    * 일정 아이템 기본값 변경*/
    @PostMapping(value = "/default_item")
    @ApiOperation(value = "일정 옵션 기본값 수정")
    public ResponseEntity<Object> setDefaultItem(@ApiIgnore Authentication authentication, @Valid @RequestBody ScheduleItemDTO.IdRequestDTO idRequestDTO) {

        scheduleItemService.setDefaultItem(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().build();

    }

}
