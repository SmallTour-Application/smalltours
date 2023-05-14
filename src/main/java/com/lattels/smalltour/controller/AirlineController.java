package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.AirlineDTO;
import com.lattels.smalltour.service.AirlineService;
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
@RequestMapping("/package/airline")
@Api(tags = "Airline Controller", description = "항공사 컨트롤러")
public class AirlineController {

    private final AirlineService airlineService;

    // 항공사 등록
    @PostMapping(value = "/add")
    @ApiOperation(value = "항공사 등록")
    public ResponseEntity<Object> addAirline(@ApiIgnore Authentication authentication, @Valid @RequestBody AirlineDTO.AddRequestDTO addRequestDTO) {

        try {
            airlineService.addAirline(Integer.parseInt(authentication.getPrincipal().toString()), addRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // 항공사 수정
    @PostMapping(value = "/update")
    @ApiOperation(value = "항공사 수정")
    public ResponseEntity<Object> updateAirline(@ApiIgnore Authentication authentication, @Valid @RequestBody AirlineDTO.UpdateRequestDTO updateRequestDTO) {

        try {
            airlineService.updateAirline(Integer.parseInt(authentication.getPrincipal().toString()), updateRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // 항공사 삭제
    @PostMapping(value = "/delete")
    @ApiOperation(value = "항공사 삭제")
    public ResponseEntity<Object> deleteAirline(@ApiIgnore Authentication authentication, @Valid @RequestBody AirlineDTO.IdRequestDTO idRequestDTO) {

        try {
            airlineService.deleteAirline(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
