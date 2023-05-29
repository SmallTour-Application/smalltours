package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.tour.MyPackageListDTO;
import com.lattels.smalltour.dto.tour.MyPackageListRequestDTO;
import com.lattels.smalltour.service.ToursService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "내 패키지 조회 API 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/myPackage")
public class MyPackageController {

    // 페이지당 조회할 패키지 수
    private static final int NUMBER_OF_PACKAGE_PER_PAGE = 10;

    @Autowired
    private ToursService ToursService;

    @ApiOperation("내 패키지 목록 조회")
    @PostMapping("/list")
    public ResponseEntity<MyPackageListDTO> myPackage(@ApiIgnore Authentication authentication, MyPackageListRequestDTO myPackageListRequestDto) {
        MyPackageListDTO myPackageListDto = ToursService.getMyTourList(authentication, myPackageListRequestDto, NUMBER_OF_PACKAGE_PER_PAGE);

        return ResponseEntity.ok(myPackageListDto);
    }

}
