package com.lattels.smalltour.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lattels.smalltour.dto.*;
import com.lattels.smalltour.persistence.GuideProfileRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.security.TokenProvider;
import com.lattels.smalltour.service.MemberGuideService;
import com.lattels.smalltour.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member/guide")
//가이드로 로그인 시 정보 확인 페이지
public class MemberGuideController {
    private final MemberGuideService memberGuideService;

    @GetMapping("/info")
    public ResponseEntity<?> viewProfile(@ApiIgnore Authentication authentication) {
        MemberAndGuideProfileDTO memberAndGuideProfileDTO = MemberAndGuideProfileDTO.builder()
                .id(Integer.parseInt(authentication.getPrincipal().toString()))
                .build();

        MemberAndGuideProfileDTO responseDTO = memberGuideService.viewProfile(memberAndGuideProfileDTO);
        if (responseDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 프로필 정보 없음");
        } else {
            return ResponseEntity.ok(responseDTO);
        }
    }

    /*
    @PostMapping("/updateProfileImg")
    public ResponseEntity<?> updateProfileImg(@ApiIgnore Authentication authentication,
                                              @ModelAttribute @Valid MemberAndGuideProfileDTO.UpdateProfile updateProfile) {
        try {
            if (updateProfile.getProfileImgRequest() != null && !updateProfile.getProfileImgRequest().isEmpty()) {
                MemberAndGuideProfileDTO.UpdateProfile responseMemberDTO = memberGuideService.updateProfileImg(
                        Integer.parseInt(authentication.getPrincipal().toString()),
                        updateProfile);
                return ResponseEntity.ok().body(responseMemberDTO);
            } else {
                throw new Exception("이미지가 없습니다.");
            }
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
*/
    @PostMapping("/updateProfileImg")
    public ResponseEntity<?> updateProfileImg(@ApiIgnore Authentication authentication,
                                              @ApiParam(value = "profile", required = true)
                                              @RequestPart(value = "profile", required = false) String updateProfileString,
                                              @RequestPart(value = "profileImgRequest", required = false) MultipartFile[] files) {
      try{
          ObjectMapper objectmapper = new ObjectMapper();
          if(updateProfileString == null){
              throw new IllegalArgumentException("ProfileString null");
          }
          MemberAndGuideProfileDTO.UpdateProfile updateProfile = objectmapper.readValue(updateProfileString,MemberAndGuideProfileDTO.UpdateProfile.class);
          if (files != null) {
              List<MultipartFile> fileList = Arrays.asList(files);
              updateProfile.setProfileImgRequest(fileList);
          }
          MemberAndGuideProfileDTO.UpdateProfile responseMemberDTO = memberGuideService.updateProfileImg(
                  Integer.parseInt(authentication.getPrincipal().toString()),
                  updateProfile);
          return ResponseEntity.ok().body(responseMemberDTO);
      } catch (Exception e) {
          e.printStackTrace();
          ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
          return ResponseEntity.badRequest().body(responseDTO);
      }
    }



    // 이력서랑 포트폴리오 부분 수정(.pdf)
    @PostMapping("/updateResumePortfolio")
    public ResponseEntity<?> updateResumeAndPortfolio(@ApiIgnore Authentication authentication,
                                                      @ModelAttribute @Valid MemberAndGuideProfileDTO.UpdateResumePortfolio updateResumePortfolio) throws IOException {
        MemberAndGuideProfileDTO.UpdateResumePortfolio responseDTO = memberGuideService.updateResumeAndPortfolio(
                Integer.parseInt(authentication.getPrincipal().toString()),
                updateResumePortfolio);
        return ResponseEntity.ok(responseDTO);
    }


}