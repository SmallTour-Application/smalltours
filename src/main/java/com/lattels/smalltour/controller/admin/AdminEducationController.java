package com.lattels.smalltour.controller.admin;

import com.lattels.smalltour.dto.admin.education.EducationDTO;
import com.lattels.smalltour.service.admin.AdminEducationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/education")
public class AdminEducationController {

    private final AdminEducationService adminEducationService;

    /**
     * 기간으로 교육목록 조회
     * */
    @ApiOperation("기간으로 교육목록 조회")
    @GetMapping("/list")
    public ResponseEntity<EducationDTO> getEducationList(@ApiIgnore Authentication authentication,
                                                         @RequestParam(required = false) String title,
                                                         @RequestParam(required = false) LocalDate startDate,
                                                         @RequestParam(required = false) LocalDate endDate,
                                                         Pageable pageable) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        EducationDTO adminEducationDTO = adminEducationService.getEducationList(adminId, title, startDate, endDate, pageable);
        return ResponseEntity.ok(adminEducationDTO);
    }
}
