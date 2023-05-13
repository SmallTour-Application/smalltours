package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.main.PopularGuideDTO;
import com.lattels.smalltour.dto.main.PopularTourDTO;
import com.lattels.smalltour.dto.search.SearchPackageDTO;
import com.lattels.smalltour.model.GuideReview;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.GuideReviewRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ReviewsRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final MemberRepository memberRepository;
    private final ToursRepository toursRepository;
    private final ReviewsRepository reviewsRepository;

    
    //type을 패키지로 해놓고 검색창에 패키지 이름으로 검색할 경우
    //해당 패키지들 결과물이 출력되어야함
    public ResponseEntity<SearchPackageDTO> searchTours(int type, String location, int people, LocalDate start, LocalDate end, int sort, int page) {
        // type이 1인 경우, 즉 guide를 검색하는 경우
        if (type == 1) {
            return searchGuide(location, sort, page);
        }


        // 최소3, 최대 5 이외엔 예외처리
        if (people < 3 || people > 5) {
            throw new IllegalArgumentException("해당 상품은 최소 인원 3명에서 최대 5명 까지 돼야 이용이 가능합니다.");
        }

        // 입력한 날짜에 상품이 없을경우
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("날짜에 해당하는 상품(패키지)가 없습니다.");
        }

        int size = 10;
        SearchPackageDTO response = SearchPackageDTO.builder()
                .content(new ArrayList<>())
                .build();

        Sort sortDirection = Sort.by("createdDay");
        if (sort == 0) {
            sortDirection = sortDirection.descending(); // 내림차순
        } else if (sort == 1) {
            sortDirection = sortDirection.ascending(); // 오름차순
        }

        //페이지 번호를 1부터 시작하도록 조정
        page = Math.max(page - 1, 0);

        if (type == 0) { // 패키지를 검색하는 경우
            Page<Tours> tours = toursRepository.findToursBySearchParameters(location, people, start, end, PageRequest.of(page, size,sortDirection));
            if (tours.isEmpty()) {
                throw new IllegalArgumentException("해당 상품이 없습니다.");
            }

            for (Tours tour : tours) {
                Member guide = memberRepository.findById(tour.getGuide().getId()).orElseThrow(() -> new RuntimeException("Guide not found"));
                if (guide.getId() == tour.getGuide().getId() && guide.getRole() == 1) {
                    Float rating = reviewsRepository.findAverageRatingByTourId(tour.getId());
                    if (rating == null) rating = 0f;
                    response.getContent().add(
                            SearchPackageDTO.Content.builder()
                                    .thumb(tour.getThumb())
                                    .title(tour.getTitle())
                                    .guideName(guide.getName())
                                    .guideProfileImg(guide.getProfile())
                                    .rating(rating)
                                    .price(tour.getPrice())
                                    .build()
                    );
                }
            }
        }
        response.setCount(response.getContent().size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<SearchPackageDTO> searchGuide(String keyword, int sort, int page) {

        int size = 10;
        SearchPackageDTO response = SearchPackageDTO.builder()
                .content(new ArrayList<>())
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
            if (guide.getRole() == 1) {
                Float rating = reviewsRepository.findAverageRatingByGuideId(guide.getId());
                if (rating == null) rating = 0f;
                response.getContent().add(
                        SearchPackageDTO.Content.builder()
                                .guideProfileImg(guide.getProfile())
                                .guideName(guide.getName())
                                .rating(rating)
                                .build()
                );
            }
        }
        // 검색 결과 개수 설정
        response.setCount(response.getContent().size());
        // 검색 결과 반환
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
