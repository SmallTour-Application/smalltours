package com.lattels.smalltour.controller;



import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.security.TokenProvider;
import com.lattels.smalltour.service.KakaoMemberService;
import com.lattels.smalltour.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member/kakao")
public class KaKaoController {

    @Value("${kakao.clientId}")
    private String CLIENT_ID;

    @Value("${kakao.redirectUri}")
    private String REDIRECT_URI;

    private final KakaoMemberService kakaoMemberService;


    private final TokenProvider tokenProvider;
    private final MemberService memberService;


    @GetMapping("/kakao/accesstoken")
    public ResponseEntity<?> getKakaoAccessToken(@RequestParam String code) {
        try {
            System.out.println("code : " + code);
            return ResponseEntity.ok(kakaoMemberService.getAccessToken(code));
        } catch (Exception e) {
            log.error("카카오 에세스 토큰 에러: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카카오 액세스 토큰을 가져오는 중 오류가 발생했습니다.");
        }
    }


    @PostMapping("/verify")
    @ApiOperation("인증번호로 카카오 계정 인증")
    public ResponseEntity<String> verifyNumberWithKakao(@RequestParam int verificationNumber,
                                                        @ApiIgnore Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("일반계정 로그인을 해야 인증을 할 수 있습니다");
        }

        Integer parentId = Integer.parseInt(authentication.getPrincipal().toString());
        try {
            kakaoMemberService.verifyNumber(verificationNumber,parentId);
            return ResponseEntity.ok("인증이 성공적으로 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류가 발생했습니다. 다시 시도해주세요.");
        }
    }


    @PostMapping("/createUser")
    @ApiOperation("카카오 계정과 일반 계정 연동")
    public ResponseEntity<String> createKakaoAccountLink(@RequestParam String code, @ApiIgnore Authentication authentication, HttpServletResponse response) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("일반계정 로그인을 해야 연동을 할 수 있습니다");
        }
        Integer userId = Integer.parseInt(authentication.getPrincipal().toString());
        String status = kakaoMemberService.createKakaoAccountLinks(code,userId);
        switch(status) {
            case "SUCCESS":
                return ResponseEntity.ok("인증번호를 성공적으로 보냈습니다.");
            case "ALREADY_LINKED":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 인증이 완료된 계정입니다.");
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류가 발생했습니다. 다시 시도해주세요.");
        }

    }



    @GetMapping("/auth/kakao/login")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) {
        try {
            MemberDTO memberDTO = kakaoMemberService.kakaoLogin(code,response);
            System.out.println("jwt :" + memberDTO.getToken());
            if (memberDTO.getToken() == null || memberDTO.getToken().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("현재 계정은 연동되지 않았습니다.");
            }
            return ResponseEntity.ok(memberDTO);
        } catch (Exception e) {
            log.error("카카오 로그인 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "카카오 로그인 실패");
        }
    }







}

