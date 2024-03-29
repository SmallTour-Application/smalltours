package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.HotelDTO;
import com.lattels.smalltour.dto.ToursDTO;
import com.lattels.smalltour.service.HotelService;
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
@RequestMapping("/package/hotel")
@Api(tags = "Hotel Controller", description = "호텔 컨트롤러")
public class HotelController {

    private final HotelService hotelService;


    // 호텔 등록
    @PostMapping(value = "/add")
    @ApiOperation(value = "호텔 등록")
    public ResponseEntity<Object> addHotel(@ApiIgnore Authentication authentication, @Valid @RequestBody HotelDTO.AddRequestDTO addRequestDTO) {

        hotelService.addHotel(Integer.parseInt(authentication.getPrincipal().toString()), addRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 호텔 수정
    @PostMapping(value = "/update")
    @ApiOperation(value = "호텔 수정")
    public ResponseEntity<Object> updateHotel(@ApiIgnore Authentication authentication, @Valid @RequestBody HotelDTO.UpdateRequestDTO updateRequestDTO) {

        hotelService.updateHotel(Integer.parseInt(authentication.getPrincipal().toString()), updateRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 호텔 삭제
    @PostMapping(value = "/delete")
    @ApiOperation(value = "호텔 삭제")
    public ResponseEntity<Object> deleteHotel(@ApiIgnore Authentication authentication, @Valid @RequestBody HotelDTO.IdRequestDTO idRequestDTO) {

        hotelService.deleteHotel(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 호텔 정보불러오기
    @GetMapping(value = "/unauth/view")
    @ApiOperation(value = "호텔 정보 불러오기")
    public ResponseEntity<List<HotelDTO.ViewResponseDTO>> viewHotel(@Valid ToursDTO.IdRequestDTO idRequestDTO) {

        List<HotelDTO.ViewResponseDTO> viewResponseDTOList = hotelService.viewHotel(idRequestDTO);
        return ResponseEntity.ok().body(viewResponseDTOList);

    }

}
