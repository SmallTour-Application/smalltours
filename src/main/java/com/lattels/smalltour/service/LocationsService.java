package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.LocationsDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.model.Hotel;
import com.lattels.smalltour.model.Locations;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.LocationsRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationsService {

    private final LocationsRepository locationsRepository;

    private final ToursRepository toursRepository;

    private final MemberRepository memberRepository;

    // 투어 위치 등록
    public void addLocations(int memberId, LocationsDTO.AddRequestDTO addRequestDTO) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 패키지 등록인이 해당 member인지 검사
        Tours tours = toursRepository.findByIdAndGuideId(addRequestDTO.getTourId(), memberId);
        Preconditions.checkNotNull(tours, "해당 유저와 패키지가 등록인이 다릅니다. (회원 ID : %s, 패키지 ID : %s)", memberId, addRequestDTO.getTourId());

        Locations locations = Locations.builder()
                .tours(tours)
                .locationName(addRequestDTO.getLocationName())
                .country(addRequestDTO.getCountry())
                .region(addRequestDTO.getRegion())
                .city(addRequestDTO.getCity())
                .locationX(addRequestDTO.getLocationX())
                .locationY(addRequestDTO.getLocationY())
                .build();

        locationsRepository.save(locations);

    }

    // 투어 위치 수정
    public void updateLocations(int memberId, LocationsDTO.UpdateRequestDTO updateRequestDTO) {

        // 위치 정보 가져오기
        Locations locations = locationsRepository.findById(updateRequestDTO.getId());
        Preconditions.checkNotNull(locations, "등록된 위치 정보가 없습니다. (위치 ID : %s)", updateRequestDTO.getId());

        // 호텔 등록인이 맞는지 검사
        Preconditions.checkArgument(memberId == locations.getTours().getGuide().getId(), "해당 유저와 등록인이 일치하지 않습니다. (수정 요청 회원 ID : %s, 기존 등록 회원 ID : %s)", memberId, locations.getTours().getGuide().getId());

        locations.setLocationName(updateRequestDTO.getLocationName());
        locations.setCountry(updateRequestDTO.getCountry());
        locations.setRegion(updateRequestDTO.getRegion());
        locations.setCity(updateRequestDTO.getCity());
        locations.setLocationX(updateRequestDTO.getLocationX());
        locations.setLocationY(updateRequestDTO.getLocationY());

        locationsRepository.save(locations);

    }

    // 위치 정보 삭제
    public void deleteLocations(int memberId, LocationsDTO.IdRequestDTO idRequestDTO) {

        // 위치 정보 가져오기
        Locations locations = locationsRepository.findById(idRequestDTO.getId());
        Preconditions.checkNotNull(locations, "등록된 위치 정보가 없습니다. (위치 정보 ID : %s)", idRequestDTO.getId());

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 정보 등록인이거나 관리자인지 검사
        Preconditions.checkArgument(memberId == locations.getTours().getGuide().getId() || member.getRole() == MemberDTO.MemberRole.ADMIN, "해당 위치 정보 등록자가 아닙니다. (위치 정보 ID : %s, 삭제 요청 회원 ID : %s)", idRequestDTO.getId(), memberId);

        locationsRepository.delete(locations);

    }
}
