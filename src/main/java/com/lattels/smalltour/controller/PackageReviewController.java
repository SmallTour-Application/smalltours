package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.ToursDTO;
import com.lattels.smalltour.dto.packagereview.*;
import com.lattels.smalltour.service.PackageReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "패키지 리뷰 API 컨트롤러")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/review/package")
public class PackageReviewController {

    // 페이지당 조회할 리뷰 수
    private static final int NUMBER_OF_REVIEW_PER_PAGE = 10;

    private final PackageReviewService packageReviewService;

    @ApiOperation("패키지에 현재 등록되어 있는 리뷰 목록 조회")
    @PostMapping("/unauth/list")
    public ResponseEntity<PackageReviewListDTO> getReviewList(PackageReviewListRequestDTO packageReviewListRequestDTO) {
        packageReviewListRequestDTO.setPage(packageReviewListRequestDTO.getPage() - 1);
        PackageReviewListDTO packageReviewListDTO = packageReviewService.getPackageReviewList(packageReviewListRequestDTO, NUMBER_OF_REVIEW_PER_PAGE);

        return ResponseEntity.ok(packageReviewListDTO);
    }

    @ApiOperation("내가 쓴 패키지 리뷰 목록 조회")
    @PostMapping("/review")
    public ResponseEntity<PackageReviewListDTO> getMyPackageReviewList(@ApiIgnore Authentication authentication, int page) {
        PackageReviewListDTO packageReviewListDTO = packageReviewService.getMyPackageReviewList(authentication, page - 1, NUMBER_OF_REVIEW_PER_PAGE);

        return ResponseEntity.ok(packageReviewListDTO);
    }

    @ApiOperation("패키지 리뷰 작성")
    @PostMapping("/write")
    public ResponseEntity<?> reviewWrite(@ApiIgnore Authentication authentication, PackageReviewWriteRequestDTO packageReviewWriteRequestDTO) {
        packageReviewService.writeReview(authentication, packageReviewWriteRequestDTO);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("패키지 리뷰 수정")
    @PostMapping("/update")
    public ResponseEntity<?> reviewUpdate(@ApiIgnore Authentication authentication, PackageReviewUpdateRequestDTO packageReviewUpdateRequestDTO) {
        packageReviewService.updateReview(authentication, packageReviewUpdateRequestDTO);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("패키지 리뷰 삭제")
    @PostMapping("/delete")
    public ResponseEntity<?> reviewDelete(@ApiIgnore Authentication authentication, int reviewId) {
        packageReviewService.deleteReview(authentication, reviewId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("패키지 리뷰 평점")
    @GetMapping("/unauth/rating")
    public ResponseEntity<PackageReviewDTO.RatingResponseDTO> getRating(ToursDTO.IdRequestDTO idRequestDTO) {
        PackageReviewDTO.RatingResponseDTO rating =  packageReviewService.getRating(idRequestDTO);

        return ResponseEntity.ok().body(rating);
    }

}
