package com.lattels.smalltour.controller;


import com.lattels.smalltour.dto.ResponseDTO;
import com.lattels.smalltour.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/confirm-email")
    public ResponseEntity<?> viewConfirmEmail(@RequestParam String token){
        try {
            boolean result = emailService.verifyEmail(token);
            ResponseDTO responseDTO = ResponseDTO.builder().error("success").build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
