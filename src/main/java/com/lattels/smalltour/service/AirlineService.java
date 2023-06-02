package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.AirlineDTO;
import com.lattels.smalltour.dto.FlightDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.ToursDTO;
import com.lattels.smalltour.model.Airline;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.AirlineRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirlineService {

    private final AirlineRepository airlineRepository;

    private final ToursRepository toursRepository;

    private final MemberRepository memberRepository;

    private final FlightService flightService;

    // 항공사 등록
    public void addAirline(int memberId, AirlineDTO.AddRequestDTO addRequestDTO) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 패키지 등록인이 해당 member인지 검사
        Tours tours = toursRepository.findByIdAndGuideId(addRequestDTO.getTourId(), memberId);
        Preconditions.checkNotNull(tours, "해당 유저와 패키지가 등록인이 다릅니다. (회원 ID : %s, 패키지 ID : %s)", memberId, addRequestDTO.getTourId());

        Airline airline = Airline.builder()
                .tours(tours)
                .name(addRequestDTO.getName())
                .country(addRequestDTO.getCountry())
                .contactEmail(addRequestDTO.getContactEmail())
                .contactTel(addRequestDTO.getContactTel())
                .build();

        airlineRepository.save(airline);

    }

    // 항공사 수정
    public void updateAirline(int memberId, AirlineDTO.UpdateRequestDTO updateRequestDTO) {

        // 항공사 정보 가져오기
        Airline airline = airlineRepository.findById(updateRequestDTO.getId());
        Preconditions.checkNotNull(airline, "등록된 항공사가 없습니다. (항공사 ID : %s)", updateRequestDTO.getId());

        // 항공사 등록인이 맞는지 검사
        Preconditions.checkArgument(memberId == airline.getTours().getGuide().getId(), "해당 유저와 등록인이 일치하지 않습니다. (수정 요청 회원 ID : %s, 기존 등록 회원 ID : %s)", memberId, airline.getTours().getGuide().getId());

        airline.setName(updateRequestDTO.getName());
        airline.setCountry(updateRequestDTO.getCountry());
        airline.setContactEmail(updateRequestDTO.getContactEmail());
        airline.setContactTel(updateRequestDTO.getContactTel());

        airlineRepository.save(airline);

    }

    // 항공사 삭제
    public void deleteAirline(int memberId, AirlineDTO.IdRequestDTO idRequestDTO) {

        // 항공사 정보 가져오기
        Airline airline = airlineRepository.findById(idRequestDTO.getId());
        Preconditions.checkNotNull(airline, "등록된 항공사가 없습니다. (항공사 ID : %s)", idRequestDTO.getId());

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 정보 등록인이거나 관리자인지 검사
        Preconditions.checkArgument(memberId == airline.getTours().getGuide().getId() || member.getRole() == MemberDTO.MemberRole.ADMIN, "해당 항공사 정보 등록자가 아닙니다. (항공사 정보 ID : %s, 삭제 요청 회원 ID : %s)", idRequestDTO.getId(), memberId);

        airlineRepository.delete(airline);

    }

    // 항공사 정보 불러오기
    public List<AirlineDTO.ViewResponseDTO> viewAirline(ToursDTO.IdRequestDTO idRequestDTO) {

        // 해당 TourId에 맞는 항공사 정보 가져오기
        List<Airline> airlineList = airlineRepository.findAllByToursId(idRequestDTO.getId());

        // 반환할 DTO 리스트 생성
        List<AirlineDTO.ViewResponseDTO> viewResponseDTOList = new ArrayList<>();

        for (Airline airline : airlineList) {
            // 받아온 Airline Entity를 Airline DTO에 담기
            AirlineDTO.ViewResponseDTO viewResponseDTO = new AirlineDTO.ViewResponseDTO(airline);
            // 비행기 정보 DTO 받아오기
            FlightDTO.ViewResponseDTO flightDTO = flightService.viewFlight(airline.getId());
            // Airline DTO에 담기
            viewResponseDTO.setFlightDTO(flightDTO);
            // 반환할 DTO 리스트에 DTO 담기
            viewResponseDTOList.add(viewResponseDTO);
        }

        return viewResponseDTOList;

    }
}
