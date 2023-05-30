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

        //GuideLock테이블에 해당 guide에 대한 startDay,endDay를가져옴
        GuideLock guideLock = guideLockRepository.findGuideLockPeriod(guideId, startDay, endDay);

        if (guideLock == null) {
            return guideScheduleDTOList;
        }

        Member guide = memberRepository.findById(guideId).orElse(null);

        if (guide != null && guide.getRole() == 2) {  // 0:회원, 1:미등록가이드, 2:가이드, 3:관리자
            List<Tours> tours = toursRepository.findByGuide(guide);

            for (Tours tour : tours) {
                // 내가 올린 상품이 결제가 된 상품에만 내 스케줄에 보여줄 수 있게 조건.
                boolean isPaymentExists = paymentRepository.existsByTourIdAndState(tour.getId(), 1);
                if (isPaymentExists) {
                    List<Payment> payments = paymentRepository.findPaymentsByGuideAndTourAndDepartureDay(guideId, tour.getId(), startDay, endDay);

                    for (Payment payment : payments) {
                            //date : String, startDay ~ endDay
                            String period = guideLock.getStartDay().toString() + "~" + guideLock.getEndDay().toString();

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
