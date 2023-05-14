package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.GuideProfileViewDTO;
import com.lattels.smalltour.dto.GuideTourReviewDTO;
import com.lattels.smalltour.dto.main.PopularGuideDTO;
import com.lattels.smalltour.dto.main.PopularTourDTO;
import com.lattels.smalltour.model.*;
import com.lattels.smalltour.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
//unauth/guide/prifole, packageReview부분, /guide/review는 정환씨가한 GuideReviewService에있음
public class ProfileService {

    private final GuideProfileRepository guideProfileRepository;

    private final ToursRepository toursRepository;

    private final ReviewsRepository reviewsRepository;

    private final GuideReviewRepository guideReviewRepository;

    private final MemberRepository memberRepository;


    // unauth/profile/guide 부분
    //가이드 클릭시 해당 정보들 나오는 메서드
    public GuideProfileViewDTO searchGuide(int guideId) {
        // guideId에 해당하는 GuideProfile를 찾기
        GuideProfile guideProfile = guideProfileRepository.findByGuideIdAndRole(guideId);
        if (guideProfile == null) {
            throw new IllegalArgumentException("해당 가이드가 없습니다.: " + guideId);
        }

        // guideId에 해당하는 Member를 찾기
        Member member = memberRepository.findByGuideId(guideId)
                .orElseThrow(() -> new IllegalArgumentException("가이드가 없습니다.: " + guideId));

        // guideId에 해당하는 모든 Tours를 찾기
        List<Tours> tours = toursRepository.findAllByGuideId(guideId);

        // 각 투어에 대한 평균 평점을 계산,
        // 투어의 상세 정보를 포함하는 TourDTO 생성,
        // tourDTOs 리스트에 추가
        List<GuideProfileViewDTO.TourDTO> tourDTOs = new ArrayList<>();
        for(Tours tour : tours) {
            Float averageRating = reviewsRepository.findAverageRatingByTourId(tour.getId());
            float rating = (averageRating != null) ? averageRating : 0.0f;

            GuideProfileViewDTO.TourDTO tourDTO = GuideProfileViewDTO.TourDTO.builder()
                    .thumb(tour.getThumb())
                    .title(tour.getTitle())
                    .guideName(tour.getGuide().getName())
                    .guideProfileImg(tour.getGuide().getProfile())
                    .rating(rating)
                    .price(tour.getPrice())
                    .build();

            tourDTOs.add(tourDTO);
        }



        GuideProfileViewDTO guideProfileViewDTO = GuideProfileViewDTO.builder()
                .name(member.getName())
                .tel(member.getTel())
                .introduce(guideProfile.getIntroduce())
                .joinDay(member.getJoinDay())
                .gender(member.getGender())
                .profileImg(member.getProfile())
                .tours(tourDTOs)
                .favoriteCount(0) //일단은 0으로
                .build();

        return guideProfileViewDTO;
    }


    //해당 가이드의 투어 리뷰 메서드
    //가이드 검색 메서드랑 다른게 얘는 가이드가 투어한 상품들의 대한 회원들의 평점 및 리뷰가 나오는 메서드
    public GuideTourReviewDTO getGuideTourReview(int guideId, int page){

        // 해당 가이드가 존재하는지 확인
        Member guide = memberRepository.findByGuideId(guideId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가이드는 없습니다."));


        Float avgRating = guideReviewRepository.findAverageRatingByGuideId(guideId);
        int count = guideReviewRepository.findGuideReviewsByGuideIdAndRole(guideId).size();
        List<Reviews> reviewsList = reviewsRepository.findAllByGuideId(guideId, PageRequest.of(page - 1, 10));

        List<GuideTourReviewDTO.Review> reviewList = new ArrayList<>();

        for (Reviews reviews : reviewsList) {
            GuideTourReviewDTO.Review review = GuideTourReviewDTO.Review.builder()
                    .packageId(reviews.getTours().getId())
                    .packageName(reviews.getTours().getTitle())
                    .nickname(reviews.getMember().getName())
                    .rating(reviews.getRating())
                    .content(reviews.getContent())
                    .createdDay(reviews.getCreatedDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .build();
            reviewList.add(review);
        }

        return GuideTourReviewDTO.builder()
                .avgRating(avgRating)
                .count(count)
                .reviews(reviewList)
                .build();
    }


}