package com.lattels.smalltour.controller.admin;


import com.lattels.smalltour.dto.admin.education.EducationDTO;
import com.lattels.smalltour.dto.admin.education.EducationGuideDTO;
import com.lattels.smalltour.dto.admin.education.EducationVideoDTO;
import com.lattels.smalltour.service.admin.AdminGuideVideoService;
import com.lattels.smalltour.service.admin.AdminVideoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/guide")
public class AdminGuideVideoController {
    private final AdminGuideVideoService adminGuideVideoService;

    @ApiOperation("가이드 교육 현황 목록")
    @PostMapping("/education/view/list")
    public ResponseEntity<?> getGuideVideoList(@ApiIgnore Authentication authentication,
                                               int page,
                                               @RequestParam int state,
                                               @RequestParam(required = false) int memberId) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        EducationGuideDTO educationLogGuideListDTO = adminGuideVideoService.getGuideEducationList(adminId, page - 1, 10, state, memberId);
        return ResponseEntity.ok(educationLogGuideListDTO);
    }

    @ApiOperation("교육 강좌 상태 수정")
    @PostMapping("/education/state/update")
    public ResponseEntity<?> getVideoUpdate(@ApiIgnore Authentication authentication,
                                            @RequestParam int educationId,
                                            @RequestParam int state,
                                            @RequestParam int guideId) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminGuideVideoService.updatestatus(adminId, guideId, educationId, state);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("가이드 강좌 목록 삭제")
    @PostMapping("/education/list/delete")
    public ResponseEntity<?> getVideoDelete(@ApiIgnore Authentication authentication,
                                            @RequestParam int educationId,
                                            @RequestParam int guideId) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminGuideVideoService.deleteEducationInfo(adminId,educationId,guideId);
        return ResponseEntity.ok().build();
    }
}
