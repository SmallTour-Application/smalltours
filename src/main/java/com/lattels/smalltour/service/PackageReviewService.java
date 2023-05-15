package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.packagereview.*;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Reviews;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ReviewsRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PackageReviewService {

    private final ReviewsRepository reviewsRepository;
    private final ToursRepository toursRepository;
    private final MemberRepository memberRepository;

    /**
     * 해당 패키지의 리뷰 목록을 불러옵니다.
     * @param packageReviewListRequestDTO 패키지 목록 요청 DTO
     * @param countPerPage 페이지당 리뷰 개수
     * @return 패키지 리뷰 목록
     */
    public PackageReviewListDTO getPackageReviewList(PackageReviewListRequestDTO packageReviewListRequestDTO, int countPerPage) {
        int page = packageReviewListRequestDTO.getPage();
        int packageId = packageReviewListRequestDTO.getPackageId();

        // 페이지
        Pageable pageable = PageRequest.of(page, countPerPage);

        // 리뷰 개수
        int reviewCount = Long.valueOf(reviewsRepository.countAllByToursId(packageId)).intValue();

        // 패키지 리뷰 불러오기
        List<Reviews> reviews = reviewsRepository.findByToursIdOrderByCreatedDayDesc(packageId, pageable);
        List<PackageReviewDTO> reviewDTOS = reviews.stream()
                .map(review -> new PackageReviewDTO(review))
                .collect(Collectors.toList());

        // DTO 반환
        return PackageReviewListDTO.builder()
                .count(reviewCount)
                .review(reviewDTOS)
                .build();
    }

    /**
     * 내가 작성한 패키지 리뷰 목록을 불러옵니다.
     * @param authentication 로그인 정보
     * @param page 페이지
     * @param countPerPage 페이지당 리뷰 개수
     * @return 패키지 리뷰 목록
     */
    public PackageReviewListDTO getMyPackageReviewList(Authentication authentication, int page, int countPerPage) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 페이지
        Pageable pageable = PageRequest.of(page, countPerPage);

        // 리뷰 개수
        int reviewCount = Long.valueOf(reviewsRepository.countAllByMemberId(memberId)).intValue();

        // 회원이 작성한 리뷰 불러오기
        List<Reviews> reviews = reviewsRepository.findByMemberIdOrderByCreatedDayDesc(memberId, pageable);
        List<PackageReviewDTO> reviewDTOS = reviews.stream()
                .map(review -> new PackageReviewDTO(review))
                .collect(Collectors.toList());

        // DTO 반환
        return PackageReviewListDTO.builder()
                .count(reviewCount)
                .review(reviewDTOS)
                .build();
    }

    /**
     * 패키지 리뷰를 작성합니다.
     * 생성된 리뷰 ID를 반환합니다.
     * @param authentication 로그인 정보
     * @param packageReviewWriteRequestDTO 패키지 리뷰 작성 요청 DTO
     * @return 생성된 리뷰 ID
     */
    public int writeReview(Authentication authentication, PackageReviewWriteRequestDTO packageReviewWriteRequestDTO) {
        int packageId = packageReviewWriteRequestDTO.getPackageId();

        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 리뷰 작성자 회원 존재 여부 체크
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 패키지 존재 여부 체크
        Tours tours = toursRepository.findById(packageId).orElse(null);
        Preconditions.checkNotNull(tours, "패키지를 찾을 수 없습니다. (패키지 ID: %s)", packageId);

        // 이미 패키지에 리뷰를 작성했는지 체크
        boolean reviewAlreadyExists = reviewsRepository.existsByToursIdAndMemberId(packageId, memberId);
        Preconditions.checkArgument(!reviewAlreadyExists, "이미 해당 회원이 해당 패키지에 작성한 리뷰가 존재합니다. (패키지 ID: %s, 회원 ID: %s)", packageId, memberId);

        // 리뷰 저장
        Reviews reviews = Reviews.builder()
                .member(member)
                .tours(tours)
                .rating(packageReviewWriteRequestDTO.getRating())
                .content(packageReviewWriteRequestDTO.getContent())
                .createdDay(LocalDateTime.now())
                .build();

        reviewsRepository.save(reviews);

        // 생성된 리뷰 ID 반환
        return reviews.getId();
    }

    /**
     * 패키지 리뷰를 수정합니다.
     * @param authentication 로그인 정보
     * @param packageReviewUpdateRequestDTO 패키지 리뷰 수정 요청 DTO
     */
    public void updateReview(Authentication authentication, PackageReviewUpdateRequestDTO packageReviewUpdateRequestDTO) {
        int reviewId = packageReviewUpdateRequestDTO.getReviewId();

        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 리뷰 작성자 회원 존재 여부 체크
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 리뷰 존재 여부 체크
        Reviews reviews = reviewsRepository.findById(reviewId).orElse(null);
        Preconditions.checkNotNull(reviews, "리뷰를 찾을 수 없습니다. (리뷰 ID: %s)", reviewId);

        // 리뷰 수정 내용 저장
        reviews.setContent(packageReviewUpdateRequestDTO.getContent());
        reviews.setRating(packageReviewUpdateRequestDTO.getRating());

        reviewsRepository.save(reviews);
    }

    /**
     * 패키지 리뷰를 삭제합니다.
     * 리뷰 작성자 본인과 관리자만 삭제할 수 있습니다.
     * @param authentication 로그인 정보
     * @param reviewId 리뷰 ID
     */
    public void deleteReview(Authentication authentication, int reviewId) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 리뷰 작성자 회원 존재 여부 체크
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 리뷰 존재 여부 체크
        Reviews reviews = reviewsRepository.findById(reviewId).orElse(null);
        Preconditions.checkNotNull(reviews, "리뷰를 찾을 수 없습니다. (리뷰 ID: %s)", reviewId);

        // 리뷰 작성자거나 관리자인지 체크
        int reviewerId = reviews.getMember().getId();
        Preconditions.checkArgument(memberId == reviewerId || member.getRole() == MemberDTO.MemberRole.ADMIN, "해당 리뷰의 작성자가 아닙니다. (리뷰 ID: %s, 리뷰 작성자 ID: %s, 현재 회원 ID: %s)", reviewId, reviewerId, memberId);

        // 리뷰 삭제
        reviewsRepository.deleteById(reviewId);
    }

}
