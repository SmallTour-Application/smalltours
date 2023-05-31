package com.lattels.smalltour.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class favoriteTourDTO {
    private int tourId;
    private String tourName;
    private String tourThumb;
    private int favorite; //해당 상품의 좋아요 수


}
