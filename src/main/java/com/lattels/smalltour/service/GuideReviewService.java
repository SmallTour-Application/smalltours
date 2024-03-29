package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.guidereview.*;
import com.lattels.smalltour.model.GuideReview;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Payment;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.GuideReviewRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 가이드 리뷰 서비스
 */
@Service
public class GuideReviewService {

    @Autowired
    private GuideReviewRepository guideReviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * 해당 가이드에 맞는 최근 가이드 리뷰 목록을 불러옵니다.
     * @param guideId 가이드 ID
     * @param pageable 페이지
     * @return 가이드 리뷰 목록
     */
    public GuideReviewListDTO getGuideReviews(int guideId, Pageable pageable) {
        // 가이드 평균 평점 불러오기
        float averageRating = guideReviewRepository.averageOfRatingsByGuideId(guideId);

        // 가이드 리뷰 개수 불러오기
        int reviewCount = Long.valueOf(guideReviewRepository.countAllByGuideId(guideId)).intValue();

        // 페이지에 맞는 가이드 리뷰 불러오기
        Page<GuideReview> guideReviews = guideReviewRepository.findAllByGuideIdOrderByCreatedDayDesc(guideId, pageable);
        List<GuideReviewDTO> guideReviewDTOS = guideReviews.stream()
                .map(guideReview -> new GuideReviewDTO(guideReview))
                .collect(Collectors.toList());

        // DTO 반환
        return GuideReviewListDTO.builder()
                .avgRating(averageRating)
                .count(reviewCount)
                .reviews(guideReviewDTOS)
                .build();
    }

    /**
     * 내가 작성한 가이드 리뷰 목록을 불러옵니다.
     * @return 가이드 리뷰 목록
     */
    public MyGuideReviewListDTO getMyGuideReviews(Authentication authentication, int page, int countPerPage) {
        int reviewerId = Integer.parseInt(authentication.getPrincipal().toString());

        // 가이드 리뷰 개수 불러오기
        int reviewCount = Long.valueOf(guideReviewRepository.countAllByReviewerId(reviewerId)).intValue();

        // 리뷰 작성자 회원 존재 여부 체크
        Member reviewer = memberRepository.findByMemberId(reviewerId);
        Preconditions.checkNotNull(reviewer, "회원을 찾을 수 없습니다. (회원 ID: %s)", reviewerId);

        // 페이지에 맞는 가이드 리뷰 불러오기
        Pageable pageable = PageRequest.of(page, countPerPage);

        Page<GuideReview> guideReviews = guideReviewRepository.findAllByReviewerIdOrderByCreatedDayDesc(reviewerId, pageable);
        List<GuideReviewDTO> guideReviewDTOS = guideReviews.stream()
                .map(guideReview -> new GuideReviewDTO(guideReview))
                .collect(Collectors.toList());

        // DTO 반환
        return MyGuideReviewListDTO.builder()
                .count(reviewCount)
                .reviews(guideReviewDTOS)
                .build();
    }

    /**
     * 가이드 리뷰를 작성합니다.
     * @param authentication 로그인 정보
     * @param guideReviewWriteDTO 가이드 리뷰 작성 DTO
     */
    public void writeGuideReview(Authentication authentication, GuideReviewWriteDTO guideReviewWriteDTO) {
        int reviewerId = Integer.parseInt(authentication.getPrincipal().toString());

        // 리뷰 작성자 회원 존재 여부 체크
        Member reviewer = memberRepository.findByMemberId(reviewerId);
        Preconditions.checkNotNull(reviewer, "회원을 찾을 수 없습니다. (회원 ID: %s)", reviewerId);

        // 결제 존재 여부 체크
        int paymentId = guideReviewWriteDTO.getPaymentId();
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        Preconditions.checkNotNull(payment, "결제를 찾을 수 없습니다. (결제 ID: %s)", paymentId);

        // 이미 작성한 리뷰 존재 여부 체크
        boolean reviewAlreadyExists = guideReviewRepository.existsByReviewerIdAndPaymentId(reviewerId, paymentId);
        Preconditions.checkArgument(!reviewAlreadyExists, "이미 해당 회원이 해당 결제에 대해 작성한 리뷰가 존재합니다. (회원 ID: %s, 결제 ID: %s)", reviewerId, paymentId);

        // 리뷰 정보 저장
        GuideReview guideReview = GuideReview.builder()
                .reviewer(reviewer)
                .payment(payment)
                .guide(payment.getTours().getGuide())
                .rating(guideReviewWriteDTO.getRating())
                .content(guideReviewWriteDTO.getContent())
                .createdDay(LocalDateTime.now())
                .build();

        guideReviewRepository.save(guideReview);
    }

    /**
     * 가이드 리뷰를 수정합니다.
     * 리뷰 작성자 본인만 수정할 수 있습니다.
     * @param authentication 로그인 정보
     * @param guideReviewUpdateDTO 가이드 수정 DTO
     */
    public void updateGuideReview(Authentication authentication, GuideReviewUpdateDTO guideReviewUpdateDTO) {
        // 리뷰 불러오기
        GuideReview guideReview = guideReviewRepository.findById(guideReviewUpdateDTO.getId()).orElse(null);
        Preconditions.checkNotNull(guideReview, "가이드 리뷰를 찾을 수 없습니다. (리뷰 ID: %s)", guideReview.getId());

        // 리뷰 작성자 맞는지 체크
        int reviewerId = Integer.parseInt(authentication.getPrincipal().toString());
        Preconditions.checkArgument(reviewerId == guideReview.getReviewer().getId(), "해당 리뷰의 작성자가 아닙니다. (리뷰 ID: %s, 실제 작성자 ID: %s, 새로운 작성자 ID: %s)", guideReview.getId(), guideReview.getReviewer().getId(), reviewerId);

        // 리뷰 저장
        guideReview.setRating(guideReviewUpdateDTO.getRating());
        guideReview.setContent(guideReviewUpdateDTO.getContent());

        guideReviewRepository.save(guideReview);
    }

    /**
     * 가이드 리뷰를 삭제합니다.
     * 리뷰 작성자 본인과 관리자만 삭제할 수 있습니다.
     * @param authentication 로그인 정보
     * @param guideReviewDeleteDTO 가이드 삭제 DTO
     */
    public void deleteGuideReview(Authentication authentication, GuideReviewDeleteDTO guideReviewDeleteDTO) {
        int reviewId = guideReviewDeleteDTO.getId();

        // 리뷰 존재 여부 체크
        GuideReview guideReview = guideReviewRepository.findById(guideReviewDeleteDTO.getId()).orElse(null);
        Preconditions.checkNotNull(guideReview, "가이드 리뷰를 찾을 수 없습니다. (리뷰 ID: %s)", guideReview.getId());

        // 회원 존재 여부 체크
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 리뷰 작성자거나 관리자인지 체크
        int reviewerId = guideReview.getReviewer().getId();
        Preconditions.checkArgument(memberId == reviewerId || member.getRole() == MemberDTO.MemberRole.ADMIN, "해당 리뷰의 작성자가 아닙니다. (리뷰 ID: %s, 리뷰 작성자 ID: %s, 현재 회원 ID: %s)", reviewId, reviewerId, memberId);

        // 리뷰 삭제
        guideReviewRepository.deleteById(guideReviewDeleteDTO.getId());
    }

}
