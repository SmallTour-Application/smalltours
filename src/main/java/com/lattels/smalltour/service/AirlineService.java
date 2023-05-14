package com.lattels.smalltour.service;

import com.lattels.smalltour.dto.AirlineDTO;
import com.lattels.smalltour.model.Airline;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.AirlineRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirlineService {

    private final AirlineRepository airlineRepository;

    private final ToursRepository toursRepository;

    // 항공사 등록
    public void addAirline(int memberId, AirlineDTO.AddRequestDTO addRequestDTO) {

        try {

            Tours tours = toursRepository.findById(addRequestDTO.getTourId());

            Airline airline = Airline.builder()
                    .tours(tours)
                    .name(addRequestDTO.getName())
                    .country(addRequestDTO.getCountry())
                    .contactEmail(addRequestDTO.getContactEmail())
                    .contactTel(addRequestDTO.getContactTel())
                    .build();

            airlineRepository.save(airline);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AirlineService.addAirline() : 에러 발생");
        }

    }

    // 항공사 수정
    public void updateAirline(int memberId, AirlineDTO.UpdateRequestDTO updateRequestDTO) {

        try {

            Airline airline = airlineRepository.findById(updateRequestDTO.getId());

            airline.setName(updateRequestDTO.getName());
            airline.setCountry(updateRequestDTO.getCountry());
            airline.setContactEmail(updateRequestDTO.getContactEmail());
            airline.setContactTel(updateRequestDTO.getContactTel());

            airlineRepository.save(airline);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AirlineService.updateAirline() : 에러 발생");
        }
    }

    // 항공사 삭제
    public void deleteAirline(int memberId, AirlineDTO.IdRequestDTO idRequestDTO) {

        try {

            Airline airline = airlineRepository.findById(idRequestDTO.getId());

            airlineRepository.delete(airline);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AirlineService.deleteAirline() : 에러 발생");
        }
    }
}
