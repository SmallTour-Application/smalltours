package com.lattels.smalltour.controller;


import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.ResponseDTO;
import com.lattels.smalltour.dto.favoriteGuideDTO;
import com.lattels.smalltour.dto.favoriteTourDTO;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.security.TokenProvider;
import com.lattels.smalltour.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    private final MemberRepository memberRepository;



    // 로그인한 정보
    @PostMapping("/info")
    public ResponseEntity<?> viewProfile(@ApiIgnore Authentication authentication) {

        try {
            MemberDTO temp = MemberDTO.builder().id(Integer.parseInt(authentication.getPrincipal().toString())).build();
            MemberDTO responseMemberDTO = memberService.viewProfile(temp);
            return ResponseEntity.ok().body(responseMemberDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    //핸드폰번호변경
    @PostMapping("/updatetel")
    public ResponseEntity<?> updatetel(@ApiIgnore Authentication authentication, @RequestBody MemberDTO.UpdateTel updateTel) {

        try {
            String tel = memberService.updateTel(Integer.parseInt(authentication.getPrincipal().toString()), updateTel.getTel());
            if (tel != null || !tel.equals("")) {
                MemberDTO responseMemberDTO = MemberDTO.builder().tel(tel).build();
                return ResponseEntity.ok().body(responseMemberDTO);
            } else {
                ResponseDTO responseDTO = ResponseDTO.builder().error("error").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }



    // 닉네임 변경
    @PostMapping("/updatenickname")
    public ResponseEntity<?> updateNickName(@ApiIgnore Authentication authentication,@RequestBody MemberDTO.UpdateNickName updateNickName) {

        try {
            String nickname = memberService.updateNickName(Integer.parseInt(authentication.getPrincipal().toString()), updateNickName.getNickname());
            if (nickname != null || !nickname.equals("")) {
                MemberDTO responseMemberDTO = MemberDTO.builder().nickname(nickname).build();
                return ResponseEntity.ok().body(responseMemberDTO);
            } else {
                ResponseDTO responseDTO = ResponseDTO.builder().error("error").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }


    // 비밀번호 변경하기 - 기존 비밀번호 체크 후 원하는 비밀번호로 변경하기
    @PostMapping("/updatepw")
    public ResponseEntity<?> chgPw(@ApiIgnore Authentication authentication, @RequestBody MemberDTO.UpdatePw updatePw) {
        try {
            if (memberService.updatePw(Integer.parseInt(authentication.getPrincipal().toString()), updatePw.getCurPw(),updatePw.getChgPw(), passwordEncoder)) {
                ResponseDTO responseDTO = ResponseDTO.builder().error("성공").build();
                return ResponseEntity.ok().body(responseDTO);
            } else {
                ResponseDTO responseDTO = ResponseDTO.builder().error("실패").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }



    // 프로필 이미지 변경
   @PostMapping("/updateProfileImg")
    @ApiOperation(value = "", notes = "")
    public ResponseEntity<?> updateProfileImg(@ApiIgnore Authentication authentication,
                                              @ApiParam(value = "updateProfile", required = true)
                                              @RequestPart(value = "updateProfile", required = false) MemberDTO.UpdateProfile updateProfile,
                                              @RequestPart(value = "profileImgRequest", required = false) MultipartFile[] files) {
        try {
            if (files != null) {
                List<MultipartFile> fileList = Arrays.asList(files);
                updateProfile.setProfileImgRequest(fileList);
            }
            MemberDTO responseMemberDTO = memberService.updateProfileImg(
                    Integer.parseInt(authentication.getPrincipal().toString()),
                    updateProfile);
            return ResponseEntity.ok().body(responseMemberDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    // 프로필 이미지 변경
/*    @PostMapping("/updateProfileImg")
    public ResponseEntity<?> updateProfileImg(@ApiIgnore Authentication authentication, @RequestBody MemberDTO.UpdateProfile memberDTO) {

        try {
            MemberDTO responseMemberDTO = memberService.updateProfileImg(Integer.parseInt(authentication.getPrincipal().toString()),memberDTO);
            return ResponseEntity.ok().body(responseMemberDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }*/

    @ApiOperation(value = "즐겨찾기 가이드")
    @GetMapping("/member/favoriteguide/{page}")
    public ResponseEntity<List<favoriteGuideDTO>> getFavoriteGuides(@ApiIgnore Authentication authentication,
                                                                    @PathVariable int page) {
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        int size = 10;
        return ResponseEntity.ok(memberService.getFavoriteGuides(memberId, page, size));
    }

    @ApiOperation(value = "즐겨찾기 상품")
    @GetMapping("/member/favoriteTour/{page}")
    public ResponseEntity<List<favoriteTourDTO>> getFavoriteTour(@ApiIgnore Authentication authentication,
                                                                   @PathVariable int page) {
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        int size = 10;
        return ResponseEntity.ok(memberService.getFavoriteTours(memberId, page, size));
    }

}