package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.GuideProfileViewDTO;
import com.lattels.smalltour.dto.GuideTourRequestDTO;
import com.lattels.smalltour.dto.GuideTourReviewDTO;
import com.lattels.smalltour.dto.ItemDTO;
import com.lattels.smalltour.dto.main.PopularGuideDTO;
import com.lattels.smalltour.dto.main.PopularTourDTO;
import com.lattels.smalltour.model.*;
import com.lattels.smalltour.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
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

    private final FavoriteGuideRepository favoriteGuideRepository;

    private final UpperPaymentRepository upperPaymentRepository;

    //application.properties
    //server.domain=http://localhost:
    private final Environment env;

    @Value("${file.path.tours.images}")
    private String filePathToursImages;

    @Value("${file.path}")
    private String filePath;


    // unauth/profile/guide 부분
    //가이드 클릭시 해당 정보들 나오는 메서드
    public GuideProfileViewDTO searchGuide(int guideId) {
        // 메소드 내부에서 사용
        String domain = env.getProperty("server.domain");
        String port = env.getProperty("server.port");

        String filePathToursImg = domain + port + "/" + filePathToursImages.replace("\\", "/") + "/";
        String filePathMember = domain + port + "/" + filePath.replace("\\", "/") + "/";


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
                    .thumb(filePathToursImg + tour.getThumb())
                    .title(tour.getTitle())
                    .guideName(tour.getGuide().getName())
                    .guideProfileImg(filePathMember + tour.getGuide().getProfile())
                    .rating(rating)
                    .price(tour.getPrice())
                    .build();

            tourDTOs.add(tourDTO);
        }

        // 가이드의 좋아요 수를 세기
        long favoriteCount = favoriteGuideRepository.countByGuideId(guideId);

        GuideProfileViewDTO guideProfileViewDTO = GuideProfileViewDTO.builder()
                .name(member.getName())
                .tel(member.getTel())
                .introduce(guideProfile.getIntroduce())
                .joinDay(member.getJoinDay())
                .gender(member.getGender())
                .profileImg(filePathMember + member.getProfile())
                .tours(tourDTOs)
                .favoriteCount((int)favoriteCount)
                .build();

        return guideProfileViewDTO;
    }


    //해당 가이드의 투어 리뷰 메서드
    //가이드 검색 메서드랑 다른게 얘는 가이드가 투어한 상품들의 대한 회원들의 평점 및 리뷰가 나오는 메서드
    public GuideTourReviewDTO getGuideTourReview(int guideId, int page){

        // 해당 가이드가 존재하는지 확인
        Member guide = memberRepository.findByGuideId(guideId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가이드는 없습니다."));

        // 해당 가이드의 role이 2인지 확인
        if(guide.getRole() != 2) {
            throw new IllegalArgumentException("해당 사용자는 가이드가 아닙니다.");
        }

        int count = reviewsRepository.countByGuideId(guideId);
        if (count == 0) {
            throw new IllegalArgumentException("해당 가이드에 대한 리뷰가 없습니다.");
        }

        Float avgRating = guideReviewRepository.findAverageRatingByGuideId(guideId);
        //결제 이력이 있는 리뷰만 가져오게
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

    //해당 가이드가 올린 투어 정보 가져오기
    public GuideTourRequestDTO getGuideTours(int guideId, int page){
        // 메소드 내부에서 사용
        String domain = env.getProperty("server.domain");
        String port = env.getProperty("server.port");

        String filePathToursImg = domain + port + "/" + filePathToursImages.replace("\\", "/") + "/";

        // 해당 가이드가 존재하는지 확인
        Member guide = memberRepository.findById(guideId).orElseThrow(()->new IllegalArgumentException("해당 가이드는 없습니다."));

        // 해당 가이드의 role이 2인지 확인
        if (guide.getRole() != 2) {
            throw new IllegalArgumentException("해당 사용자는 가이드가 아닙니다.");
        }

        int size = 10;
        PageRequest pageRequest = PageRequest.of(page -1, size);
        Page<Tours> tours = toursRepository.findByGuideIdAndRoleAndApproval(guideId, pageRequest);

        List<GuideTourRequestDTO.contents> content = new ArrayList<>();


        long count = toursRepository.countByGuideId(guideId);

        GuideTourRequestDTO guideTourRequestDTO = new GuideTourRequestDTO().builder()
                .count((int)count)
                .contents(content)
                .build();

        for (Tours tour : tours) {
            Optional<UpperPayment> optionalUpperPayment = upperPaymentRepository.findByTourIdAndGuideRoleAndItemTypeAndApprovals(tour.getId());

            GuideTourRequestDTO.contents tourContent = GuideTourRequestDTO.contents.builder()
                    .tourId(tour.getId())
                    .thumb(filePathToursImg + tour.getThumb())
                    .title(tour.getTitle())
                    .subTitle(tour.getSubTitle())
                    .build();

            if (optionalUpperPayment.isPresent()) {
                UpperPayment upperPayment = optionalUpperPayment.get();

                ItemDTO.UpperPaymentTourIdResponseDTO upperPaymentTourIdResponseDTO = new ItemDTO.UpperPaymentTourIdResponseDTO(upperPayment);

                tourContent.setUpperPaymentTourIdResponseDTO(upperPaymentTourIdResponseDTO); // 상위결제시 표시
            }

            content.add(tourContent);
        }

        return guideTourRequestDTO;
    }

}