package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.LocationsDTO;
import com.lattels.smalltour.service.LocationsService;
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
@RequestMapping("/package/locations")
@Api(tags = "Locations Controller", description = "투어 위치 컨트롤러")
public class LocationsController {

    private final LocationsService locationsService;

    // 투어 위치 등록
    @PostMapping(value = "/add")
    @ApiOperation(value = "투어 위치 등록")
    public ResponseEntity<Object> addLocations(@ApiIgnore Authentication authentication, @Valid @RequestBody LocationsDTO.AddRequestDTO addRequestDTO) {

        locationsService.addLocations(Integer.parseInt(authentication.getPrincipal().toString()), addRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 투어 위치 등록
    @PostMapping(value = "/update")
    @ApiOperation(value = "투어 위치 수정")
    public ResponseEntity<Object> updateLocations(@ApiIgnore Authentication authentication, @Valid @RequestBody LocationsDTO.UpdateRequestDTO updateRequestDTO) {

        locationsService.updateLocations(Integer.parseInt(authentication.getPrincipal().toString()), updateRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 투어 위치 등록
    @PostMapping(value = "/delete")
    @ApiOperation(value = "투어 위치 삭제")
    public ResponseEntity<Object> deleteLocations(@ApiIgnore Authentication authentication, @Valid @RequestBody LocationsDTO.IdRequestDTO idRequestDTO) {

        locationsService.deleteLocations(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().build();

    }

}
