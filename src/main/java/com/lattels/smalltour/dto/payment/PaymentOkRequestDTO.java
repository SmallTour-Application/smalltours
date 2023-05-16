package com.lattels.smalltour.dto.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 결제 요청 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOkRequestDTO {

    @ApiParam("패키지 ID")
    private int packageId;

    @ApiParam("여행 일정 아이템 ID 목록")
    private List<Integer> items;

    @ApiParam("여행 출발일")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate departureDay;

    @ApiParam("인원 수")
    private int people;

}
