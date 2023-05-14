package com.lattels.smalltour.service;

import com.lattels.smalltour.dto.AirlineDTO;
import com.lattels.smalltour.dto.FlightDTO;
import com.lattels.smalltour.model.Airline;
import com.lattels.smalltour.model.Flight;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.AirlineRepository;
import com.lattels.smalltour.persistence.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    private final AirlineRepository airlineRepository;

    // 항공사 등록
    public void addFlight(int memberId, FlightDTO.AddRequestDTO addRequestDTO) {

        try {

            Airline airline = airlineRepository.findById(addRequestDTO.getAirlineId());

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

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FlightService.addFlight() : 에러 발생");
        }

    }

    // 항공사 수정
    public void updateFlight(int memberId, FlightDTO.UpdateRequestDTO updateRequestDTO) {

        try {

            Flight flight = flightRepository.findById(updateRequestDTO.getId());

            flight.setDepartDateTime(updateRequestDTO.getDepartDateTime());
            flight.setArrivalDateTime(updateRequestDTO.getArrivalDateTime());
            flight.setDepartCity(updateRequestDTO.getDepartCity());
            flight.setArrivalAirport(updateRequestDTO.getArrivalAirport());
            flight.setDuration(updateRequestDTO.getDuration());
            flight.setPrice(updateRequestDTO.getPrice());
            flight.setFlightName(updateRequestDTO.getFlightName());
            flight.setSeatType(updateRequestDTO.getSeatType());

            flightRepository.save(flight);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FlightService.updateFlight() : 에러 발생");
        }
    }

    // 항공사 삭제
    public void deleteFlight(int memberId, FlightDTO.IdRequestDTO idRequestDTO) {

        try {

            Flight flight = flightRepository.findById(idRequestDTO.getId());

            flightRepository.delete(flight);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FlightService.deleteFlight() : 에러 발생");
        }
    }
}
