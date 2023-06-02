package com.lattels.smalltour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lattels.smalltour.model.Tours;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class ToursDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "투어 ID 요청 DTO")
    public static class IdRequestDTO {

        @ApiModelProperty(value = "투어 ID", example = "1")
        private int id;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "투어 등록 요청 DTO")
    public static class AddRequestDTO {

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "투어 이름", example = "투어 이름입니다")
        private String title;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "투어 부제목", example = "투어 부제목입니다")
        private String subTitle;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "투어 설명", example = "투어 설명입니다")
        private String description;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "만나는 장소(시간)", example = "만나는 장소 (시간)")
        private String meetingPoint;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "투어 기간", example = "1")
        private int duration;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "투어 가격", example = "10000")
        private int price;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 최대 인원", example = "1")
        private int maxGroupSize;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 최소 인원", example = "1")
        private int minGroupSize;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "투어 기본 가격", example = "10000")
        private int defaultPrice;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "투어 수정 요청 DTO")
    public static class UpdateRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 ID", example = "1")
        private int id;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "투어 이름", example = "투어 이름입니다")
        private String title;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "투어 부제목", example = "투어 부제목입니다")
        private String subTitle;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "투어 설명", example = "투어 설명입니다")
        private String description;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "만나는 장소(시간)", example = "만나는 장소 (시간)")
        private String meetingPoint;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "투어 기간", example = "1")
        private int duration;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "투어 가격", example = "10000")
        private int price;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 최대 인원", example = "1")
        private int maxGroupSize;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "투어 최소 인원", example = "1")
        private int minGroupSize;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "투어 기본 가격", example = "10000")
        private int defaultPrice;

    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "투어 가져오기 요청 DTO")
    public static class ViewResponseDTO {

        @ApiModelProperty(value = "투어 ID", example = "1")
        private int id;

        @ApiModelProperty(value = "가이드 ID", example = "1")
        private int memberId;

        @ApiModelProperty(value = "투어 이름", example = "투어 이름입니다")
        private String title;

        @ApiModelProperty(value = "투어 부제목", example = "투어 부제목입니다")
        private String subTitle;

        @ApiModelProperty(value = "투어 설명", example = "투어 설명입니다")
        private String description;

        @ApiModelProperty(value = "만나는 장소(시간)", example = "만나는 장소 (시간)")
        private String meetingPoint;

        @ApiModelProperty(value = "투어 기간", example = "1")
        private int duration;

        @ApiModelProperty(value = "투어 가격", example = "10000")
        private int price;

        @ApiModelProperty(value = "투어 최대 인원", example = "1")
        private int maxGroupSize;

        @ApiModelProperty(value = "투어 최소 인원", example = "1")
        private int minGroupSize;

        @ApiModelProperty(value = "투어 기본 가격", example = "10000")
        private int defaultPrice;

        @ApiModelProperty(value = "투어 생성일", example = "")
        private LocalDateTime createdDay;

        @ApiModelProperty(value = "투어 수정일", example = "")
        private LocalDateTime updateDay;

        @ApiModelProperty(value = "투어 상태", example = "1")
        private int approvals;

        @ApiModelProperty(value = "투어 썸네일", example = "투어 썸네일입니다")
        private String thumb;

        // 가이드 프로필 DTO
        MemberDTO.ProfileResponseDTO profileResponseDTO;

        // 특정 기간 투어 잠금 DTO
        List<GuideLockDTO.ViewResponseDTO> guideLockDTOList;

        // 투어 위치 DTO
        LocationsDTO.ViewResponseDTO locationsDTO;

        // 투어 이미지 리스트 DTO
        List<ToursImagesDTO.ViewResponseDTO> toursImagesDTOList;

        public ViewResponseDTO(Tours tours) {
            this.id = tours.getId();
            this.memberId = tours.getGuide().getId();
            this.title = tours.getTitle();
            this.subTitle = tours.getSubTitle();
            this.description = tours.getDescription();
            this.meetingPoint = tours.getMeetingPoint();
            this.duration = tours.getDuration();
            this.price = tours.getPrice();
            this.maxGroupSize = tours.getMaxGroupSize();
            this.minGroupSize = tours.getMinGroupSize();
            this.defaultPrice = tours.getDefaultPrice();
            this.createdDay = tours.getCreatedDay();
            this.updateDay = tours.getUpdateDay();
            this.approvals = tours.getApprovals();
        }

    }

    public static class ToursApprovals {

        // 미승인
        public static final int UNAPPROVED = 0;

        // 승인
        public static final int APPROVAL = 1;

        // 정지
        public static final int PAUSE = 2;

        // 삭제
        public static final int DELETE = 3;

    }
}