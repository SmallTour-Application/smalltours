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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public ResponseEntity<Object> addTours(@ApiIgnore Authentication authentication,
                                           @Valid ToursDTO.AddRequestDTO addRequestDTO,
                                           @RequestPart(value = "tourImages", required = false) List<MultipartFile> tourImages,
                                           @RequestPart(value = "thumb", required = false) List<MultipartFile> thumb) {


        toursService.addTours(Integer.parseInt(authentication.getPrincipal().toString()), addRequestDTO, tourImages, thumb);
        return ResponseEntity.ok().build();

    }

    // 투어 수정
    @PostMapping(value = "/update")
    @ApiOperation(value = "투어 수정")
    public ResponseEntity<Object> updateTours(@ApiIgnore Authentication authentication,
                                           @Valid ToursDTO.UpdateRequestDTO updateRequestDTO,
                                           @RequestPart(value = "tourImages", required = false) List<MultipartFile> tourImages,
                                           @RequestPart(value = "thumb", required = false) List<MultipartFile> thumb) {


        toursService.updateTours(Integer.parseInt(authentication.getPrincipal().toString()), updateRequestDTO, tourImages, thumb);
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
