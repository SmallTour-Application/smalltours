package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.ItemDTO;
import com.lattels.smalltour.dto.main.PopularGuideDTO;
import com.lattels.smalltour.dto.main.PopularTourDTO;
import com.lattels.smalltour.dto.search.SearchGuideDTO;
import com.lattels.smalltour.dto.search.SearchPackageDTO;
import com.lattels.smalltour.dto.search.SearchTourDTO;
import com.lattels.smalltour.model.*;
import com.lattels.smalltour.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
//unauth/search/package , guide 관련 Service
public class SearchService {
    private final MemberRepository memberRepository;
    private final ToursRepository toursRepository;
    private final ReviewsRepository reviewsRepository;
    private final GuideReviewRepository guideReviewRepository;
    private final FavoriteGuideRepository favoriteGuideRepository;
    private final UpperPaymentRepository upperPaymentRepository;
    private final ItemRepository itemRepository;
    private final GuideLockRepository guideLockRepository;

    @Value("${file.path}")
    private String filePath;

    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;

    @Value("${file.path.tours.images}")
    private String filePathToursImages;

    @Value("${file.path.tours}")
    private String tourImg;

    public File getMemberDirectoryPath() {
        File file = new File(filePath);
        file.mkdirs();

        return file;
    }

    public File getTourDirectoryPath() {
        File file = new File(filePathToursImages);
        file.mkdirs();

        return file;
    }


    //type을 패키지로 해놓고 검색창에 패키지 이름으로 검색할 경우
    //해당 패키지들 결과물이 출력되어야함
    public ResponseEntity<SearchPackageDTO> searchPackage(int type, String title, int people, LocalDate start, LocalDate end, int sort, int page) {


     /*   if (people < 3 || people > 5) {
            throw new IllegalArgumentException("해당 상품은 최소 인원 3명에서 최대 5명 까지 돼야 이용이 가능합니다.");
        }*/

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("날짜에 해당하는 상품(패키지)가 없습니다.");
        }


        int size = 10;
        SearchPackageDTO response = SearchPackageDTO.builder()
                .content(new ArrayList<>())
                .build();

        Sort sortDirection = Sort.by("created_day");
        if (sort == 0) {
            sortDirection = sortDirection.descending(); // 내림차순
        } else if (sort == 1) {
            sortDirection = sortDirection.ascending(); // 오름차순
        }

        page = Math.max(page - 1, 0);

        if (type == 0) { // 패키지를 검색하는 경우
            Page<Tours> tours = toursRepository.findToursBySearchParameters(title, people, start, end, PageRequest.of(page, size, sortDirection));
            if (tours.isEmpty()) {
                throw new IllegalArgumentException("해당 기간에 상품이 없습니다.");
            }

            for (Tours tour : tours) {
                Member guide = memberRepository.findById(tour.getGuide().getId()).orElseThrow(() -> new RuntimeException("가이드가 없습니다."));
                List<GuideLock> guideLocks = guideLockRepository.findAllByGuideId((guide.getId()));

                boolean isLocked = false;
                for (GuideLock guideLock : guideLocks) {
                    if ((start.isAfter(guideLock.getStartDay()) && start.isBefore(guideLock.getEndDay()))
                            || (end.isAfter(guideLock.getStartDay()) && end.isBefore(guideLock.getEndDay()))
                            || (start.isBefore(guideLock.getStartDay()) && end.isAfter(guideLock.getEndDay()))) {
                        isLocked = true;
                        break;
                    }
                }

                if (!isLocked && guide.getId() == tour.getGuide().getId() && guide.getRole() == 2) {
                    Float rating = reviewsRepository.findAverageRatingByTourId(tour.getId());
                    if (rating == null) rating = 0f;

                    Optional<UpperPayment> optionalUpperPayment = upperPaymentRepository.findByTourIdAndGuideRoleAndItemTypeAndApprovals(tour.getId());

                    SearchPackageDTO.PackageContent packageContent = SearchPackageDTO.PackageContent.builder()
                            .tourId(tour.getId())
                            .thumb(domain + port + "/img/tours/" + tour.getThumb())
                            .title(tour.getTitle())
                            .guideName(guide.getName())
                            //.guideProfileImg(domain + port + "/img/member/" + guide.getProfile())
                            .rating(rating)
                            .price(tour.getPrice())
                            .build();


                    if (guide.getProfile() != null) {
                        SearchPackageDTO.PackageContent.builder()
                                .guideProfileImg(domain + port + "/img/member/" + guide.getProfile())
                                .build();
                    }

                    if (optionalUpperPayment.isPresent()) {
                        UpperPayment upperPayment = optionalUpperPayment.get();
                        ItemDTO.UpperPaymentTourIdResponseDTO upperPaymentTourIdResponseDTO = new ItemDTO.UpperPaymentTourIdResponseDTO(upperPayment);
                        packageContent.setUpperPaymentTourIdResponseDTO(upperPaymentTourIdResponseDTO); // 상위결제시 표시
                    }

                    response.getContent().add(packageContent);
                }
            }
        }

        if (response.getContent().isEmpty()) {
            throw new IllegalArgumentException("해당 기간에 상품이 없습니다.");
        }

        response.setCount(response.getContent().size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    //가이드로 검색할 경우.
    //SearchPackageDTO에 List<ContentGuide>contentGuide
    public ResponseEntity<SearchGuideDTO> searchGuide(String keyword, int sort, int page) {

        int size = 10;
        SearchGuideDTO response = SearchGuideDTO.builder()
                .contentGuides(new ArrayList<>())
                .build();

        Sort sortDirection = Sort.by("name");
        if (sort == 0) {
            sortDirection = sortDirection.descending(); // 내림차순
        } else if (sort == 1) {
            sortDirection = sortDirection.ascending(); // 오름차순
        }

        //페이지 번호를 1부터 시작하도록 조정
        page = Math.max(page - 1, 0);

        // 가이드를 검색하는 경우
        Page<Member> guides = memberRepository.findGuidesByKeyword(keyword, PageRequest.of(page, size, sortDirection));
        if (guides.isEmpty()) {
            throw new IllegalArgumentException("해당 가이드가 없습니다.");
        }

        for (Member guide : guides) {
            if (guide.getRole() == 2) {
                Float rating = guideReviewRepository.findAverageRatingByGuideId(guide.getId());

                int guideId = guide.getId();
                // 가이드의 좋아요 수를 세기
                long favoriteCount = favoriteGuideRepository.countByGuideId(guideId);
                // 해당 가이드가 올린 상품 갯수
                long tourCount = toursRepository.countByGuideId(guideId);

                if (rating == null) rating = 0f;
                response.getContentGuides().add(
                        SearchGuideDTO.ContentGuide.builder()
                                .guideId(guide.getId())
                                .guideProfileImg(domain + port + "/img/search/member/" + guide.getProfile())
                                .guideName(guide.getName())
                                .rating(rating)
                                .favoriteCount((int) favoriteCount)
                                .uploadTourCount((int) tourCount)
                                .build()
                );


            }
        }
        // 검색 결과 개수 설정
        response.setCount(response.getContentGuides().size());
        // 검색 결과 반환
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    public ResponseEntity<SearchTourDTO> searchTours(String keyword, int sort, String location, int people, LocalDate start,LocalDate end, int page) {


        // 최소3, 최대 5 이외엔 예외처리
        if (people < 3 || people > 5) {
            throw new IllegalArgumentException("해당 상품은 최소 인원 3명에서 최대 5명 까지 돼야 이용이 가능합니다.");
        }

        // 입력한 날짜에 상품이 없을경우
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("날짜에 해당하는 상품(패키지)가 없습니다.");
        }

        // 상품 정렬방식
        Sort sortDirection = Sort.by("created_day");
        if (sort == 0) {
            sortDirection = sortDirection.descending(); // 내림차순
        } else if (sort == 1) {
            sortDirection = sortDirection.ascending(); // 오름차순
        }

        // LocalDateTime->LocalDate
        LocalDateTime startDay = start.atStartOfDay();

        //페이지 번호를 1부터 시작하도록 조정
        page = Math.max(page - 1, 0);

        // 검색 파라미터를 기준으로 상품 찾기
        Page<Tours> tours = toursRepository.findToursBySearchParameters(keyword, location, people, start, end, PageRequest.of(page, 10, sortDirection));

        if (tours.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // 검색결과 DTO 생성
        SearchTourDTO response = SearchTourDTO.builder().content(new ArrayList<>()).build();

        for (Tours tour : tours) {
            Member guide = memberRepository.findById(tour.getGuide().getId()).orElseThrow(() -> new RuntimeException("가이드가 없습니다."));
            List<GuideLock> guideLocks = guideLockRepository.findAllByGuideId((guide.getId()));
            boolean isLocked = false;

            for (GuideLock guideLock : guideLocks) {
                if ((start.isAfter(guideLock.getStartDay()) && start.isBefore(guideLock.getEndDay()))
                        || (end.isAfter(guideLock.getStartDay()) && end.isBefore(guideLock.getEndDay()))
                        || (start.isBefore(guideLock.getStartDay()) && end.isAfter(guideLock.getEndDay()))) {
                    isLocked = true;
                    break;
                }
            }

            if (!isLocked && guide.getId() == tour.getGuide().getId() && guide.getRole() == 2) {
                Float rating = reviewsRepository.findAverageRatingByTourId(tour.getId());
                if (rating == null) rating = 0f;

                SearchTourDTO.TourContentDTO tourContent = SearchTourDTO.TourContentDTO.builder()
                        .tourId(tour.getId())
                        .thumb(domain + port + "/img/search/tour/" + tour.getThumb())
                        .title(tour.getTitle())
                        .subTitle(tour.getSubTitle())
                        .rating(rating)
                        .price(tour.getPrice())
                        .guideName(guide.getName())
                        .guideProfileImg(domain + port + "/img/search/member/" + guide.getProfile())
                        .build();

                Optional<UpperPayment> optionalUpperPayment = upperPaymentRepository.findByTourIdAndGuideRoleAndItemTypeAndApprovals(tour.getId());
                if (optionalUpperPayment.isPresent()) {
                    UpperPayment upperPayment = optionalUpperPayment.get();
                    ItemDTO.UpperPaymentTourIdResponseDTO upperPaymentTourIdResponseDTO = new ItemDTO.UpperPaymentTourIdResponseDTO(upperPayment);
                    tourContent.setUpperPaymentTourIdResponseDTO(upperPaymentTourIdResponseDTO); // 상위결제시 표시
                }

                response.getContent().add(tourContent);
            }
        }

        response.setCount(response.getContent().size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}