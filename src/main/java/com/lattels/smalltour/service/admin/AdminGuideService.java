package com.lattels.smalltour.service.admin;

import com.lattels.smalltour.dto.admin.tours.AdminToursDTO;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.PaymentRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminGuideService {

    private final ToursRepository toursRepository;
    private final PaymentRepository paymentRepository;

    // 특정 멤버가 올린 tour 리스트 가져오기
    public AdminToursDTO.ToursDTOList getToursByMemberId(int memberId, Pageable pageable) {
        try{
            Page<Tours> tours = toursRepository.findAllByGuideIdOrderByCreatedDayDesc(memberId, pageable);
            List<AdminToursDTO.ToursDTO> toursDTOs = new ArrayList<>();
            // 전체 검색결과
            long totalCnt = toursRepository.countAllByGuideId(memberId);
            // DTO로 변환
            AdminToursDTO.ToursDTOList toursDTOList = AdminToursDTO.ToursDTOList.builder().totalCnt(totalCnt).build();
            // tours -> toursDTO
            for (Tours tour : tours) {
                // 해당 투어의 총 판매량(갯수) 가져오기
                int sales = paymentRepository.countAllByToursId(tour.getId());

                AdminToursDTO.ToursDTO toursDTO = AdminToursDTO.ToursDTO.builder()
                        .toursId(tour.getId())
                        .toursTitle(tour.getTitle())
                        .createDate(tour.getCreatedDay())
                        .updateDate(tour.getUpdateDay())
                        .state(tour.getApprovals())
                        .sales(sales)
                        .build();
                toursDTOs.add(toursDTO);
            }
            // 합쳐서 리턴
            toursDTOList.setToursList(toursDTOs);
            return toursDTOList;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
