package com.lattels.smalltour.service.admin;

import com.lattels.smalltour.dto.admin.education.EducationDTO;
import com.lattels.smalltour.persistence.EducationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.lattels.smalltour.model.Education;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEducationService {
    private final EducationRepository educationRepository;

    // admin check
    public boolean adminCheck(int adminId){
        try{
            // adminId로 admin인지 확인
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //getEducationList
    public EducationDTO getEducationList(int adminId, String title, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        try{
            // adminId로 admin인지 확인
            if(!adminCheck(adminId)){
                return null;
            }
            // 들어온 기간으로 Education 목록 조회하기
            EducationDTO educationDTO = new EducationDTO();
            // startDate와 endDate null체크하고 아니면 LocalDate 타입으로 변환
            LocalDate startDay = null;
            LocalDate endDay = null;
            List<Education> educationList = null;
            String educationTitle = null;
            int cnt = 0;
            if(startDate != null && endDate != null){
                log.info("날짜가 들어온 상태로 검색을 진행합니다...");
                if(title == null){
                    educationTitle = "";
                }else{
                    educationTitle = title;
                }
                // 이름, 기간으로 조회
                educationList = educationRepository.findByTitleContainingAndStartDayGreaterThanEqualAndEndDayLessThanEqualOrderByEndDayDesc(educationTitle, startDate, endDate, pageable);
                cnt = educationRepository.countByTitleContainingAndStartDayGreaterThanEqualAndEndDayLessThanEqual(educationTitle, startDate, endDate);
            }else{ // 기간이 안들어온 경우 이름으로만 조회
                log.info("날짜가 들어오지 않은 상태로 검색을 진행합니다...");
                if(title == null){
                    educationTitle = "";
                }else{
                    educationTitle = title;
                }
                educationList = educationRepository.findByTitleContainingOrderByEndDayDesc(educationTitle, pageable);
                cnt = educationRepository.countByTitleContaining(educationTitle);
            }
            // EducationDTO로 변환
            EducationDTO.EducationListDTO educationListDTO = null;
            List<EducationDTO.EducationListDTO> educationListDTOs = new ArrayList<>();
            if(educationList != null){
                // 수정된 코드
                if (educationDTO.getEducationList() != null) {
                    educationListDTOs = educationDTO.getEducationList();
                } else {
                    educationListDTOs = new ArrayList<>();
                }
                // 이후 로직
                for(Education education : educationList){
                    educationListDTO = EducationDTO.EducationListDTO.builder()
                            .id(education.getId())
                            .educationTitle(education.getTitle())
                            .startDay(education.getStartDay())
                            .endDay(education.getEndDay())
                            .state(education.getState())
                            .videoPath(education.getVideoPath())
                            .playTime(education.getPlayTime())
                            .build();
                    educationListDTOs.add(educationListDTO);
                }
            }
            educationDTO.setCount(cnt);
            educationDTO.setEducationList(educationListDTOs);
            return educationDTO;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
