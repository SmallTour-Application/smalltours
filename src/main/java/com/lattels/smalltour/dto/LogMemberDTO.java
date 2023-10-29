package com.lattels.smalltour.dto;

import com.lattels.smalltour.model.Locations;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class LogMemberDTO {
   private String region;
}
