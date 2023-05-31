package com.lattels.smalltour.controller;


import com.lattels.smalltour.dto.GuideProfileViewDTO;
import com.lattels.smalltour.dto.MemberAndGuideProfileDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.ResponseDTO;
import com.lattels.smalltour.security.TokenProvider;
import com.lattels.smalltour.service.EmailTokenService;
import com.lattels.smalltour.service.UnauthGuiderService;
import com.lattels.smalltour.service.UnauthMemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/unauth/guide")
@Api(tags = "UnauthGuide", description = "비회원 가이드 컨트롤러")
public class UnauthGuideController {

    private final EmailTokenService emailTokenService;

    private final TokenProvider tokenProvider;

    private final UnauthGuiderService unauthGuiderService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerGuide(@ModelAttribute MemberAndGuideProfileDTO.SignUp memberGuideDTO) {
        try {
            // 서비스 호출
            MemberAndGuideProfileDTO registeredMemberGuide = unauthGuiderService.addGuide(memberGuideDTO);

            // 이메일 토큰 생성 및 이메일 전송
            emailTokenService.createEmailToken(registeredMemberGuide.getId(), registeredMemberGuide.getEmail());

            // 응답
            return ResponseEntity.ok().body(registeredMemberGuide);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /*
    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody MemberDTO.loginDTO loginDTO) {

        try{

            // 로그인 성공 시에만 MemberEntity 가져옴
            MemberDTO successMemberDTO = unauthGuiderService.getByCredentials(
                    loginDTO.getEmail(),
                    loginDTO.getPassword(),
                    passwordEncoder
            );


            // MemberEntity 가져오기 성공 시
            if (successMemberDTO != null) {

                // TokenProvider 클래스를 이용해 토큰을 생성한 후 MemberDTO에 넣어서 반환
                final String token = tokenProvider.create(successMemberDTO);
                MemberDTO responseMemberDTO = MemberDTO.builder()
                        .email(successMemberDTO.getEmail())
                        .id(successMemberDTO.getId())
                        .token(token)
                        .role(successMemberDTO.getRole())//멤버 타입
                        .nickname(successMemberDTO.getNickname())
                        .build();
                return ResponseEntity.ok().body(responseMemberDTO);

            } else {
                // MemberEntity 가져오기 실패 시 -> 로그인 실패
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .error("로그인 실패").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
        } catch (RuntimeException e){
            //예외발생->이메일 인증 실패,
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 이메일 중복 체크
    @PostMapping("/checkemail")
    public ResponseEntity<?> checkEmail(@RequestBody MemberDTO.CheckEmail checkEmail){
        try{
            if(unauthGuiderService.checkEmail(checkEmail.getEmail())){
                ResponseDTO responseDTO = ResponseDTO.builder().error("ok").build();
                System.out.println(responseDTO + "성공");
                return ResponseEntity.ok().body(responseDTO);
            }else{
                ResponseDTO responseDTO = ResponseDTO.builder().error("이메일 존재함").build();
                System.out.println(responseDTO + "에러");
                return ResponseEntity.badRequest().body(responseDTO);
            }
        }catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            System.out.println(responseDTO + "실패");
            return ResponseEntity.badRequest().body(responseDTO);

        }
    }


    // 인증 이메일 재전송
    @PostMapping("/reconfirm")
    public ResponseEntity<?> resendConfirmEmail(@RequestBody MemberDTO.ViewConfirmEmail viewConfirmEmail){
        try{
            emailTokenService.createEmailToken(viewConfirmEmail.getId(), viewConfirmEmail.getEmail()); // 이메일 전송
            ResponseDTO responseDTO = ResponseDTO.builder().error("ok").build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
*/
}