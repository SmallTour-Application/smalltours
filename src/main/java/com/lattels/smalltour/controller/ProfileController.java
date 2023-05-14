package com.lattels.smalltour.controller;


import com.lattels.smalltour.dto.GuideProfileViewDTO;
import com.lattels.smalltour.dto.GuideTourReviewDTO;
import com.lattels.smalltour.service.MainService;
import com.lattels.smalltour.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/unauth/profile/guideId")
@Api(tags = "UnauthProfile", description = "가이드정보 API 컨트롤러")
public class ProfileController {

    private final ProfileService profileService;

    @ApiOperation(value = "가이드 프로필 정보 (비회원접근가능)")
    @GetMapping("/{guideId}")
    public ResponseEntity<GuideProfileViewDTO> getGuideProfile(@PathVariable int guideId) {
        GuideProfileViewDTO guideProfile = profileService.searchGuide(guideId);
        return ResponseEntity.ok(guideProfile);
    }


    @ApiOperation(value = "가이드의 투어 리뷰 가져오기")
    @GetMapping("/{guideId}/reviews/{page}")
    public ResponseEntity<GuideTourReviewDTO> getGuideTourReview(@PathVariable int guideId, @PathVariable int page) {
        GuideTourReviewDTO guideTourReview = profileService.getGuideTourReview(guideId, page);
        return ResponseEntity.ok(guideTourReview);
    }


}