package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.HotelDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.model.Hotel;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.HotelRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    private final ToursRepository toursRepository;

    private final MemberRepository memberRepository;

    // 호텔 등록
    public void addHotel(int memberId, HotelDTO.AddRequestDTO addRequestDTO) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 패키지 등록인이 해당 member인지 검사
        Tours tours = toursRepository.findByIdAndGuideId(addRequestDTO.getTourId(), memberId);
        Preconditions.checkNotNull(tours, "해당 유저와 패키지가 등록인이 다릅니다. (회원 ID : %s, 패키지 ID : %s)", memberId, addRequestDTO.getTourId());

        Hotel hotel = Hotel.builder()
                .tours(tours)
                .name(addRequestDTO.getName())
                .address(addRequestDTO.getAddress())
                .hotelTel(addRequestDTO.getHotelTel())
                .description(addRequestDTO.getDescription())
                .hotelLocationX(addRequestDTO.getHotelLocationX())
                .hotelLocationY(addRequestDTO.getHotelLocationY())
                .build();

        hotelRepository.save(hotel);

    }

    // 호텔 수정
    public void updateHotel(int memberId, HotelDTO.UpdateRequestDTO updateRequestDTO) {

        // 호텔 정보 가져오기
        Hotel hotel = hotelRepository.findById(updateRequestDTO.getId());
        Preconditions.checkNotNull(hotel, "등록된 호텔이 없습니다. (호텔 ID : %s)", updateRequestDTO.getId());

        // 호텔 등록인이 맞는지 검사
        Preconditions.checkArgument(memberId == hotel.getTours().getGuide().getId(), "해당 유저와 등록인이 일치하지 않습니다. (수정 요청 회원 ID : %s, 기존 등록 회원 ID : %s)", memberId, hotel.getTours().getGuide().getId());

        hotel.setName(updateRequestDTO.getName());
        hotel.setAddress(updateRequestDTO.getAddress());
        hotel.setHotelTel(updateRequestDTO.getHotelTel());
        hotel.setHotelLocationX(updateRequestDTO.getHotelLocationX());
        hotel.setHotelLocationY(updateRequestDTO.getHotelLocationY());

        hotelRepository.save(hotel);

    }

    // 호텔 삭제
    public void deleteHotel(int memberId, HotelDTO.IdRequestDTO idRequestDTO) {

        // 호텔 정보 가져오기
        Hotel hotel = hotelRepository.findById(idRequestDTO.getId());
        Preconditions.checkNotNull(hotel, "등록된 호텔이 없습니다. (호텔 ID : %s)", idRequestDTO.getId());

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 정보 등록인이거나 관리자인지 검사
        Preconditions.checkArgument(memberId == hotel.getTours().getGuide().getId() || member.getRole() == MemberDTO.MemberRole.ADMIN, "해당 호텔 정보 등록자가 아닙니다. (호텔 정보 ID : %s, 삭제 요청 회원 ID : %s)", idRequestDTO.getId(), memberId);

        hotelRepository.delete(hotel);

    }

}
