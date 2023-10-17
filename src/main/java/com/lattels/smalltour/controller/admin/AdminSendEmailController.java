package com.lattels.smalltour.controller.admin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lattels.smalltour.dto.ResponseDTO;
import com.lattels.smalltour.dto.admin.member.EmailAllSendMessageDTO;
import com.lattels.smalltour.dto.admin.member.EmailMessageDTO;
import com.lattels.smalltour.dto.admin.member.ListMemberDTO;
import com.lattels.smalltour.dto.payment.PaymentMemberListDTO;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.service.EmailMessageService;
import com.lattels.smalltour.service.EmailService;
import com.lattels.smalltour.service.MemberFavoriteStatusService;
import com.lattels.smalltour.service.admin.AdminPaymentService;
import com.lattels.smalltour.service.admin.AdminService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/admin/send")
public class AdminSendEmailController {
    private final MemberRepository memberRepository;
    private final EmailMessageService emailMessageService;

    private static final int NUMBER_OF_PAYMENT_PER_PAGE = 10;

    private final MemberFavoriteStatusService memberFavoriteStatusService;


    // 이메일보내기
    @PostMapping("/email")
    public ResponseEntity<?> memberSendEmail(@ApiIgnore Authentication authentication,@RequestParam int memberId, @RequestBody EmailMessageDTO emailMessageDTO) {

        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            EmailMessageDTO emailMessageDTOs = emailMessageService.sendEmailMessage(adminId,memberId,emailMessageDTO);
            return ResponseEntity.ok().body(emailMessageDTOs);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    // 전체 이메일보내기(회원)
    @PostMapping("/email/member/all")
    public ResponseEntity<?> memberSendAllEmail(@ApiIgnore Authentication authentication,@RequestBody EmailAllSendMessageDTO emailAllSendMessageDTO) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            EmailAllSendMessageDTO emailAllMessageDTOs = emailMessageService.sendAllMemberEmailMessage(adminId,emailAllSendMessageDTO);
            return ResponseEntity.ok().body(emailAllMessageDTOs);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 전체 이메일보내기(가이드)
    @PostMapping("/email/guide/all")
    public ResponseEntity<?> guideSendAllEmail(@ApiIgnore Authentication authentication,@RequestBody EmailAllSendMessageDTO emailAllSendMessageDTO) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            EmailAllSendMessageDTO emailAllMessageDTOs = emailMessageService.sendAllGuideEmailMessage(adminId,emailAllSendMessageDTO);
            return ResponseEntity.ok().body(emailAllMessageDTOs);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


}
