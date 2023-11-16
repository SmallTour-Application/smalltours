package com.lattels.smalltour.controller.admin;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.ResponseDTO;
import com.lattels.smalltour.dto.ToursDTO;
import com.lattels.smalltour.dto.admin.Traffic.AdminFavoriteGuideCountUpdateDTO;
import com.lattels.smalltour.dto.admin.member.AdminAddMemberDTO;
import com.lattels.smalltour.dto.admin.member.ListMemberDTO;
import com.lattels.smalltour.dto.admin.payment.AdminPaymentListDTO;
import com.lattels.smalltour.dto.admin.review.AdminReviewDTO;
import com.lattels.smalltour.dto.admin.search.AdminSearchDTO;
import com.lattels.smalltour.dto.admin.tours.AdminToursDTO;
import com.lattels.smalltour.dto.payment.PaymentMemberInfoDTO;
import com.lattels.smalltour.dto.payment.PaymentMemberListDTO;
import com.lattels.smalltour.dto.search.SearchGuideDTO;
import com.lattels.smalltour.service.EmailTokenService;
import com.lattels.smalltour.service.admin.AdminGuideService;
import com.lattels.smalltour.service.admin.AdminMemberService;
import com.lattels.smalltour.service.admin.AdminPaymentService;
import com.lattels.smalltour.service.admin.AdminService;
import com.lattels.smalltour.service.MemberFavoriteStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/member")
public class AdminMemberController {
    private final AdminService adminService;

    private final PasswordEncoder passwordEncoder;

    private final AdminPaymentService adminPaymentService;

    private final EmailTokenService emailTokenService;

    private final AdminMemberService adminMemberService;

    private final AdminGuideService adminGuideService;


    private static final int NUMBER_OF_PAYMENT_PER_PAGE = 10;

    private final MemberFavoriteStatusService memberFavoriteStatusService;


    // 회원상세목록
    @PostMapping("/info")
    public ResponseEntity<?> memberInfoProfiles(@ApiIgnore Authentication authentication, @RequestParam int memberId) {

        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            ListMemberDTO responseMemberDTO = adminService.getInfoMembers(adminId, memberId);
            return ResponseEntity.ok().body(responseMemberDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    // 회원 전체 목록
    @GetMapping("/list")
    public ResponseEntity<?> memberList(@ApiIgnore Authentication authentication) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            List<ListMemberDTO> responseMemberDTO = adminService.getListMembers(adminId);
            return ResponseEntity.ok().body(responseMemberDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    //회원 전화번호 수정
    @PostMapping("/update/tel")
    public ResponseEntity<?> memberUpdateTel(@ApiIgnore Authentication authentication, @RequestParam int memberId, @RequestBody ListMemberDTO.UpdateTel updateTel) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            String updateMemberTel = adminService.updateTel(adminId, memberId, updateTel.getTel());
            return ResponseEntity.ok().body("성공적으로 수정되었습니다." + updateMemberTel);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //회원 닉네임 수정
    @PostMapping("/update/nickname")
    public ResponseEntity<?> memberUpdateNickName(@ApiIgnore Authentication authentication, @RequestParam int memberId, @RequestBody ListMemberDTO.UpdateNickName updateNickName) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            String updateMemberNickName = adminService.updateNickName(adminId, memberId, updateNickName.getNickname());
            return ResponseEntity.ok().body("성공적으로 수정되었습니다." + updateMemberNickName);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 비밀번호 변경하기 - 기존 비번 필요없음 관리자라
    @PostMapping("/update/password")
    public ResponseEntity<?> chgPw(@ApiIgnore Authentication authentication, @RequestParam int memberId, @RequestBody String chgPw) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            String updatePassword = adminService.updatePw(adminId, memberId, chgPw, passwordEncoder);
            return ResponseEntity.ok().body("성공적으로 수정되었습니다." + updatePassword);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 회원 이미지 변경하기 
    @PostMapping("/update/profile")
    public ResponseEntity<?> memberUpdateProfile(@ApiIgnore Authentication authentication,
                                           @RequestParam int memberId,
                                           @ApiParam(value = "updateProfile", required = true)
                                           @RequestPart(value = "updateProfile", required = false) String updateProfileString,
                                           @RequestPart(value = "profileImgRequest", required = false) MultipartFile[] files) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            ObjectMapper objectMapper = new ObjectMapper();

            if (updateProfileString == null) {
                return ResponseEntity.badRequest().body("이미지를 넣어주세요");
            }

            ListMemberDTO.UpdateProfile updateProfile = objectMapper.readValue(updateProfileString, ListMemberDTO.UpdateProfile.class);

            if (files != null) {
                List<MultipartFile> fileList = Arrays.asList(files);
                updateProfile.setProfileImgRequest(fileList);
            }

            ListMemberDTO responseMemberDTO = adminService.updateProfileImg(adminId, memberId, updateProfile);
            return ResponseEntity.ok().body("성공적으로 수정되었습니다." + responseMemberDTO);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*//블랙리스트 설정
    @PostMapping("/update/blackList")
    public ResponseEntity<?> memberUpdateBlackList(@ApiIgnore Authentication authentication, @RequestParam int memberId, @RequestBody int blackList) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            int updateSettingList = adminService.updateBlackList(adminId, memberId, blackList);
            return ResponseEntity.ok().body("성공적으로 수정되었습니다." + updateSettingList);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //우수가이드 설정(role=2 인계정만)
    @PostMapping("/update/greatGuide")
    public ResponseEntity<?> memberUpdateGreatGuide(@ApiIgnore Authentication authentication, @RequestParam int memberId, @RequestBody int greateGuide) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            int updateSettingGuide = adminService.updateGreateGuide(adminId, memberId, greateGuide);
            return ResponseEntity.ok().body("성공적으로 수정되었습니다." + updateSettingGuide);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }*/



    @ApiOperation("회원 결제 목록")
    @PostMapping("/paymentList")
    public ResponseEntity<?> getPaymentList(@ApiIgnore Authentication authentication,int page) {
        List<PaymentMemberListDTO> paymentListDTO = adminPaymentService.getPaymentMemberList(authentication, page - 1, NUMBER_OF_PAYMENT_PER_PAGE);

        return ResponseEntity.ok(paymentListDTO);
    }

    @ApiOperation("회원 결제 취소 목록")
    @PostMapping("/payment/cancel")
    public ResponseEntity<?> getCancelPayment(@ApiIgnore Authentication authentication,int page) {
        List<PaymentMemberListDTO> paymentListDTO = adminPaymentService.getPaymentCancelMemberList(authentication, page - 1, NUMBER_OF_PAYMENT_PER_PAGE);

        return ResponseEntity.ok(paymentListDTO);
    }

    @ApiOperation("회원 결제 환불 목록")
    @PostMapping("/payment/refund")
    public ResponseEntity<?> getRefundPayment(@ApiIgnore Authentication authentication,int page) {
        List<PaymentMemberListDTO> paymentListDTO = adminPaymentService.getPaymentrefundMemberList(authentication, page - 1, NUMBER_OF_PAYMENT_PER_PAGE);

        return ResponseEntity.ok(paymentListDTO);
    }

    @ApiOperation("회원 삭제")
    @PostMapping("/delete/member")
    public ResponseEntity<?> getDeleteMember(@ApiIgnore Authentication authentication,@RequestParam int memberId) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminService.deleteMember(adminId, memberId);
        return ResponseEntity.ok("삭제완료");
    }

    @PostMapping("/search/member")
    public ResponseEntity<AdminSearchDTO> searchMembers(
            @ApiIgnore Authentication authentication,
            @RequestParam(required = false) String memberName,
            @RequestParam(required = false) String memberEmail,
            @RequestParam(required = false) String memberTel,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDay,
            @RequestParam(value = "size", defaultValue = "0") int size,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        return adminService.adminSearch(adminId,memberName,memberEmail,memberTel,birthDay, page,size);
    }


    @PostMapping("/add/member")
    public ResponseEntity<?> registerMember(@ApiIgnore Authentication authentication, AdminAddMemberDTO.AddMember addMemberDTO) {

        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            AdminAddMemberDTO registeredMember = adminService.adminAddMember(adminId,addMemberDTO);
            AdminAddMemberDTO responseMemberDTO = AdminAddMemberDTO.builder()
                    .email(registeredMember.getEmail())
                    .nickName(registeredMember.getNickName())
                    .build();
            return ResponseEntity.ok().body(responseMemberDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }




    @GetMapping(value = "/search/favorite/guide")
    @ApiOperation(value = "가이드 좋아요 수 변동")
    public ResponseEntity<Object> searchFavoriteGuideChange(@ApiIgnore Authentication authentication,
                                                            @RequestParam(value = "month", required = false) Integer month,
                                                            @RequestParam(value = "year", required = false) Integer year,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                                            @RequestParam(value = "sort", defaultValue = "1") int sort,
                                                            @RequestParam(value = "name", required = false) String name) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());

            // 현재 날짜 기준으로 month와 year가 없으면 설정
            if (month == null || month <= 0) {
                month = LocalDate.now().getMonthValue();
            }
            if (year == null || year <= 0) {
                year = LocalDate.now().getYear();
            }
            if(name == null){
                name = "";
            }
            if(sort <=0){
                throw new IllegalArgumentException("제대로 된 값을 입력하세요. 1:이름 오림차순 2: 내름차순");
            }

            // 서비스 메소드 호출
            AdminFavoriteGuideCountUpdateDTO ad = adminService.countFavoriteGuideUpdate(adminId, month, year, page, size,sort,name);
            return ResponseEntity.ok(ad);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    // 특정 멤버의 결제정보 가져오기
    @GetMapping("/payment/member")
    public ResponseEntity<?> getPaymentMember(@ApiIgnore Authentication authentication, @RequestParam int memberId, Pageable pageable, @RequestParam int state) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            AdminPaymentListDTO.MemberPaymentListDTO responseMemberDTO = adminPaymentService.getPaymentMemberInfo(authentication, memberId, pageable, state);
            return ResponseEntity.ok().body(responseMemberDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 특정 멤버의 리뷰 리스트 가져오기
    @GetMapping("/review/member")
    public ResponseEntity<?> getReviewMember(@ApiIgnore Authentication authentication, @RequestParam int memberId, Pageable pageable) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            AdminReviewDTO.ReviewListDTO responseMemberDTO = adminMemberService.getReviewsByMemberId(memberId, pageable);
            return ResponseEntity.ok().body(responseMemberDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 특정 멤버의 가이드 리뷰 리스트 가져오기
    @GetMapping("/review/guide")
    public ResponseEntity<?> getReviewGuide(@ApiIgnore Authentication authentication, @RequestParam int memberId, Pageable pageable) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            AdminReviewDTO.ReviewListDTO responseMemberDTO = adminMemberService.getGuideReview(memberId, pageable);
            return ResponseEntity.ok().body(responseMemberDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 특정 멤버가 올린 tour 리스트 가져오기
    @GetMapping("/tours/member")
    public ResponseEntity<?> getToursByMemberId(@ApiIgnore Authentication authentication, @RequestParam int memberId, Pageable pageable) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            AdminToursDTO.ToursDTOList responseMemberDTO = adminGuideService.getToursByMemberId(memberId, pageable);
            return ResponseEntity.ok().body(responseMemberDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

}
