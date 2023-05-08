package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.guidereview.GuideReviewDTO;
import com.lattels.smalltour.dto.guidereview.GuideReviewDeleteDto;
import com.lattels.smalltour.dto.guidereview.GuideReviewUpdateDto;
import com.lattels.smalltour.dto.guidereview.GuideReviewWriteDto;
import com.lattels.smalltour.model.GuideReview;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.GuideReviewRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    /**
     * 해당 가이드에 맞는 가이드 리뷰 목록을 불러옵니다.
     * @param guideId 가이드 ID
     * @param pageable 페이지
     * @return 가이드 리뷰 목록
     */
    public List<GuideReviewDTO> getGuideReviews(int guideId, Pageable pageable) {
        Page<GuideReview> guideReviews = guideReviewRepository.findAllByGuideId(guideId, pageable);

        return guideReviews.stream()
                .map(guideReview -> new GuideReviewDTO(guideReview))
                .collect(Collectors.toList());
    }

    /**
     * 가이드 리뷰를 작성합니다.
     * @param authentication 로그인 정보
     * @param guideReviewWriteDto 가이드 리뷰 작성 DTO
     */
    public void writeGuideReview(Authentication authentication, GuideReviewWriteDto guideReviewWriteDto) {
        int reviewerId = Integer.parseInt(authentication.getPrincipal().toString());

        // 리뷰 작성자 회원 존재 여부 체크
        Member reviewer = memberRepository.findByMemberId(reviewerId);
        Preconditions.checkNotNull(reviewer, "회원을 찾을 수 없습니다. (회원 ID: %d)", reviewerId);

        // 가이드 회원 존재 여부 체크
        int guideId = guideReviewWriteDto.getGuideId();
        Member guide = memberRepository.findByMemberId(guideId);
        Preconditions.checkNotNull(guide, "회원을 찾을 수 없습니다. (회원 ID: %d)", guideId);

        // 이미 작성한 리뷰 존재 여부 체크
        boolean reviewAlreadyExists = guideReviewRepository.existsByReviewerIdAndGuideId(reviewerId, guideId);
        Preconditions.checkState(!reviewAlreadyExists, "이미 해당 회원이 해당 가이드에게 작성한 리뷰가 존재합니다. (회원 ID: %d, 가이드 ID: %d)", reviewerId, guideId);

        // 리뷰 정보 저장
        GuideReview guideReview = GuideReview.builder()
                .reviewer(reviewer)
                .guide(guide)
                .rating(guideReviewWriteDto.getRating())
                .content(guideReviewWriteDto.getContent())
                .createdDay(LocalDateTime.now())
                .build();

        guideReviewRepository.save(guideReview);
    }

    /**
     * 가이드 리뷰를 수정합니다.
     * @param authentication 로그인 정보
     * @param guideReviewUpdateDto 가이드 수정 DTO
     */
    public void updateGuideReview(Authentication authentication, GuideReviewUpdateDto guideReviewUpdateDto) {
        // 리뷰 불러오기
        GuideReview guideReview = guideReviewRepository.findById(guideReviewUpdateDto.getId()).orElse(null);
        Preconditions.checkNotNull(guideReview, "가이드 리뷰를 찾을 수 없습니다. (리뷰 ID: %d)", guideReview.getId());

        // 리뷰 작성자 맞는지 체크
        int reviewerId = Integer.parseInt(authentication.getPrincipal().toString());
        Preconditions.checkArgument(reviewerId == guideReview.getReviewer().getId(), "해당 리뷰의 작성자가 아닙니다. (리뷰 ID: %d, 실제 작성자 ID: %d, 새로운 작성자 ID: %d)", guideReview.getId(), guideReview.getReviewer().getId(), reviewerId);

        // 리뷰 저장
        guideReview.setRating(guideReview.getRating());
        guideReview.setContent(guideReview.getContent());

        guideReviewRepository.save(guideReview);
    }

    /**
     * 가이드 리뷰를 삭제합니다.
     * @param authentication 로그인 정보
     * @param guideReviewDeleteDto 가이드 삭제 DTO
     */
    public void deleteGuideReview(Authentication authentication, GuideReviewDeleteDto guideReviewDeleteDto) {
        // 리뷰 존재 여부 체크
        boolean exists = guideReviewRepository.existsById(guideReviewDeleteDto.getId());
        Preconditions.checkState(exists, "가이드 리뷰를 찾을 수 없습니다. (리뷰 ID: %d)", guideReviewDeleteDto.getId());

        // 리뷰 삭제
        guideReviewRepository.deleteById(guideReviewDeleteDto.getId());
    }

}
