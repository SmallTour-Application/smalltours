package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.main.PopularGuideDTO;
import com.lattels.smalltour.dto.main.PopularTourDTO;
import com.lattels.smalltour.model.GuideReview;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.GuideReviewRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ReviewsRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {

    //PopularTOP3Guide
    private final MemberRepository memberRepository;
    private final GuideReviewRepository guideReviewRepository;

    //PopluarTOP3Tour
    private final ToursRepository toursRepository;
    private final ReviewsRepository reviewsRepository;

    //가이드인 사람들 role이 1임, 메인화면에서 TOP3 가이드를 나타내기 위한 부분,GuideReview에 Rating기준으로 높은 점수순으로.
    public PopularGuideDTO getTopRatedGuides(){
        //member테이블에 role이 1인 사람 = 가이드
        List<Member> guides = memberRepository.findByRole(1);

        //평점에 따라 가이드를 정렬하는 우선순위 큐 생성
        //일반적인 큐의 구조 FIFO(First In First Out)를 가지면서, 데이터가 들어온 순서대로 데이터가 나가는 것이 아닌 우선순위를 먼저 결정하고
        // 그 우선순위가 높은 데이터가 먼저 나가는 자료구조
        PriorityQueue<PopularGuideDTO.ReviewInfo> topGuides = new PriorityQueue<>(
                Comparator.comparing(PopularGuideDTO.ReviewInfo::getRating)
        );

        for (Member guide : guides) {
            //guideReview테이블에서 해당 가이드의 리뷰를 모두 가져옴
            List<GuideReview> reviews = guideReviewRepository.findGuideReviewsByGuideIdAndRole(guide.getId());
            float sum = 0;
            //해당 가이드 모든 리뷰에 대해 평점 더함
            for (GuideReview review : reviews) {
                sum += review.getRating();
            }
            // 평점의 평균을 계산합니다. 리뷰가 없는 경우 평균은 0입니다.
            float average = (reviews.size() > 0) ? sum / reviews.size() : 0;

            PopularGuideDTO.ReviewInfo reviewInfo = new PopularGuideDTO.ReviewInfo();
            reviewInfo.setProfileImg(guide.getProfile());
            reviewInfo.setGuideName(guide.getName());
            reviewInfo.setRating(average);

            //가이드의 리뷰가 3개미만일 경우 추가
            //또는 가이드 리뷰 점수가 낮은데 그보다 높은점수의 가이드 리뷰가 나타날경우 추가
            if (topGuides.size() < 3) {
                topGuides.add(reviewInfo);
            } else if (average > topGuides.peek().getRating()) {
                topGuides.poll();
                topGuides.add(reviewInfo);
            }
        }

        // 우선순위 큐의 내용을 리스트로 변환 이렇게 하면 리스트의 내용을 정렬할 수 있다.
        List<PopularGuideDTO.ReviewInfo> topThreeGuides = new ArrayList<>(topGuides);
        
        //리스트 내용을 평점이 높은 순서로 정렬
        //GuideSearchDTO.ReviewInfo::getRating == GuideSearchDTO.ReviewInfo reviewInfo -> reviewInfo.getRating() 람다표현식
        topThreeGuides.sort(Comparator.comparing(PopularGuideDTO.ReviewInfo::getRating).reversed());//내림차순으로 결과가 나와서 .reversed 사용해서 오름차순으로 변경(높은점수부터 출력)
        PopularGuideDTO popularGuideResult = new PopularGuideDTO();
        popularGuideResult.setCount(topThreeGuides.size());
        popularGuideResult.setContent(topThreeGuides);

        return popularGuideResult;
    }

    //평점이 높은 순서대로 Top3 투어상품
    public PopularTourDTO getPopularTours(){
        List<Tours> allTours = toursRepository.findAll();
        PriorityQueue<PopularTourDTO.TourInfo> topTours = new PriorityQueue<>(
                Comparator.comparing(PopularTourDTO.TourInfo::getRating)
        );

        for (Tours tour : allTours) {
            Float averageRating = reviewsRepository.findAverageRatingByTourId(tour.getId());
            if (averageRating == null) continue; //평점이 없는경우, 해당 상품에 대해 리뷰가 없다고 판단 ,그냥넘어감

            PopularTourDTO.TourInfo tourInfo = PopularTourDTO.TourInfo.builder()
                    .thumb(tour.getThumb())
                    .title(tour.getTitle())
                    .subTitle(tour.getSubTitle())
                    .price(tour.getPrice())
                    .minPeople(tour.getMinGroupSize())
                    .maxPeople(tour.getMaxGroupSize())
                    .rating(averageRating)
                    .build();

            if (topTours.size() < 3) {
                topTours.add(tourInfo);
            } else if (averageRating > topTours.peek().getRating()) {
                topTours.poll();
                topTours.add(tourInfo);
            }
        }

        List<PopularTourDTO.TourInfo> topThreeTours = new ArrayList<>(topTours);
        topThreeTours.sort(Comparator.comparing(PopularTourDTO.TourInfo::getRating).reversed());
        PopularTourDTO popularTourResult = new PopularTourDTO();
        popularTourResult.setCount(topThreeTours.size());
        popularTourResult.setContent(topThreeTours);

        return popularTourResult;
    }


}