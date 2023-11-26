package com.lattels.smalltour.service.admin;

import com.lattels.smalltour.dto.admin.review.AdminReviewDTO;
import com.lattels.smalltour.model.GuideReview;
import com.lattels.smalltour.model.Reviews;
import com.lattels.smalltour.persistence.GuideReviewRepository;
import com.lattels.smalltour.persistence.ReviewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final ReviewsRepository reviewRepository;
    private final GuideReviewRepository guideReviewRepository;


    // 특정 회원의 리뷰 목록 가져오기 + pageable
    public AdminReviewDTO.ReviewListDTO getReviewsByMemberId(int memberId, Pageable pageable) {
        try{
            List<Reviews> reviews = reviewRepository.findByMemberIdOrderByCreatedDayDesc(memberId, pageable);
            List<AdminReviewDTO.ReviewDTO> reviewDTOs = new ArrayList<>();
            // 전체 검색결과
            int totalCnt = reviewRepository.countByMemberId(memberId);
            // DTO로 변환
            AdminReviewDTO.ReviewListDTO reviewListDTO = AdminReviewDTO.ReviewListDTO.builder().totalCnt(totalCnt).build();

            for (Reviews review : reviews) {
                AdminReviewDTO.ReviewDTO reviewDTO = AdminReviewDTO.ReviewDTO.builder()
                        .reviewId(review.getId())
                        .memberId(review.getMember().getId())
                        .guideId(review.getTours().getGuide().getId())
                        .content(review.getContent())
                        .score(review.getRating())
                        .packageName(review.getTours().getTitle())
                        .reviewContent(review.getContent())
                        .reviewDate(review.getCreatedDay())
                        .paymentId(review.getPayment().getId())
                        .guideName(review.getTours().getGuide().getName())
                        .packageId(review.getTours().getId())
                        .build();
                reviewDTOs.add(reviewDTO);
            }
            reviewListDTO.setReviewList(reviewDTOs);
            return reviewListDTO;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    // 특정 멤버가 작성한 가이드 리뷰 가져오기
    public AdminReviewDTO.ReviewListDTO getGuideReview(int memberId, Pageable pageable) {
        try{
            List<GuideReview> guideReviews = guideReviewRepository.findByReviewerIdOrderByCreatedDayDesc(memberId, pageable);
            List<AdminReviewDTO.ReviewDTO> reviewDTOs = new ArrayList<>();
            // 전체 검색결과
            int totalCnt = guideReviewRepository.countByReviewerId(memberId);
            // DTO로 변환
            AdminReviewDTO.ReviewListDTO reviewListDTO = AdminReviewDTO.ReviewListDTO.builder().totalCnt(totalCnt).build();

            for (GuideReview review : guideReviews) {
                AdminReviewDTO.ReviewDTO reviewDTO = AdminReviewDTO.ReviewDTO.builder()
                        .reviewId(review.getId())
                        .memberId(review.getReviewer().getId())
                        .guideId(review.getGuide().getId())
                        .content(review.getContent())
                        .score(review.getRating())
                        .packageName(review.getPayment().getTours().getTitle())
                        .reviewContent(review.getContent())
                        .reviewDate(review.getCreatedDay())
                        .paymentId(review.getPayment().getId())
                        .guideName(review.getGuide().getName())
                        .packageId(review.getPayment().getTours().getId())
                        .build();
                reviewDTOs.add(reviewDTO);
            }
            reviewListDTO.setReviewList(reviewDTOs);
            return reviewListDTO;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
