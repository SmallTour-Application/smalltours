package com.lattels.smalltour.controller;


import com.lattels.smalltour.dto.GuideSearchDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.ResponseDTO;
import com.lattels.smalltour.security.TokenProvider;
import com.lattels.smalltour.service.EmailTokenService;
import com.lattels.smalltour.service.MainService;
import com.lattels.smalltour.service.UnauthMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/unauth/main")
@Api(tags = "UnauthMain", description = "메인화면 API 컨트롤러")
public class MainController {

    private final MainService mainService;


    // 평점이 높은 가이드 3명 검색하기

    @GetMapping("/top-ratings")
    @ApiOperation(value = "평점이 높은 가이드 3명 검색하기")
    public ResponseEntity<?> getTopRatedGuides(){
        return ResponseEntity.ok(mainService.getTopRatedGuides());
    }

}