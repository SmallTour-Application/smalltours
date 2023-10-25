package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.stats.TotalCntPerMonthDTO;
import com.lattels.smalltour.dto.stats.StatsDTO;
import com.lattels.smalltour.dto.stats.TotalVolumePercentageDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsService {

    private final MemberRepository memberRepository;

    private final ToursRepository toursRepository;

    private final PaymentRepository paymentRepository;

    private final GuideReviewRepository guideReviewRepository;

    private final ReviewsRepository reviewsRepository;

    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;


    /*
    * 강사 통계 보기
    */
    public StatsDTO.StatsResponseDTO viewStats(int memberId, StatsDTO.DateRequestDTO dateRequestDTO) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 받아온 날짜 할당
        LocalDateTime startDate = dateRequestDTO.getStartDate().atStartOfDay();
        LocalDateTime endDate = dateRequestDTO.getEndDate().atStartOfDay();

        // 시작일과 종료일 중 하나만 선택했다면
        if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
            log.warn("시작일과 종료일 중 하나만 선택하셨습니다. 시작일과 종료일을 모두 선택하시거나 아무 것도 선택하지 마세요.");
            throw new RuntimeException("시작일과 종료일 중 하나만 선택하셨습니다. 시작일과 종료일을 모두 선택하시거나 아무 것도 선택하지 마세요.");
        }

        // 전체 판매 수량
        int totalSalesVolume = 0;
        // 전체 판매액
        int totalSalesRevenue = 0;

        // 가이드의 모든 투어 가져오기
        List<Tours> toursList = toursRepository.findAllByGuideId(memberId);
        List<StatsDTO.StatsToursDTO> toursDTOS = new ArrayList<>();

        for (Tours tours : toursList) {
            // 투어 정보 저장
            StatsDTO.StatsToursDTO toursDTO = new StatsDTO.StatsToursDTO(tours);
            // 총 판매액 저장
            String salesRevenue = paymentRepository.getPriceByDate(tours, startDate, endDate);
            if (salesRevenue == null) salesRevenue = "0";
            toursDTO.setSalesRevenue(Integer.parseInt(salesRevenue));
            // 총 판매횟수 저장
            toursDTO.setSalesVolume((int) paymentRepository.getVolumeByDate(tours, startDate,endDate));
            // 이미지 저장
            toursDTO.setThumb(domain + port + "/img/tours/" + tours.getThumb());
            // dto 리스트에 저장
            toursDTOS.add(toursDTO);

            // 전체 합계에 저장
            totalSalesRevenue += toursDTO.getSalesRevenue();
            totalSalesVolume += toursDTO.getSalesVolume();
        }

        // 판매수량으로 정렬
        Comparator<StatsDTO.StatsToursDTO> comparator = Comparator.comparing(StatsDTO.StatsToursDTO::getSalesVolume, Comparator.reverseOrder());
        List<StatsDTO.StatsToursDTO> comparatorToursDTO = toursDTOS.stream().sorted(comparator).collect(Collectors.toList());

        // 반환 DTO 생성
        StatsDTO.StatsResponseDTO statsResponseDTO = new StatsDTO.StatsResponseDTO();
        statsResponseDTO.setStatsToursDTOList(comparatorToursDTO);
        statsResponseDTO.setSalesRevenue(totalSalesRevenue);
        statsResponseDTO.setSalesVolume(totalSalesVolume);
        // 가이드 리뷰 평점
        String guideReviewRating = guideReviewRepository.getReviewRating(member, startDate, endDate);
        if (guideReviewRating == null) guideReviewRating = "0";
        statsResponseDTO.setGuideReviewRating(Double.parseDouble(guideReviewRating));
        // 투어 리뷰 평점
        String tourReviewRating = reviewsRepository.getReviewRating(member, startDate, endDate);
        if (tourReviewRating == null) tourReviewRating = "0";
        statsResponseDTO.setTourReviewRating(Double.parseDouble(tourReviewRating));

        return statsResponseDTO;
    }

    /*
    * 월별 가입 수 가져오기
    */
    public List<TotalCntPerMonthDTO> getMemberPerMonth(Authentication authentication) {

        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member member = memberRepository.findByMemberId(memberId);
        // 등록된 회원인지 검사
        if (member == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (member.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusYears(1);
        List<TotalCntPerMonthDTO> responseDTOList = memberRepository.countMemberPerMonth(startDate.atStartOfDay());

        int i = -1;
        boolean isMatch = false;
        while (!startDate.isAfter(currentDate)) {
            i++;
            isMatch = false;
            String date = startDate.getYear() + "-" + startDate.getMonthValue();
            for (TotalCntPerMonthDTO dto : responseDTOList) {
                if (date.equals(dto.getMonth())) {
                    isMatch = true;
                    continue;
                }
            }
            if (isMatch == false) {
                responseDTOList.add(i, new TotalCntPerMonthDTO(startDate, 0));
            }
            startDate = startDate.plusMonths(1);
        }

        return responseDTOList;

    }

    /*
    * 월별 예약 수 가져오기
    */
    public List<TotalCntPerMonthDTO> getPaymentPerMonth(Authentication authentication) {

        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member member = memberRepository.findByMemberId(memberId);
        // 등록된 회원인지 검사
        if (member == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (member.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusYears(1);
        List<TotalCntPerMonthDTO> responseDTOList = paymentRepository.countPaymentPerMonth(startDate.atStartOfDay());

        int i = -1;
        boolean isMatch = false;
        while (!startDate.isAfter(currentDate)) {
            i++;
            isMatch = false;
            String date = startDate.getYear() + "-" + startDate.getMonthValue();
            for (TotalCntPerMonthDTO dto : responseDTOList) {
                if (date.equals(dto.getMonth())) {
                    isMatch = true;
                    continue;
                }
            }
            if (isMatch == false) {
                responseDTOList.add(i, new TotalCntPerMonthDTO(startDate, 0));
            }
            startDate = startDate.plusMonths(1);
        }

        return responseDTOList;

    }

    /*
    * 현재 총 회원수 가져오기
    */
    public StatsDTO.TotalMembersDTO getTotalMembers(Authentication authentication) {

        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member member = memberRepository.findByMemberId(memberId);
        // 등록된 회원인지 검사
        if (member == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (member.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }

        StatsDTO.TotalMembersDTO totalMembersDTO = new StatsDTO.TotalMembersDTO();
        // 권한에 맞는 회원 수 받아서 DTO에 저장
        int travelerCnt = memberRepository.countByMemberRole(MemberDTO.MemberRole.TRAVELER);
        totalMembersDTO.setTraveler(travelerCnt);

        int unregisteredGuideCnt = memberRepository.countByMemberRole(MemberDTO.MemberRole.UNREGISTERED_GUIDE);
        totalMembersDTO.setUnregisteredGuide(unregisteredGuideCnt);

        int guideCnt = memberRepository.countByMemberRole(MemberDTO.MemberRole.GUIDE);
        totalMembersDTO.setGuide(guideCnt);

        int totalCnt = travelerCnt + unregisteredGuideCnt + guideCnt;
        totalMembersDTO.setTotal(totalCnt);

        //반환
        return totalMembersDTO;

    }

    /*
     * 기간 동안의 판매 비율 가져오기
     */
    public StatsDTO.TotalVolumePercentageResponseDTO getTotalVolumePercentageList(Authentication authentication, StatsDTO.DateRequestDTO requestDTO) {

        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member member = memberRepository.findByMemberId(memberId);
        // 등록된 회원인지 검사
        if (member == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (member.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }

        LocalDateTime startDate = requestDTO.getStartDate().atStartOfDay();
        LocalDateTime endDate = requestDTO.getEndDate().atStartOfDay();
        List<TotalVolumePercentageDTO> totalVolumePercentageDTOList = paymentRepository.totalVolumePercentage(startDate, endDate);

        long longTotalVolume = paymentRepository.totalCnt(startDate, endDate);
        int totalVolume = (int) longTotalVolume;

        // 퍼센테이지 구하기
        for (TotalVolumePercentageDTO totalVolumePercentageDTO : totalVolumePercentageDTOList) {
//            totalVolume += totalVolumePercentageDTO.getSalesVolume();
            double percentage = ((double) totalVolumePercentageDTO.getSalesVolume() / totalVolume) * 100;
            // 저장
            totalVolumePercentageDTO.setPercentage(Math.round(percentage*100)/100.0);
        }

        StatsDTO.TotalVolumePercentageResponseDTO responseDTO = new StatsDTO.TotalVolumePercentageResponseDTO();
        responseDTO.setTotalSalesVolume(totalVolume);
        responseDTO.setTotalVolumePercentageDTOList(totalVolumePercentageDTOList);

        return responseDTO;

    }
}
