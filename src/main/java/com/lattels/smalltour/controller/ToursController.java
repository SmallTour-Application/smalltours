package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.FlightDTO;
import com.lattels.smalltour.dto.ToursDTO;
import com.lattels.smalltour.service.ToursService;
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
@RequestMapping("/package")
@Api(tags = "Tours Controller", description = "투어 컨트롤러")
public class ToursController {

    private final ToursService toursService;

    // 투어 등록
    @PostMapping(value = "/add")
    @ApiOperation(value = "투어 등록")
    public ResponseEntity<Object> addTours(@ApiIgnore Authentication authentication, @Valid @RequestBody ToursDTO.AddRequestDTO addRequestDTO) {

        toursService.addTours(Integer.parseInt(authentication.getPrincipal().toString()), addRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 투어 삭제
    @PostMapping(value = "/delete")
    @ApiOperation(value = "투어 삭제")
    public ResponseEntity<Object> deleteTours(@ApiIgnore Authentication authentication, @Valid @RequestBody ToursDTO.IdRequestDTO idRequestDTO) {

        toursService.deleteTours(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().build();

    }


}
