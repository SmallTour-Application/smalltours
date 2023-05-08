package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.guidereview.*;
import com.lattels.smalltour.dto.ResponseDTO;
import com.lattels.smalltour.service.GuideReviewService;
import com.lattels.smalltour.service.MemberService;
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
    private MemberService memberService;

    @Autowired
    private GuideReviewService guideReviewService;

    @ApiOperation("가이드에 맞는 리뷰 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<?> getGuideReviewList(GuideReviewListDto guideReviewListDto) {
        try {
            Pageable pageable = PageRequest.of(guideReviewListDto.getPage(), NUMBER_OF_REVIEW_PER_PAGE);
            List<GuideReviewDTO> guideReviewDtoList = guideReviewService.getGuideReviews(guideReviewListDto.getGuideId(), pageable);

            return ResponseEntity.ok(guideReviewDtoList);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().body(ResponseDTO.builder().error(e.getMessage()).build());
        }
    }

    @ApiOperation("가이드 리뷰 수정")
    @PostMapping("/update")
    public ResponseEntity<?> reviewUpdate(@ApiIgnore Authentication authentication, GuideReviewUpdateDto guideReviewUpdateDto) {
        try {
            guideReviewService.updateGuideReview(authentication, guideReviewUpdateDto);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().body(ResponseDTO.builder().error(e.getMessage()).build());
        }
    }

    @ApiOperation("가이드 리뷰 작성")
    @PostMapping("/write")
    public ResponseEntity<?> reviewWrite(@ApiIgnore Authentication authentication, GuideReviewWriteDto guideReviewWriteDto) {
        try {
            guideReviewService.writeGuideReview(authentication, guideReviewWriteDto);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().body(ResponseDTO.builder().error(e.getMessage()).build());
        }
    }

    @ApiOperation("가이드 리뷰 삭제")
    @PostMapping("/delete")
    public ResponseEntity<?> reviewDelete(@ApiIgnore Authentication authentication, GuideReviewDeleteDto guideReviewDeleteDto) {
        try {
            guideReviewService.deleteGuideReview(authentication, guideReviewDeleteDto);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().body(ResponseDTO.builder().error(e.getMessage()).build());
        }
    }

}
