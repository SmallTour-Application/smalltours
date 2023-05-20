
package com.lattels.smalltour.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
///schedule/guide 가이드스케줄 DTO
public class GuideScheduleDTO {
    private String date;
    private int packageId;
    private String packageName;
    private String memberNickName;
    private String memberTel;

}

