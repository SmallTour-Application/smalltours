package com.lattels.smalltour.controller.admin;


import com.lattels.smalltour.dto.admin.tours.AdminDetailImgPackageDTO;
import com.lattels.smalltour.dto.admin.tours.AdminDetailPackageDTO;
import com.lattels.smalltour.dto.admin.tours.AdminPackageDTO;
import com.lattels.smalltour.service.admin.AdminPackageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/package")
public class AdminPackageController {
    private final AdminPackageService adminPackageService;




    @ApiOperation("패키지 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<AdminPackageDTO> getToursList(@ApiIgnore Authentication authentication,
                                                        @RequestParam(value = "tourId", required = false) Integer tourId,
                                                        @RequestParam(required = false)String title,
                                                        int page,
                                                        @RequestParam(required = false)Integer month,
                                                        @RequestParam(required = false)Integer year,
                                                        @RequestParam(required = false)Integer state)
    {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        // 질문 목록 조회
        AdminPackageDTO adminPackageDTO = adminPackageService.getToursList(adminId, tourId,title,page - 1, 10,month,year,state);
        return ResponseEntity.ok(adminPackageDTO);
    }


    @ApiOperation(" 패키지 수정")
    @PostMapping("/update")
    public ResponseEntity<?> updateContent(@ApiIgnore Authentication authentication,
                                           @RequestParam(required = false) int tourId,
                                           @RequestParam(required = false) String title,
                                           @RequestParam(required = false) String subTitle,
                                           @RequestParam(required = false) String description,
                                           @RequestParam(required = false) String meetingPoint,
                                           @RequestParam(required = false) Integer maxGroupSize,
                                           @RequestParam(required = false) Integer minGroupSize) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminPackageService.updateTour(adminId, tourId,title,subTitle,description,meetingPoint,maxGroupSize,minGroupSize);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(" 패키지 승인 수정")
    @PostMapping("/update/approval")
    public ResponseEntity<?> updateApproval(@ApiIgnore Authentication authentication,
                                           @RequestParam(required = false) Integer tourId,
                                           @RequestParam(required = false) Integer approvals) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminPackageService.updateApprovals(adminId, tourId,approvals);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(" 패키지 삭제")
    @PostMapping("/delete")
    public ResponseEntity<?> deleteReview(@ApiIgnore Authentication authentication, int tourId) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminPackageService.deleteTour(adminId, tourId);

        return ResponseEntity.ok().build();
    }


    @ApiOperation("패키지 상세 목록 조회")
    @GetMapping("/detail/list")
    public ResponseEntity<AdminDetailPackageDTO> getToursList(@ApiIgnore Authentication authentication,
                                                              @RequestParam(value = "tourId", required = false) Integer tourId,
                                                              int page)
    {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        // 질문 목록 조회
        AdminDetailPackageDTO adminDetailPackageDTO = adminPackageService.getToursDetailList(adminId, tourId,page - 1, 10);
        return ResponseEntity.ok(adminDetailPackageDTO);
    }

    @ApiOperation(" 패키지 수정")
    @PostMapping("/detail/update")
    public ResponseEntity<?> updateContent(@ApiIgnore Authentication authentication,
                                           @RequestParam(required = false) int tourId,
                                           @RequestParam(required = false) String title,
                                           @RequestParam(required = false) Integer duration,
                                           @RequestParam(required = false) Integer price,
                                           @RequestParam(required = false) Integer maxPeople,
                                           @RequestParam(required = false) Integer minPeople) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminPackageService.updateDetailTour(adminId, tourId,title,duration,price,maxPeople,minPeople);
        return ResponseEntity.ok().build();
    }


    @ApiOperation("패키지 상세 목록 (이미지 포함) 조회")
    @GetMapping("/detail/img")
    public ResponseEntity<AdminDetailImgPackageDTO> getToursImg(@ApiIgnore Authentication authentication,
                                                                @RequestParam(value = "tourId") Integer tourId)
    {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        // 질문 목록 조회
        AdminDetailImgPackageDTO adminDetailImgPackageDTO = adminPackageService.getToursDetailImg(adminId, tourId);
        return ResponseEntity.ok(adminDetailImgPackageDTO);
    }

}
