package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.GuideProfileViewDTO;
import com.lattels.smalltour.dto.GuideTourRequestDTO;
import com.lattels.smalltour.dto.GuideTourReviewDTO;
import com.lattels.smalltour.dto.guidereview.GuideReviewListDTO;
import com.lattels.smalltour.dto.guidereview.GuideReviewListRequestDTO;
import com.lattels.smalltour.service.GuideReviewService;
import com.lattels.smalltour.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "비로그인 프로필 API 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/unauth/profile")
public class UnauthProfileController {

    // 페이지당 조회할 리뷰 수
    private static final int NUMBER_OF_REVIEW_PER_PAGE = 10;

    @Autowired
    private GuideReviewService guideReviewService;

    @Autowired
    private ProfileService profileService;

    @ApiOperation("가이드 리뷰 조회")
    @GetMapping("/guide/review")
    public ResponseEntity<GuideReviewListDTO> getGuideReview(GuideReviewListRequestDTO guideReviewListRequestDTO) {
        Pageable pageable = PageRequest.of(guideReviewListRequestDTO.getPage() - 1, NUMBER_OF_REVIEW_PER_PAGE);
        GuideReviewListDTO guideReviewListDTO = guideReviewService.getGuideReviews(guideReviewListRequestDTO.getGuideId(), pageable);

        return ResponseEntity.ok(guideReviewListDTO);
    }

    @ApiOperation(value = "가이드 프로필 정보 (비회원접근가능)")
    @GetMapping("/guide")
    public ResponseEntity<GuideProfileViewDTO> getGuideProfile(@RequestParam("guideId") int guideId) {
        GuideProfileViewDTO guideProfile = profileService.searchGuide(guideId);
        return ResponseEntity.ok(guideProfile);
    }

    @ApiOperation(value = "가이드의 투어 리뷰 가져오기")
    @GetMapping("/guide/reviews")
    public ResponseEntity<GuideTourReviewDTO> getGuideTourReview(@RequestParam("guideId") int guideId,
                                                                 @RequestParam(value = "page", defaultValue = "1") int page) {
        GuideTourReviewDTO guideTourReview = profileService.getGuideTourReview(guideId, page);
        return ResponseEntity.ok(guideTourReview);
    }

    @GetMapping("/guide/tours")
    public ResponseEntity<GuideTourRequestDTO> getGuideTours(@RequestParam("guideId") int guideId,
                                                             @RequestParam(value = "page", defaultValue = "1") int page) {
        GuideTourRequestDTO guideTours = profileService.getGuideTours(guideId, page);
        return ResponseEntity.ok(guideTours);
    }

}
