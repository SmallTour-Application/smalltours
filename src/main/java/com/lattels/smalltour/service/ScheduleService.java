package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.ScheduleDTO;
import com.lattels.smalltour.dto.ScheduleItemDTO;
import com.lattels.smalltour.dto.ToursDTO;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Schedule;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ScheduleRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final MemberRepository memberRepository;

    private final ToursRepository toursRepository;

    private final ScheduleItemService scheduleItemService;

    // 여행 일정 등록
    public void addSchedule(int memberId, ScheduleDTO.AddRequestDTO addRequestDTO) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 패키지 등록인이 해당 member인지 검사
        Tours tours = toursRepository.findByIdAndGuideId(addRequestDTO.getTourId(), memberId);
        Preconditions.checkNotNull(tours, "해당 유저와 패키지가 등록인이 다릅니다. (회원 ID : %s, 패키지 ID : %s)", memberId, addRequestDTO.getTourId());

        // 시간이 겹치는 일정이 있는지 검사
        Schedule scheduleTimeChk = scheduleRepository.checkTime(addRequestDTO.getTourId(), addRequestDTO.getTourDay(), addRequestDTO.getStartTime(), addRequestDTO.getEndTime());

        boolean chk = false;
        if (scheduleTimeChk == null) chk = true;
        Preconditions.checkArgument(chk, "시간이 겹치는 일정이 있습니다.");

        Schedule schedule = Schedule.builder()
                .tours(tours)
                .tourDay(addRequestDTO.getTourDay())
                .startTime(addRequestDTO.getStartTime())
                .endTime(addRequestDTO.getEndTime())
                .build();

        scheduleRepository.save(schedule);

    }

    // 여행 일정 수정
    public void updateSchedule(int memberId, ScheduleDTO.UpdateRequestDTO updateRequestDTO) {

        // 일정 정보 가져오기
        Schedule schedule = scheduleRepository.findById(updateRequestDTO.getId());
        Preconditions.checkNotNull(schedule, "등록된 스케줄 없습니다. (스케줄 ID : %s)", updateRequestDTO.getId());

        // 일정 등록인이 맞는지 검사
        Preconditions.checkArgument(memberId == schedule.getTours().getGuide().getId(), "해당 유저와 등록인이 일치하지 않습니다. (수정 요청 회원 ID : %s, 기존 등록 회원 ID : %s)", memberId, schedule.getTours().getGuide().getId());

        // 시간이 겹치는 일정이 있는지 검사
        Schedule scheduleTimeChk = scheduleRepository.checkTime(schedule.getTours().getId(), updateRequestDTO.getTourDay(), updateRequestDTO.getStartTime(), updateRequestDTO.getEndTime());
        log.info("체크"+scheduleTimeChk);
        boolean chk = false;
        if (scheduleTimeChk == null) chk = true;
        Preconditions.checkArgument(chk, "시간이 겹치는 일정이 있습니다.");

        schedule.setTourDay(updateRequestDTO.getTourDay());
        schedule.setStartTime(updateRequestDTO.getStartTime());
        schedule.setEndTime(updateRequestDTO.getEndTime());

        scheduleRepository.save(schedule);

    }

    // 여행 일정 삭제
    public void deleteSchedule(int memberId, ScheduleDTO.IdRequestDTO idRequestDTO) {

        // 일정 정보 가져오기
        Schedule schedule = scheduleRepository.findById(idRequestDTO.getId());
        Preconditions.checkNotNull(schedule, "등록된 스케줄 없습니다. (스케줄 ID : %s)", idRequestDTO.getId());

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 정보 등록인이거나 관리자인지 검사
        Preconditions.checkArgument(memberId == schedule.getTours().getGuide().getId() || member.getRole() == MemberDTO.MemberRole.ADMIN, "해당 여행 일정 등록자가 아닙니다. (여행 일정 ID : %s, 삭제 요청 회원 ID : %s)", idRequestDTO.getId(), memberId);

        scheduleRepository.delete(schedule);

    }

    // 여행 일정 불러오기
    public List<ScheduleDTO.ViewResponseDTO> viewSchedule(ToursDTO.IdRequestDTO idRequestDTO) {

        // 해당 TourId에 맞는 여행 일정 정보 가져오기
        List<Schedule> scheduleList = scheduleRepository.findAllByToursId(idRequestDTO.getId());

        // 반환할 DTO 리스트 생성
        List<ScheduleDTO.ViewResponseDTO> viewResponseDTOList = new ArrayList<>();

        for (Schedule schedule : scheduleList) {
            // 받아온 Schedule Entity를 Schedule DTO에 담기
            ScheduleDTO.ViewResponseDTO viewResponseDTO = new ScheduleDTO.ViewResponseDTO(schedule);
            // 여행 일정 옵션 DTO 가져오기
            List<ScheduleItemDTO.ViewResponseDTO> scheduleItemDTOList = scheduleItemService.viewScheduleItemList(schedule.getId());
            // Schedule DTO에 담기
            viewResponseDTO.setScheduleItemDTOList(scheduleItemDTOList);
            // 반환할 DTO 리스트에 DTO 담기
            viewResponseDTOList.add(viewResponseDTO);
        }

        return viewResponseDTOList;

    }
}
