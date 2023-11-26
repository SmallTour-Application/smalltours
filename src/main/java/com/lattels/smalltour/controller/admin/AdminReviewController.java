package com.lattels.smalltour.controller.admin;


import com.lattels.smalltour.dto.admin.review.AdminDetailReviewDTO;
import com.lattels.smalltour.dto.admin.review.AdminReviewDTO;
import com.lattels.smalltour.dto.admin.review.AdminReviewsDTO;
import com.lattels.smalltour.dto.admin.tour.AdminToursDTO;
import com.lattels.smalltour.service.admin.AdminPackageService;
import com.lattels.smalltour.service.admin.AdminReviewService;
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
@RequestMapping("/admin/review")
public class AdminReviewController {
    private final AdminReviewService adminReviewService;

    @ApiOperation("리뷰 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<AdminReviewsDTO> getReviewList(@ApiIgnore Authentication authentication,
                                                        int page,
                                                        @RequestParam(value = "title", required = false) String title,
                                                        @RequestParam(required = false)Integer month,
                                                        @RequestParam(required = false)Integer year,
                                                        @RequestParam(required = false)String name,
                                                        @RequestParam(required = false)Integer state)
    {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        // 질문 목록 조회
        AdminReviewsDTO adminReviewDTO = adminReviewService.getReviewList(adminId, page - 1,10,title,month,year,name,state);
        return ResponseEntity.ok(adminReviewDTO);
    }

    @ApiOperation(" 리뷰 수정")
    @PostMapping("/update")
    public ResponseEntity<?> updateContent(@ApiIgnore Authentication authentication,@RequestParam(required = false) int reviewId,@RequestParam(required = false) String newContent) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminReviewService.updateReview(adminId, reviewId,newContent);
        return ResponseEntity.ok().build();
    }


    @ApiOperation(" 리뷰 삭제")
    @PostMapping("/delete")
    public ResponseEntity<?> deleteReview(@ApiIgnore Authentication authentication, int reviewId) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminReviewService.deleteReview(adminId, reviewId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("특정 여행 리뷰 목록 조회")
    @GetMapping("/tour/list")
    public ResponseEntity<AdminReviewsDTO> getTourReviewsList(@ApiIgnore Authentication authentication,
                                                        @RequestParam(required = false)Integer tourId,
                                                        @RequestParam(required = false)int page,
                                                        @RequestParam(required = false)Integer state)
    {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        // 질문 목록 조회
        AdminReviewsDTO adminReviewDTO = adminReviewService.getTourReviews(adminId, tourId,page -1,10,state);
        return ResponseEntity.ok(adminReviewDTO);
    }

    @ApiOperation(" 특정 투어 리뷰 수정")
    @PostMapping("/tour/update")
    public ResponseEntity<?> updateTourReviewContent(@ApiIgnore Authentication authentication,
                                                     @RequestParam(required = false) int tourId,
                                                     @RequestParam(required = false) int reviewId,
                                                     @RequestParam(required = false) String newContent) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminReviewService.updateTourReview(adminId,tourId, reviewId,newContent);
        return ResponseEntity.ok().build();
    }


    @ApiOperation("특정 투어 리뷰 삭제")
    @PostMapping("/tour/delete")
    public ResponseEntity<?> deleteTourReviewReview(@ApiIgnore Authentication authentication,
                                                    @RequestParam(required = false) int tourId,
                                                    @RequestParam(required = false) int reviewId) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminReviewService.deleteTourReview(adminId,tourId, reviewId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("리뷰 상세 목록 조회")
    @GetMapping("/detail/list")
    public ResponseEntity<AdminDetailReviewDTO> getDetailReviewList(@ApiIgnore Authentication authentication,
                                                                    @RequestParam(value = "memberId", required = false)Integer memberId,
                                                                    @RequestParam(value = "title", required = false)String title,
                                                                    int page)
    {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        // 질문 목록 조회
        AdminDetailReviewDTO adminDetailReviewDTO = adminReviewService.getReviewsByBuyer(adminId,memberId,title,page - 1,10);
        return ResponseEntity.ok(adminDetailReviewDTO);
    }

    @ApiOperation(" 해당 회원 리뷰 및 평점 수정")
    @PostMapping("/detail/update")
    public ResponseEntity<?> updatedetailContent(@ApiIgnore Authentication authentication,
                                                 @RequestParam(required = false) int reviewId,
                                                 @RequestParam(required = false) String newContent,
                                                 @RequestParam(required = false) int rating) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminReviewService.updateDetailReview(adminId, reviewId,newContent,rating);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(" 해당 회원 리뷰 삭제")
    @PostMapping("/detail/delete")
    public ResponseEntity<?> deleteDetailReview(@ApiIgnore Authentication authentication,
                                                @RequestParam(required = false) int reviewId) {
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        adminReviewService.deleteDetailReview(adminId, reviewId);

        return ResponseEntity.ok().build();
    }


}
