package com.lattels.smalltour.controller;


import com.lattels.smalltour.dto.GuideSearchDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.ResponseDTO;
import com.lattels.smalltour.security.TokenProvider;
import com.lattels.smalltour.service.EmailTokenService;
import com.lattels.smalltour.service.MainService;
import com.lattels.smalltour.service.UnauthMemberService;
import io.swagger.annotations.Api;
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


    @GetMapping("/search")
    public ResponseEntity<GuideSearchDTO.GuideSearchResult> searchGuideByName(@RequestParam String guideName){
        return ResponseEntity.ok(mainService.searchGuideByName(guideName));
    }

}