package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.GuideProfileViewDTO;
import com.lattels.smalltour.dto.main.PopularGuideDTO;
import com.lattels.smalltour.dto.main.PopularTourDTO;
import com.lattels.smalltour.model.*;
import com.lattels.smalltour.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Slf4j
@Service
@RequiredArgsConstructor
//unauth/guide/prifole, packageReview부분, /guide/review는 정환씨가한 GuideReviewService에있음
public class ProfileService {

    private final GuideProfileRepository guideProfileRepository;

    private final ToursRepository toursRepository;

    private final ReviewsRepository reviewsRepository;

    private final MemberRepository memberRepository;


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


}