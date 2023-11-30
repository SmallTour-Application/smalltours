package com.lattels.smalltour.service.admin;


import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.admin.review.AdminDetailReviewDTO;
import com.lattels.smalltour.dto.admin.review.AdminSpecificReviewsDTO;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Reviews;
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
            reviewPage = reviewsRepository.findAllReviews(pageable,state,memberId);
            reviewsCount = reviewsRepository.countAllReviews(state);
        } else {
            // 하나 이상의 조건이 있을 경우, 조건에 맞는 리뷰와 그 개수를 조회
            reviewPage = reviewsRepository.findReviewsByConditions(title, month, year, name,state,pageable,memberId);
            reviewsCount = reviewsRepository.findReviewsCount(title, month, year, name,state);
        }

        List<AdminSpecificReviewsDTO.ContentReview> contentReviews = new ArrayList<>();
        for (Object[] review : reviewPage) {
            String status = (Integer) review[6] == 1 ? "작성완료" : "삭제됨";

            AdminSpecificReviewsDTO.ContentReview contentReview = AdminSpecificReviewsDTO.ContentReview.builder()
                    .id((Integer) review[0])
                    .tourName((String) review[1])
                    .reviewContent((String) review[2])
                    .createdDay(((Timestamp) review[3]).toLocalDateTime())
                    .status(status)
                    .paymentId((Integer) review[4])
                    .memeberName((String) review[5])
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
    public AdminSpecificReviewsDTO getTourReviews(int adminId, int tourId, int page, int count, Integer state) {
        checkAdmin(adminId);
        Pageable pageable = PageRequest.of(page, count);
        Page<Reviews> reviewsPage;

        if (state != null) {
            reviewsPage = reviewsRepository.findByToursIdAndState(tourId, state, pageable);
        } else {
            reviewsPage = reviewsRepository.findByToursId(tourId, pageable);
        }

        long reviewsCount = reviewsPage.getTotalElements();

        List<AdminSpecificReviewsDTO.ContentReview> contentReviews = reviewsPage.getContent().stream()
                .map(review -> AdminSpecificReviewsDTO.ContentReview.builder()
                        .id(review.getId())
                        .tourName(review.getTours().getTitle()) // 예시: 투어 제목
                        .reviewContent(review.getContent())
                        .createdDay(review.getCreatedDay())
                        .status(review.getState() == 1 ? "작성완료" : "삭제됨")
                        .paymentId(review.getMember().getId())
                        .memeberName(review.getMember().getName())
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

    public AdminDetailReviewDTO getReviewsByBuyer(int adminId, int memberId,String title, int page, int size) {
        checkAdmin(adminId);
        Pageable pageable = PageRequest.of(page, size);

        Page<Object[]> reviewPage = reviewsRepository.findReviewsByBuyerId(memberId, title,pageable);

        List<AdminDetailReviewDTO.detailedReview> detailedReviews = reviewPage.getContent().stream()
                .map(objects -> {
                    return AdminDetailReviewDTO.detailedReview.builder()
                            .id((Integer) objects[0])
                            .tourName((String) objects[1])
                            .guideName((String) objects[2])
                            .buyName((String) objects[3])
                            .rating((Integer) objects[4])
                            .reviewContent((String) objects[5])
                            .build();

                }).collect(Collectors.toList());

        return AdminDetailReviewDTO.builder()
                .count(reviewPage.getTotalElements())
                .deatilReviews(detailedReviews)
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