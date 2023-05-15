package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.tour.MyPackageDTO;
import com.lattels.smalltour.dto.tour.MyPackageListDTO;
import com.lattels.smalltour.dto.tour.MyPackageListRequestDTO;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.ToursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 투어 정보 (패키지) 서비스
 */
@Service
public class TourService {

    @Autowired
    private ToursRepository toursRepository;

    /**
     * 내 패키지 목록을 불러옵니다.
     * @param authentication 로그인 정보
     * @param myPackageListRequestDto 내 패키지 목록 요청 DTO
     * @param countPerPage 페이지당 패키지 개수
     * @return 내 패키지 목록
     */
    public MyPackageListDTO getMyTourList(Authentication authentication, MyPackageListRequestDTO myPackageListRequestDto, int countPerPage) {
        // 페이지 계산
        Pageable pageable = PageRequest.of(myPackageListRequestDto.getPage() - 1, countPerPage);

        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원의 패키지 개수 가져오기
        int packageCount = Long.valueOf(toursRepository.countAllByGuideId(memberId)).intValue();

        // 회원의 패키지 목록 불러오기
        Page<Tours> tours = null;
        int sort = myPackageListRequestDto.getSort();

        // 정렬 타입 체크
        Preconditions.checkArgument(sort == MyPackageListRequestDTO.Sort.LATEST || sort == MyPackageListRequestDTO.Sort.STATE,
                "잘못된 정렬 형식입니다. (입력한 정렬 ID: %s)", sort);

        // 최신순
        if (sort == MyPackageListRequestDTO.Sort.LATEST) {
            tours = toursRepository.findAllByGuideIdOrderByCreatedDayDesc(memberId, pageable);
        }
        // 등록여부순 (상태순)
        else if (sort == MyPackageListRequestDTO.Sort.STATE) {
            tours = toursRepository.findAllByGuideIdOrderByApprovalsDesc(memberId, pageable);
        }

        // 패키지를 DTO로 변환
        List<MyPackageDTO> myPackageDTOS = tours.stream()
                .map(tour -> new MyPackageDTO(tour))
                .collect(Collectors.toList());

        // DTO 반환
        return MyPackageListDTO.builder()
                .count(packageCount)
                .content(myPackageDTOS)
                .build();
    }

}
