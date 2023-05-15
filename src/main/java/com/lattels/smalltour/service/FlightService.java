package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.AirlineDTO;
import com.lattels.smalltour.dto.FlightDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.model.Airline;
import com.lattels.smalltour.model.Flight;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.AirlineRepository;
import com.lattels.smalltour.persistence.FlightRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    private final AirlineRepository airlineRepository;

    private final MemberRepository memberRepository;

    // 비행기 정보 등록
    public void addFlight(int memberId, FlightDTO.AddRequestDTO addRequestDTO) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 패키지 등록인이 해당 member인지 검사
        Airline airline = airlineRepository.findByIdAndMember(addRequestDTO.getAirlineId(), member);
        Preconditions.checkNotNull(airline, "해당 유저와 항공사 등록인이 다릅니다. (회원 ID : %s, 항공사 ID : %s)", memberId, addRequestDTO.getAirlineId());

        Flight flight = Flight.builder()
                .airline(airline)
                .departDateTime(addRequestDTO.getDepartDateTime())
                .arrivalDateTime(addRequestDTO.getArrivalDateTime())
                .departCity(addRequestDTO.getDepartCity())
                .arrivalAirport(addRequestDTO.getArrivalAirport())
                .duration(addRequestDTO.getDuration())
                .price(addRequestDTO.getPrice())
                .flightName(addRequestDTO.getFlightName())
                .seatType(addRequestDTO.getSeatType())
                .build();

        flightRepository.save(flight);

    }

    // 비행기 정보 수정
    public void updateFlight(int memberId, FlightDTO.UpdateRequestDTO updateRequestDTO) {

        // 비행기 정보 가져오기
        Flight flight = flightRepository.findById(updateRequestDTO.getId());
        Preconditions.checkNotNull(flight, "등록된 비행기가 없습니다. (비행기 ID : %s)", updateRequestDTO.getId());

        // 비행기 등록인이 맞는지 검사
        Preconditions.checkArgument(memberId == flight.getAirline().getTours().getGuide().getId(), "해당 유저와 등록인이 일치하지 않습니다. (수정 요청 회원 ID : %s, 기존 등록 회원 ID : %s)", memberId, flight.getAirline().getTours().getGuide().getId());

        flight.setDepartDateTime(updateRequestDTO.getDepartDateTime());
        flight.setArrivalDateTime(updateRequestDTO.getArrivalDateTime());
        flight.setDepartCity(updateRequestDTO.getDepartCity());
        flight.setArrivalAirport(updateRequestDTO.getArrivalAirport());
        flight.setDuration(updateRequestDTO.getDuration());
        flight.setPrice(updateRequestDTO.getPrice());
        flight.setFlightName(updateRequestDTO.getFlightName());
        flight.setSeatType(updateRequestDTO.getSeatType());

        flightRepository.save(flight);

    }

    // 비행기 삭제
    public void deleteFlight(int memberId, FlightDTO.IdRequestDTO idRequestDTO) {

        // 비행기 정보 가져오기
        Flight flight = flightRepository.findById(idRequestDTO.getId());
        Preconditions.checkNotNull(flight, "등록된 비행기가 없습니다. (비행기 ID : %s)", idRequestDTO.getId());

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 정보 등록인이거나 관리자인지 검사
        Preconditions.checkArgument(memberId == flight.getAirline().getTours().getGuide().getId() || member.getRole() == MemberDTO.MemberRole.ADMIN);

        flightRepository.delete(flight);

    }
}
