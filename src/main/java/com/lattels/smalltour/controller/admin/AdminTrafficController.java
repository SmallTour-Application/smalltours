package com.lattels.smalltour.controller.admin;


import com.lattels.smalltour.dto.admin.Traffic.AdminFavoriteGuideCountUpdateDTO;
import com.lattels.smalltour.dto.admin.Traffic.AdminTrafficSearchDTO;
import com.lattels.smalltour.dto.admin.education.EducationDTO;
import com.lattels.smalltour.dto.admin.education.EducationVideoDTO;
import com.lattels.smalltour.service.admin.AdminTrafficService;
import com.lattels.smalltour.service.admin.AdminVideoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/traffic")
public class AdminTrafficController {
    private final AdminTrafficService adminTrafficService;

    @PostMapping(value = "/search/region/month")
    @ApiOperation(value = "로그인 지역 날짜로 검색")
    public ResponseEntity<Object> searchTrafficRegionMonth(@ApiIgnore Authentication authentication,
                                                     @RequestParam(required = false)int month,
                                                     @RequestParam(required = false)int year) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            AdminTrafficSearchDTO ad = adminTrafficService.trafficSearchRegionDTO(adminId,month,year);
            return ResponseEntity.ok(ad);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/search/browser/month")
    @ApiOperation(value = "로그인한 브라우저 날짜로 검색")
    public ResponseEntity<Object> searchTrafficBrowserMonth(@ApiIgnore Authentication authentication,
                                                     @RequestParam(required = false)int month,
                                                     @RequestParam(required = false)int year) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            AdminTrafficSearchDTO ad = adminTrafficService.trafficSearchBrowserDTO(adminId,month,year);
            return ResponseEntity.ok(ad);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
