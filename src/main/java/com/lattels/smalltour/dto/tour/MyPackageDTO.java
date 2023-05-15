package com.lattels.smalltour.dto.tour;

import com.lattels.smalltour.model.Tours;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 내 패키지 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPackageDTO {

    @ApiParam("패키지 ID")
    private int packageId;

    @ApiParam("패키지 이름")
    private String packageName;

    @ApiParam("등록 여부")
    private boolean approvals;

    @ApiParam("등록일")
    private LocalDateTime createdDay;

    public MyPackageDTO(Tours tours) {
        this.packageId = tours.getId();
        this.packageName = tours.getTitle();
        this.approvals = tours.getApprovals() == 1;
        this.createdDay = tours.getCreatedDay();
    }

}
