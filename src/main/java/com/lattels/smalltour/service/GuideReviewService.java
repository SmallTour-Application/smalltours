package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.guidereview.*;
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
     * 해당 가이드의 평균 평점을 불러옵니다.
     * @param guideId 가이드 ID
     */
    public float getGuideAverageRating(int guideId) {
        return guideReviewRepository.averageOfRatingsByGuideId(guideId);
    }

    /**
     * 해당 가이드의 가이드 리뷰 개수를 불러옵니다.
     * @param guideId 가이드 ID
     * @return 가이드 리뷰 개수
     */
    public long getGuideReviewCount(int guideId) {
        return guideReviewRepository.countAllByGuideId(guideId);
    }

    /**
     * 해당 가이드에 맞는 최근 가이드 리뷰 목록을 불러옵니다.
     * @param guideId 가이드 ID
     * @param pageable 페이지
     * @return 가이드 리뷰 목록
     */
    public GuideReviewListDTO getGuideReviews(int guideId, Pageable pageable) {
        // 가이드 평균 평점 불러오기
        float averageRating = getGuideAverageRating(guideId);

        // 가이드 리뷰 개수 불러오기
        int reviewCount = Long.valueOf(getGuideReviewCount(guideId)).intValue();

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
     * 가이드 리뷰를 작성합니다.
     * @param authentication 로그인 정보
     * @param guideReviewWriteDTO 가이드 리뷰 작성 DTO
     */
    public void writeGuideReview(Authentication authentication, GuideReviewWriteDTO guideReviewWriteDTO) {
        int reviewerId = Integer.parseInt(authentication.getPrincipal().toString());

        // 리뷰 작성자 회원 존재 여부 체크
        Member reviewer = memberRepository.findByMemberId(reviewerId);
        Preconditions.checkNotNull(reviewer, "회원을 찾을 수 없습니다. (회원 ID: %s)", reviewerId);

        // 가이드 회원 존재 여부 체크
        int guideId = guideReviewWriteDTO.getGuideId();
        Member guide = memberRepository.findByMemberId(guideId);
        Preconditions.checkNotNull(guide, "회원을 찾을 수 없습니다. (회원 ID: %s)", guideId);

        // 이미 작성한 리뷰 존재 여부 체크
        boolean reviewAlreadyExists = guideReviewRepository.existsByReviewerIdAndGuideId(reviewerId, guideId);
        Preconditions.checkArgument(!reviewAlreadyExists, "이미 해당 회원이 해당 가이드에게 작성한 리뷰가 존재합니다. (회원 ID: %s, 가이드 ID: %s)", reviewerId, guideId);

        // 리뷰 정보 저장
        GuideReview guideReview = GuideReview.builder()
                .reviewer(reviewer)
                .guide(guide)
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
        guideReview.setRating(guideReview.getRating());
        guideReview.setContent(guideReview.getContent());

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
        Preconditions.checkArgument(memberId == reviewerId || member.getState() == MemberDTO.MemberRole.ADMIN, "해당 리뷰의 작성자가 아닙니다. (리뷰 ID: %s, 리뷰 작성자 ID: %s, 현재 회원 ID: %s)", reviewId, reviewerId, memberId);

        // 리뷰 삭제
        guideReviewRepository.deleteById(guideReviewDeleteDTO.getId());
    }

}
