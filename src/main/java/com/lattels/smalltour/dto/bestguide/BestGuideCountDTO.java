package com.lattels.smalltour.dto.bestguide;

import com.lattels.smalltour.model.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BestGuideCountDTO {

    @ApiModelProperty(value = "Guide", example = "1")
    private Member guide;

    @ApiModelProperty(value = "우수 가이드 선정 횟수", example = "1")
    private long cnt;

    public BestGuideCountDTO(Member guide, long cnt) {
        this.guide = guide;
        this.cnt = cnt;
    }
}
