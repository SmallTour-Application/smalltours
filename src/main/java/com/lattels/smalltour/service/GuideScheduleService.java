package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.GuideProfileViewDTO;
import com.lattels.smalltour.dto.GuideScheduleDTO;
import com.lattels.smalltour.dto.GuideTourReviewDTO;
import com.lattels.smalltour.model.*;
import com.lattels.smalltour.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
//schedule/guide,해당 가이드가 내 상품을 산 사람 정보 가져오는 서비스, memberNickname, memberTel
public class GuideScheduleService {

    private final GuideLockRepository guideLockRepository;
    private final MemberRepository memberRepository;
    private final ToursRepository toursRepository;
    private final PaymentRepository paymentRepository;

    public List<GuideScheduleDTO> getGuideSchedules(int guideId, LocalDate startDay, LocalDate endDay) {
        List<GuideScheduleDTO> guideScheduleDTOList = new ArrayList<>();

        Member guide = memberRepository.findById(guideId).orElse(null);

        if (guide != null && guide.getRole() == 2) {  // 0:회원, 1:미등록가이드, 2:가이드, 3:관리자
            List<Tours> tours = toursRepository.findByGuideAndApprovals(guideId);
 
            for (Tours tour : tours) {
                // 내가 올린 상품이 결제가 된 상품에만 내 스케줄에 보여줄 수 있게 조건.
                boolean isPaymentExists = paymentRepository.existsByTourIdAndState(tour.getId(), 1);
                if (isPaymentExists) {
                        List<Payment> payments = paymentRepository.findPaymentsByGuideAndTourAndDepartureDay(guideId, tour.getId(),startDay, endDay);
                        for (Payment payment : payments) {
                            //payment에 departureDay는 출발일, tours에 duration을 더해서 출발일 + duration = 종료일 구함
                            //DB에 Schedule여행일정에 tour_day 가 뭘의미하는지 이해안가서 일단 사용안함
                            String period = payment.getDepartureDay().toString()+ "~" + payment.getDepartureDay().plusDays(tour.getDuration()).toString();

                            GuideScheduleDTO guideScheduleDTO = GuideScheduleDTO.builder()
                                    .date(period)
                                    .packageId(tour.getId())
                                    .packageName(tour.getTitle())
                                    .memberNickName(payment.getMember().getNickname())
                                    .memberTel(payment.getMember().getTel())
                                    .build();
                            guideScheduleDTOList.add(guideScheduleDTO);
                        }
                    }
                }
            }
        return guideScheduleDTOList;
    }
}