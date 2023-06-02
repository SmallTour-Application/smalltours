package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.AirlineDTO;
import com.lattels.smalltour.dto.HotelDTO;
import com.lattels.smalltour.dto.ToursDTO;
import com.lattels.smalltour.service.AirlineService;
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
@RequestMapping("/package/airline")
@Api(tags = "Airline Controller", description = "항공사 컨트롤러")
public class AirlineController {

    private final AirlineService airlineService;

    // 항공사 등록
    @PostMapping(value = "/add")
    @ApiOperation(value = "항공사 등록")
    public ResponseEntity<Object> addAirline(@ApiIgnore Authentication authentication, @Valid @RequestBody AirlineDTO.AddRequestDTO addRequestDTO) {

        airlineService.addAirline(Integer.parseInt(authentication.getPrincipal().toString()), addRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 항공사 수정
    @PostMapping(value = "/update")
    @ApiOperation(value = "항공사 수정")
    public ResponseEntity<Object> updateAirline(@ApiIgnore Authentication authentication, @Valid @RequestBody AirlineDTO.UpdateRequestDTO updateRequestDTO) {

        airlineService.updateAirline(Integer.parseInt(authentication.getPrincipal().toString()), updateRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 항공사 삭제
    @PostMapping(value = "/delete")
    @ApiOperation(value = "항공사 삭제")
    public ResponseEntity<Object> deleteAirline(@ApiIgnore Authentication authentication, @Valid @RequestBody AirlineDTO.IdRequestDTO idRequestDTO) {

        airlineService.deleteAirline(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 항공사 정보 불러오기
    @GetMapping(value = "/unauth/view")
    @ApiOperation(value = "항공사 정보 불러오기")
    public ResponseEntity<List<AirlineDTO.ViewResponseDTO>> viewAirline(@Valid ToursDTO.IdRequestDTO idRequestDTO) {

        List<AirlineDTO.ViewResponseDTO> viewResponseDTOList = airlineService.viewAirline(idRequestDTO);
        return ResponseEntity.ok().body(viewResponseDTOList);

    }

}
