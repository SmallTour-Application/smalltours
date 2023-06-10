package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.EducationLogDTO;
import com.lattels.smalltour.service.EducationLogService;
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
@RequestMapping("/education/log")
@Api(tags = "Education Log Controller", description = "가이드 교육 로그 컨트롤러")
public class EducationLogController {

    private final EducationLogService educationLogService;


    /*
     * 비디오 시청 결과
     */
    @PostMapping(value = "/view/result")
    @ApiOperation(value = "비디오 시청 결과")
    public ResponseEntity<Object> saveViewedResult(@ApiIgnore Authentication authentication, @Valid @RequestBody EducationLogDTO.ViewedResultRequestDTO viewedResultRequestDTO) {

        educationLogService.saveViewedResult(Integer.parseInt(authentication.getPrincipal().toString()), viewedResultRequestDTO);
        return ResponseEntity.ok().build();

    }

}
