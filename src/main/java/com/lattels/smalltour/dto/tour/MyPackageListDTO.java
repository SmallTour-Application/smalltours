package com.lattels.smalltour.dto.tour;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 내 패키지 목록 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPackageListDTO {

    @ApiParam("패키지 개수")
    private int count;

    @ApiParam("패키지 목록")
    private List<MyPackageDTO> content;

}
