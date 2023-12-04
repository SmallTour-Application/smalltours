package com.lattels.smalltour.service.admin;

import com.lattels.smalltour.dto.admin.review.AdminReviewDTO;
import com.lattels.smalltour.model.GuideReview;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Reviews;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.GuideReviewRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ReviewsRepository;
import com.lattels.smalltour.persistence.ToursRepository;
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
    private final ToursRepository toursRepository;
    private final MemberRepository memberRepository;

    // 특정 회원이 받은 가이드 리뷰 가져오기 getGuideReviewByReceiver
    public AdminReviewDTO.ReviewListDTO getGuideReviewByReceiver(int memberId, Pageable pageable, int state) {
        try{
            List<GuideReview> guideReviews = guideReviewRepository.findByGuideIdAndStateOrderByCreatedDayDesc(memberId,state, pageable);
            List<AdminReviewDTO.ReviewDTO> reviewDTOs = new ArrayList<>();
            // 전체 검색결과
            int totalCnt = guideReviewRepository.countByGuideIdAndState(memberId, state);
            // DTO로 변환
            AdminReviewDTO.ReviewListDTO reviewListDTO = AdminReviewDTO.ReviewListDTO.builder().totalCnt(totalCnt).build();

            for (GuideReview review : guideReviews) {
                // 작성자 이름 가져오기
                String reviewerName = review.getReviewer().getName();
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
                        .state(review.getPayment().getState())
                        .reviewerName(reviewerName)
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

    // 특정 멤버가 만든 여행들에 대한 리뷰 페이징해서 가져오기
    // getToursReview
    public AdminReviewDTO.ReviewListDTO getToursReviewReceiver(int memberId, Pageable pageable, int state) {
        try{
            List<Reviews> reviews = reviewRepository.findAllByGuideIdAndStateOrderByCreatedDayDESC(memberId, state, pageable);
            List<AdminReviewDTO.ReviewDTO> reviewDTOs = new ArrayList<>();
            // 전체 검색결과
            int totalCnt = reviewRepository.countByGuideIdAndState(memberId, state);
            // DTO로 변환
            AdminReviewDTO.ReviewListDTO reviewListDTO = AdminReviewDTO.ReviewListDTO.builder().totalCnt(totalCnt).build();
            for (Reviews review : reviews) {
                // 작성자 이름 가져오기
                String reviewerName = review.getMember().getName();
                AdminReviewDTO.ReviewDTO reviewDTO = AdminReviewDTO.ReviewDTO.builder()
                        .reviewId(review.getId())
                        .memberId(review.getMember().getId())
                        .content(review.getContent())
                        .score(review.getRating())
                        .packageName(review.getPayment().getTours().getTitle())
                        .reviewContent(review.getContent())
                        .reviewDate(review.getCreatedDay())
                        .paymentId(review.getPayment().getId())
                        .packageId(review.getPayment().getTours().getId())
                        .state(review.getPayment().getState())
                        .reviewerName(reviewerName)
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



    // 특정 회원의 리뷰 목록 가져오기 + pageable
    public AdminReviewDTO.ReviewListDTO getReviewsByMemberId(int memberId,int state, Pageable pageable) {
        try{
            List<Reviews> reviews = reviewRepository.findByMemberIdAndStateOrderByCreatedDayDesc(memberId, state, pageable);
            // state와 memberId로 검색한 결과를 reviews에 할당
            List<AdminReviewDTO.ReviewDTO> reviewDTOs = new ArrayList<>();
            // 전체 검색결과
            int totalCnt = reviewRepository.countByMemberIdAndStateOrderByCreatedDayDesc(memberId, state);
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
    public AdminReviewDTO.ReviewListDTO getGuideReview(int memberId, int state,  Pageable pageable) {
        try{
            List<GuideReview> guideReviews = guideReviewRepository.findByReviewerIdAndStateOrderByCreatedDayDesc(memberId,state, pageable);
            List<AdminReviewDTO.ReviewDTO> reviewDTOs = new ArrayList<>();
            // 전체 검색결과
            int totalCnt = guideReviewRepository.countByReviewerIdAndStateOrderByCreatedDayDesc(memberId, state);
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

    //updateMemberRole
    public boolean updateMemberRole(int adminId, int memberId, int role) {
        try{
            // adminId로 admin인지 확인
            if(!adminCheck(adminId)){
                return false;
            }
            // memberId로 member 찾기
            Member member = memberRepository.findById(memberId).orElse(null);
            if(member == null){
                return false;
            }
            // role 변경
            member.setRole(role);
            memberRepository.save(member);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // admin check
    public boolean adminCheck(int adminId){
        try{
            // adminId로 admin인지 확인
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
