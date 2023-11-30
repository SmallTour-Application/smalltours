package com.lattels.smalltour.controller.admin;


import com.lattels.smalltour.dto.admin.education.EducationDTO;
import com.lattels.smalltour.dto.admin.education.EducationGuideDTO;
import com.lattels.smalltour.dto.admin.education.EducationVideoDTO;
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
@RequestMapping("/admin/upload")
public class AdminVideoController {
    private final AdminVideoService adminVideoService;

    @PostMapping(value = "/education/video")
    @ApiOperation(value = "동영상 업로드 및 진행률")
    public ResponseEntity<Object> uploadVideoWithProgress(@ApiIgnore Authentication authentication,
                                                          @Valid EducationVideoDTO.UploadRequestDTO uploadRequestDTO,
                                                          @RequestPart(value = "videoFile", required = true) List<MultipartFile> videoFile) {
        try {
            int adminId = Integer.parseInt(authentication.getPrincipal().toString());
            adminVideoService.uploadVideoWithProgress(adminId, uploadRequestDTO, videoFile);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("교육 강좌 목록")
    @PostMapping("/education/view/list")
    public ResponseEntity<?> getVideoList(@ApiIgnore Authentication authentication, int page,@RequestParam int state,@RequestParam(required = false) Integer educationId) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        EducationDTO educationLogVideoDTO = adminVideoService.getEducationList(adminId, page - 1, 10,state,educationId);
        return ResponseEntity.ok(educationLogVideoDTO);
    }

    @ApiOperation("교육 강좌 목록 상태 변경")
    @PostMapping("/education/view/state/update")
    public ResponseEntity<?> getVideoUpdate(@ApiIgnore Authentication authentication,
                                            @RequestParam(required = false) int educationId,
                                            @RequestParam(required = false) int state) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminVideoService.updatestatus(adminId,educationId,state);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("교육 강좌 목록 삭제")
    @PostMapping("/education/view/list/delete")
    public ResponseEntity<?> getVideoDelete(@ApiIgnore Authentication authentication,
                                            @RequestParam(required = false) int educationId) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminVideoService.deleteEducationInfo(adminId,educationId);
        return ResponseEntity.ok().build();
    }

    // 동영상 수정
    @PostMapping(value = "/education/view/video/update")
    @ApiOperation(value = "동영상 수정")
    public ResponseEntity<Object> updateVideo(@ApiIgnore Authentication authentication,
                                              @Valid EducationVideoDTO.UpdateRequestDTO updateRequestDTO,
                                              @RequestPart(value = "videoFile", required = false) List<MultipartFile> videoFile) {
        try {
            adminVideoService.updateVideo(Integer.parseInt(authentication.getPrincipal().toString()),
                    updateRequestDTO,
                    videoFile);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
