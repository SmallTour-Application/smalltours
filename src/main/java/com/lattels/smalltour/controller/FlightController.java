package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.FlightDTO;
import com.lattels.smalltour.service.FlightService;
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
@RequestMapping("/package/airline/flight")
@Api(tags = "Flight Controller", description = "비행기 컨트롤러")
public class FlightController {

    private final FlightService flightService;

    // 항공사 등록
    @PostMapping(value = "/add")
    @ApiOperation(value = "비행기 정보 등록")
    public ResponseEntity<Object> addFlight(@ApiIgnore Authentication authentication, @Valid @RequestBody FlightDTO.AddRequestDTO addRequestDTO) {

        try {
            flightService.addFlight(Integer.parseInt(authentication.getPrincipal().toString()), addRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // 항공사 수정
    @PostMapping(value = "/update")
    @ApiOperation(value = "비행기 정보 수정")
    public ResponseEntity<Object> updateFlight(@ApiIgnore Authentication authentication, @Valid @RequestBody FlightDTO.UpdateRequestDTO updateRequestDTO) {

        try {
            flightService.updateFlight(Integer.parseInt(authentication.getPrincipal().toString()), updateRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // 항공사 삭제
    @PostMapping(value = "/delete")
    @ApiOperation(value = "비행기 정보 삭제")
    public ResponseEntity<Object> deleteFlight(@ApiIgnore Authentication authentication, @Valid @RequestBody FlightDTO.IdRequestDTO idRequestDTO) {

        try {
            flightService.deleteFlight(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
