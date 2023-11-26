package com.lattels.smalltour.service.admin;


import com.lattels.smalltour.dto.admin.Traffic.AdminFavoriteGuideCountUpdateDTO;
import com.lattels.smalltour.dto.admin.Traffic.AdminTrafficSearchDTO;
import com.lattels.smalltour.dto.admin.search.AdminSearchDTO;
import com.lattels.smalltour.dto.search.SearchGuideDTO;
import com.lattels.smalltour.model.FavoriteGuide;
import com.lattels.smalltour.model.LogMember;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.FavoriteGuideRepository;
import com.lattels.smalltour.persistence.LogMemberRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminTrafficService {
    private final MemberRepository memberRepository;
    private final LogMemberRepository logMemberRepository;


    /**
     * 관리자 인지 체크
     */
    private void checkAdmin(final int adminId,final int month, final int year){
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자를 찾을수없습니다."));
        if(admin.getRole() != 3){
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }


        if(month > 12 || month < 1){
            throw new IllegalArgumentException("월 의 범위를 벗어났습니다.");
        }

        String yearStr = String.valueOf(year);
        if(!yearStr.matches("[0-9]{4}")) { // 정확히 4자리 숫자로만 구성되어 있는지 검사
            throw new IllegalArgumentException("년도는 4자리 숫자로 구성되어야 합니다.");
        }

    }


    /**
     트래픽(브라우저 + 지역)
     */
    public AdminTrafficSearchDTO trafficBrowserAndRegion(int adminId,Integer month, Integer year){
        AdminTrafficSearchDTO browser = trafficSearchBrowserDTO(adminId,month,year);
        AdminTrafficSearchDTO region = trafficSearchRegionDTO(adminId,month,year);

        return AdminTrafficSearchDTO.builder()
                .count(browser.getCount()) //횟수는 브라우저 갯수나 지역 갯수나 동일해서 둘 중 아무거나 사용해도 상관 없음.
                .regions(region.getRegions())
                .browsers(browser.getBrowsers())
                .build();
    }

    public AdminTrafficSearchDTO trafficSearchRegionDTO(int adminId, Integer month, Integer year){
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자를 찾을수없습니다."));
        if(admin.getRole() != 3){
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }

        Integer countMonth;
        List<Object[]> logMember;
        // 현재 날짜 기준으로 month와 year가 없으면 설정
        if (month == null || month <= 0 || month >= 13 || year == null || year <= 0) {
            logMember = logMemberRepository.findBySearchRegion();
            countMonth = logMemberRepository.findByCountRegion();
        }else{
            logMember = logMemberRepository.findBySearchRegionDay(month,year);
            countMonth = logMemberRepository.findByCountRegionDay(month, year);

        }

        if(logMember.isEmpty()){
            throw new IllegalArgumentException("해당 날짜에 대한 기록이 없습니다.");
        }

        List<AdminTrafficSearchDTO.Region> adminTrafficRegion = new ArrayList<>();
        for(Object[] logMembers : logMember){
            AdminTrafficSearchDTO.Region region = AdminTrafficSearchDTO.Region.builder()
                    .region(String.valueOf(logMembers[0]))
                    .count(Integer.parseInt(String.valueOf(logMembers[1])))
                    .build();
            adminTrafficRegion.add(region);
        }

        return AdminTrafficSearchDTO.builder()
                .count(countMonth)
                .regions(adminTrafficRegion)
                .browsers(new ArrayList<>())
                .build();

    }

    public AdminTrafficSearchDTO trafficSearchBrowserDTO(int adminId,Integer month,Integer year){
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자를 찾을수없습니다."));
        if(admin.getRole() != 3){
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }

        List<Object[]> logMember;
        Integer countMonth;
        // 현재 날짜 기준으로 month와 year가 없으면 설정
        if (month == null || month <= 0 || year == null || year <= 0) {
            logMember = logMemberRepository.findBySearchBrowser();
            countMonth = logMemberRepository.findByCountBrowser();
        } else {
            logMember = logMemberRepository.findBySearchBrowserDay(month, year);
            countMonth = logMemberRepository.findByCountBrowserDay(month, year);
        }



        if(logMember.isEmpty()){
            throw new IllegalArgumentException("해당 날짜에 대한 기록이 없습니다.");
        }

        List<AdminTrafficSearchDTO.Browser> adminTrafficSearchDTO = new ArrayList<>();

        for(Object[] logMembers : logMember){
            AdminTrafficSearchDTO.Browser browser = AdminTrafficSearchDTO.Browser.builder()
                    .browser(String.valueOf(logMembers[0]))
                    .count(Integer.parseInt(String.valueOf(logMembers[1])))
                    .build();
            adminTrafficSearchDTO.add(browser);
        }


        return AdminTrafficSearchDTO.builder()
                .count(countMonth)
                .browsers(adminTrafficSearchDTO)
                .regions(new ArrayList<>())
                .build();
    }


}


