package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.ScheduleItemDTO;
import com.lattels.smalltour.model.Airline;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Schedule;
import com.lattels.smalltour.model.ScheduleItem;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ScheduleItemRepository;
import com.lattels.smalltour.persistence.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleItemService {

    private final ScheduleItemRepository scheduleItemRepository;

    private final MemberRepository memberRepository;

    private final ScheduleRepository scheduleRepository;

    // 일정 옵션 등록
    public void addScheduleItem(int memberId, ScheduleItemDTO.AddRequestDTO addRequestDTO) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 패키지 등록인이 해당 member인지 검사
        Schedule schedule = scheduleRepository.findByIdAndMember(addRequestDTO.getScheduleId(), member);
        Preconditions.checkNotNull(schedule, "해당 유저와 일정 등록인이 다릅니다. (회원 ID : %s, 일정 ID : %s)", memberId, addRequestDTO.getScheduleId());

        ScheduleItem scheduleItem = ScheduleItem.builder()
                .schedule(schedule)
                .title(addRequestDTO.getTitle())
                .content(addRequestDTO.getContent())
                .price(addRequestDTO.getPrice())
                .defaultItem(addRequestDTO.getDefaultItem())
                .locationX(addRequestDTO.getLocationX())
                .locationY(addRequestDTO.getLocationY())
                .build();

        scheduleItemRepository.save(scheduleItem);

    }

    // 일정 옵션 수정
    public void updateScheduleItem(int memberId, ScheduleItemDTO.UpdateRequestDTO updateRequestDTO) {

        // 옵션 가져오기
        ScheduleItem scheduleItem = scheduleItemRepository.findById(updateRequestDTO.getId());
        Preconditions.checkNotNull(scheduleItem, "등록된 일정 옵션이 없습니다. (옵션 ID : %s)", updateRequestDTO.getId());

        // 비행기 등록인이 맞는지 검사
        Preconditions.checkArgument(memberId == scheduleItem.getSchedule().getTours().getGuide().getId(), "해당 유저와 등록인이 일치하지 않습니다. (수정 요청 회원 ID : %s, 기존 등록 회원 ID : %s)", memberId, scheduleItem.getSchedule().getTours().getGuide().getId());

        scheduleItem.setTitle(updateRequestDTO.getTitle());
        scheduleItem.setContent(updateRequestDTO.getContent());
        scheduleItem.setPrice(updateRequestDTO.getPrice());
        scheduleItem.setDefaultItem(updateRequestDTO.getDefaultItem());
        scheduleItem.setLocationX(updateRequestDTO.getLocationX());
        scheduleItem.setLocationY(updateRequestDTO.getLocationY());

        scheduleItemRepository.save(scheduleItem);

    }

    // 옵션 삭제
    public void deleteScheduleItem(int memberId, ScheduleItemDTO.IdRequestDTO idRequestDTO) {

        // 옵션 가져오기
        ScheduleItem scheduleItem = scheduleItemRepository.findById(idRequestDTO.getId());
        Preconditions.checkNotNull(scheduleItem, "등록된 일정 옵션이 없습니다. (옵션 ID : %s)", idRequestDTO.getId());

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 정보 등록인이거나 관리자인지 검사
        Preconditions.checkArgument(memberId == scheduleItem.getSchedule().getTours().getGuide().getId() || member.getRole() == MemberDTO.MemberRole.ADMIN, "해당 옵션 등록자가 아닙니다. (옵션 ID : %s, 삭제 요청 회원 ID : %s)", idRequestDTO.getId(), memberId);
        scheduleItemRepository.delete(scheduleItem);

    }

    // 여행 일정 옵션 리스트 가져오기
    public List<ScheduleItemDTO.ViewResponseDTO> viewScheduleItemList(int scheduleId) {

        // 여행 일정 아이디로 옵션 리스트 가져오기
        List<ScheduleItem> scheduleItemList = scheduleItemRepository.findAllByScheduleId(scheduleId);
        // 반환할 DTO 리스트에 저장
        List<ScheduleItemDTO.ViewResponseDTO> viewResponseDTOList = scheduleItemList.stream()
                .map(scheduleItem -> new ScheduleItemDTO.ViewResponseDTO(scheduleItem))
                .collect(Collectors.toList());

        return viewResponseDTOList;

    }
}
