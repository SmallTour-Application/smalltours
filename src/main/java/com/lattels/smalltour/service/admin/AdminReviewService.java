package com.lattels.smalltour.service.admin;


import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.admin.review.AdminDetailReviewDTO;
import com.lattels.smalltour.dto.admin.review.AdminSpecificReviewsDTO;
import com.lattels.smalltour.model.GuideReview;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Reviews;
import com.lattels.smalltour.persistence.GuideReviewRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.QuestionRepository;
import com.lattels.smalltour.persistence.ReviewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminReviewService {
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final ReviewsRepository reviewsRepository;
    //guideReviewRepository
    private final GuideReviewRepository guideReviewRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${file.path}")
    private String filePath;

    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;

    @Value("${file.path.tours.images}")
    private String filePathToursImages;

    public File getQuestionDirectoryPath() {
        File file = new File(filePath);
        file.mkdirs();

        return file;
    }
    /**
     * 질문의 답변은 정환씨가 했던 QuestionAnswerService를 사용하면 됨
     */


    /**
     * 관리자 인지 체크
     */
    public void checkAdmin(final int adminId){
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자를 찾을수없습니다."));
        if(admin.getRole() != 3){
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }
    }

    /**
     * updateGuideReview(adminId, score, reviewId,newContent); 가이드 리뷰 수정
     * */
    public void updateGuideReview(int adminId, int score, int reviewId,String newContent) {
        checkAdmin(adminId);

        GuideReview review = guideReviewRepository.findById(reviewId).orElse(null);
        Preconditions.checkNotNull(review, "리뷰를 찾을 수 없습니다.", reviewId);

        if (review.getState() == 0) {
            throw new RuntimeException("해당 리뷰는 삭제된 상태입니다.");
        }
        guideReviewRepository.updateGuideReviewContent(reviewId, newContent, score);
    }


    /**
     * 가이드 리뷰 삭제
     * */
    public void deleteGuideReview(int adminId, int reviewId) {
        checkAdmin(adminId);

        GuideReview review = guideReviewRepository.findById(reviewId).orElse(null);
        Preconditions.checkNotNull(review,"리뷰를 찾을 수 없습니다.",reviewId);

        // 기존 이미지 삭제
        // 질문 삭제
        guideReviewRepository.updateGuideReviewState(reviewId);
    }

    /**
     * 작성자 리뷰 게시글 가져오기
     */
    public AdminSpecificReviewsDTO getReviewList(int adminId, int page, int count, String title, Integer month, Integer year, String name, Integer state, Integer memberId){
        checkAdmin(adminId);
        Pageable pageable = PageRequest.of(page,count);

        // month와 year 유효성 검사
        if (month != null && (month < 1 || month > 12)) {
            throw new IllegalArgumentException("월의 범위를 벗어났습니다. (1~12 사이의 값을 입력해주세요)");
        }
        if (year != null && (year < 1900 || year > 2100)) {
            throw new IllegalArgumentException("연도의 범위를 벗어났습니다. (1900~2100 사이의 값을 입력해주세요)");
        }

        Page<Object[]> reviewPage;
        long reviewsCount;
        if (title == null && month == null && year == null && name == null && state == null) {
            // 모든 조건이 null일 경우, 전체 리뷰와 그 개수를 조회
            reviewPage = reviewsRepository.findAllReviews(pageable);
            reviewsCount = reviewPage.getTotalElements();
        } else {
            // 하나 이상의 조건이 있을 경우, 조건에 맞는 리뷰와 그 개수를 조회
            reviewPage = reviewsRepository.findReviewsByConditions(title, month, year, name,state,memberId,pageable);
            reviewsCount = reviewPage.getTotalElements();
        }

        List<AdminSpecificReviewsDTO.ContentReview> contentReviews = new ArrayList<>();
        for (Object[] review : reviewPage) {
            String status = (Integer) review[7] == 1 ? "작성완료" : "삭제됨";

            AdminSpecificReviewsDTO.ContentReview contentReview = AdminSpecificReviewsDTO.ContentReview.builder()
                    .id((Integer) review[0])
                    .tourId((Integer) review[1])
                    .tourName((String) review[2])
                    .reviewContent((String) review[3])
                    .createdDay(((Timestamp) review[4]).toLocalDateTime())
                    .status(status)
                    .paymentId((Integer) review[5])
                    .memeberName((String) review[6])
                    .memberId((Integer) review[8]) //리뷰 작성자
                    .guideId((Integer) review[9]) // 리뷰를 받은 당사자 아이디(가이드)
                    .build();
            contentReviews.add(contentReview);
        }

        return AdminSpecificReviewsDTO.builder()
                .count(reviewsCount)
                .contentReviews(contentReviews)
                .build();
    }


    /**
     * 리뷰  내용 수정 합니다.
     */
    public void updateReview(int adminId, int reviewId,String newContent) {
        checkAdmin(adminId);

        Reviews review = reviewsRepository.findById(reviewId).orElse(null);
        Preconditions.checkNotNull(review,"리뷰를 찾을 수 없습니다.",reviewId);

        if (review.getState() == 0) {
            throw new RuntimeException("해당 리뷰는 삭제된 상태입니다.");
        }

        reviewsRepository.updateReviewContent(reviewId, newContent);

    }



    /**
     * 리뷰 삭제합니다.
     */
    public void deleteReview(int adminId, int reviewId) {
        checkAdmin(adminId);

        Reviews review = reviewsRepository.findById(reviewId).orElse(null);
        Preconditions.checkNotNull(review,"리뷰를 찾을 수 없습니다.",reviewId);

        // 기존 이미지 삭제
        // 질문 삭제
        reviewsRepository.updateReviewState(reviewId);
    }


    /**
     * 특정 투어에 대한 리뷰 목록 가져오기
     */
    public AdminSpecificReviewsDTO getTourReviews(int adminId, Integer tourId, int page, int count, Integer state) {
        checkAdmin(adminId);
        Pageable pageable = PageRequest.of(page, count);
        Page<Object[]> reviewsPage;

        if (state != null || tourId != null) {
            reviewsPage = reviewsRepository.findByToursIdAndState(tourId, state, pageable);
        } else {
            reviewsPage = reviewsRepository.findByToursId(pageable);
        }

        long reviewsCount = reviewsPage.getTotalElements();

        List<AdminSpecificReviewsDTO.ContentReview> contentReviews = reviewsPage.getContent().stream()
                .map(review -> AdminSpecificReviewsDTO.ContentReview.builder()
                        .id((Integer) review[0]) // r.id
                        .tourId((Integer) review[1])
                        .tourName((String) review[2]) // t.title
                        .reviewContent((String) review[3]) // r.content
                        .createdDay(((Timestamp) review[4]).toLocalDateTime()) // r.created_day
                        .status((Integer) review[5] == 1 ? "작성완료" : "삭제됨") // r.state
                        .paymentId((Integer) review[6]) // p.id
                        .memeberName((String) review[7]) // m.name
                        .memberId((Integer) review[8]) // m.id AS memberId
                        .guideId((Integer) review[9]) // g.id AS guideId
                        .build())
                .collect(Collectors.toList());

        return AdminSpecificReviewsDTO.builder()
                .count(reviewsCount)
                .contentReviews(contentReviews)
                .build();
    }

    public void updateTourReview(int adminId, int tourId, int reviewId, String newContent){
        checkAdmin(adminId);

        Reviews review = reviewsRepository.findById(reviewId).orElseThrow(()-> new RuntimeException("해당 투어에 대한 리뷰를 찾을 수 없습니다."));
        if (review.getTours().getId() != tourId) {
            throw new RuntimeException("리뷰가 해당 투어에 속하지 않습니다.");
        }

        if (review.getState() == 0) {
            throw new RuntimeException("해당 리뷰는 삭제된 상태입니다.");
        }
        reviewsRepository.updateTourReviewContent(tourId,reviewId, newContent);
    }


    public void deleteTourReview(int adminId,int tourId, int reviewId) {
        checkAdmin(adminId);

        Reviews review = reviewsRepository.findById(reviewId).orElse(null);
        Preconditions.checkNotNull(review,"리뷰를 찾을 수 없습니다.",reviewId);

        // 기존 이미지 삭제
        // 질문 삭제
        reviewsRepository.updateTourReviewState(tourId,reviewId);
    }


    /**
     리뷰 목록 -> 클릭 -> 해당 멤버가 작성한 리뷰,
     상품의 title로 검색할수있게함
     */

    public AdminDetailReviewDTO getReviewsByBuyer(int adminId, int memberId,int reviewId) {
        checkAdmin(adminId);

        List<Object[]> reviewList = reviewsRepository.findReviewsByBuyerId(memberId,reviewId);

        // 첫 번째 리뷰 가져오기
        Object[] review = reviewList.isEmpty() ? null : reviewList.get(0);

        // 첫 번째 리뷰가 있을 경우에만 DTO 생성
        AdminDetailReviewDTO.detailedReview detailedReview = null;
        if (review != null) {
            detailedReview = AdminDetailReviewDTO.detailedReview.builder()
                    .id((Integer) review[0])
                    .tourId((Integer) review[1])
                    .tourName((String) review[2])
                    .guideId((Integer) review[3])
                    .guideName((String) review[4])
                    .buyId((Integer) review[5])
                    .buyName((String) review[6])
                    .rating((Integer) review[7])
                    .reviewContent((String) review[8])
                    .build();
        }

        return AdminDetailReviewDTO.builder()
                .count(review != null ? 1L : 0L) // 리뷰가 있으면 1, 없으면 0
                .detailReviews(detailedReview)
                .build();
    }




    /**
     * 리뷰  내용 수정 합니다.
     */
    public void updateDetailReview(int adminId, int reviewId,String newContent,int rating) {
        checkAdmin(adminId);

        Reviews review = reviewsRepository.findById(reviewId).orElse(null);
        Preconditions.checkNotNull(review,"리뷰를 찾을 수 없습니다.",reviewId);

        if (review.getState() == 0) {
            throw new RuntimeException("해당 리뷰는 삭제된 상태입니다.");
        }
        reviewsRepository.updateDetailReviewContent(reviewId,newContent,rating);

    }

    /**
     * 리뷰 삭제합니다.
     */
    public void deleteDetailReview(int adminId, int reviewId) {
        checkAdmin(adminId);

        Reviews review = reviewsRepository.findById(reviewId).orElse(null);
        Preconditions.checkNotNull(review,"리뷰를 찾을 수 없습니다.",reviewId);


        reviewsRepository.updateDetailReviewState(reviewId);
    }


}