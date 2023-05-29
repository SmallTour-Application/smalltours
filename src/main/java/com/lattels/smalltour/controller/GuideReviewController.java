package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.guidereview.*;
import com.lattels.smalltour.service.GuideReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(description = "가이드 리뷰 API 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/review/guide")
public class GuideReviewController {

    // 페이지당 조회할 리뷰 수
    private static final int NUMBER_OF_REVIEW_PER_PAGE = 10;

    @Autowired
    private GuideReviewService guideReviewService;

    @ApiOperation("가이드 리뷰 수정")
    @PostMapping("/update")
    public ResponseEntity<?> reviewUpdate(@ApiIgnore Authentication authentication, GuideReviewUpdateDTO guideReviewUpdateDTO) {
        guideReviewService.updateGuideReview(authentication, guideReviewUpdateDTO);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("가이드 리뷰 작성")
    @PostMapping("/write")
    public ResponseEntity<?> reviewWrite(@ApiIgnore Authentication authentication, GuideReviewWriteDTO guideReviewWriteDTO) {
        guideReviewService.writeGuideReview(authentication, guideReviewWriteDTO);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("가이드 리뷰 삭제")
    @PostMapping("/delete")
    public ResponseEntity<?> reviewDelete(@ApiIgnore Authentication authentication, GuideReviewDeleteDTO guideReviewDeleteDTO) {
        guideReviewService.deleteGuideReview(authentication, guideReviewDeleteDTO);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("내가 작성한 가이드 리뷰 조회")
    @PostMapping("/review")
    public ResponseEntity<MyGuideReviewListDTO> getMyGuideReview(@ApiIgnore Authentication authentication, int page) {
        MyGuideReviewListDTO guideReviewListDTO = guideReviewService.getMyGuideReviews(authentication, page - 1, NUMBER_OF_REVIEW_PER_PAGE);

        return ResponseEntity.ok(guideReviewListDTO);
    }

}
