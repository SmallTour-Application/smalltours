package com.lattels.smalltour.dto.admin.tours;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminToursDTO {

    @Builder
    @Getter
    @Setter
    public static class ToursDTO{
        // 투어 아이디
        private int toursId;
        // 투어명
        private String toursTitle;
        // 생성일시
        private LocalDateTime createDate;
        // 수정일시
        private LocalDateTime updateDate;
        // 상태
        private int state;
        // 판매량
        private int sales;
    }


    @Builder
    @Getter
    @Setter
    public static class ToursDTOList{
        // 총 갯수
        private long totalCnt;
        // 투어 정보 목록
        private List<ToursDTO> toursList;
    }
}
