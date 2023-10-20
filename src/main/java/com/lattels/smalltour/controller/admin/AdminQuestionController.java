package com.lattels.smalltour.controller.admin;


import com.lattels.smalltour.dto.admin.question.AdminQuestionListDTO;
import com.lattels.smalltour.service.admin.AdminPaymentService;
import com.lattels.smalltour.service.admin.AdminQuestionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/Question")
public class AdminQuestionController {
    private final AdminQuestionService adminQuestionService;

    private final PasswordEncoder passwordEncoder;

    private final AdminPaymentService adminPaymentService;


    @ApiOperation("질문 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<AdminQuestionListDTO> getQuestionList(@ApiIgnore Authentication authentication,int isAnswer,int page) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        // 질문 목록 조회
        AdminQuestionListDTO questionListDTO = adminQuestionService.getQuestionList(adminId, isAnswer,page - 1, 10);

        return ResponseEntity.ok(questionListDTO);
    }


    @ApiOperation(" 질문 삭제")
    @PostMapping("/delete")
    public ResponseEntity<?> deleteQuestion(@ApiIgnore Authentication authentication, int questionId) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminQuestionService.deleteQuestion(adminId, questionId);

        return ResponseEntity.ok().build();
    }
}
