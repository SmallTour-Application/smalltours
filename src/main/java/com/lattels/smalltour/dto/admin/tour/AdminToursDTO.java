package com.lattels.smalltour.dto.admin.tour;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lattels.smalltour.model.Question;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 질문 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminToursDTO {

    private long count; //총 결과물 갯수
    private List<AdminToursList> adminToursList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminToursList {
        //번호,패키지명,생성일시,수정일시,상태
        @ApiParam("패키지 ID")
        private int tourId;

        @ApiParam("패키지명")
        private String tourName;

        @ApiParam("생성일")
        private LocalDateTime createdDay;

        @ApiParam("수정일시")
        private LocalDateTime updateDay;

        @ApiParam("상태")
        private String approval;
    }

}