package com.lattels.smalltour.dto.admin.education;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lattels.smalltour.model.Education;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class EducationVideoDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "동영상 ID 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "동영상 ID", example = "1")
        private int id;


        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "동영상 이름", example = "동영상 이름입니다")
        private String title;

        public IdRequestDTO(int id) {
            this.id = id;
        }
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "동영상 업로드 요청 DTO")
    public static class UploadRequestDTO {

  /*      @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "목록 제목", example = "목록 이름입니다")
        private String postTitle;*/

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "동영상 이름", example = "동영상 이름입니다")
        private String title;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDay;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDay;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "동영상 수정 요청 DTO")
    public static class UpdateRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "동영상 ID", example = "1")
        private int id;


        @ApiModelProperty(value = "동영상 이름", example = "동영상 이름입니다")
        private String title;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDay;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDay;

    }


    /*
     * 동영상 정보 응답
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "동영상 정보 응답 DTO")
    public static class ViewResponseDTO {

        @ApiModelProperty(value = "비디오 ID", example = "1")
        private int id;

        @ApiModelProperty(value = "비디오 경로", example = "")
        private String videoPath;
        @ApiModelProperty(value = "교육 시작일", example = "")
        private LocalDate startDay;

        @ApiModelProperty(value = "교육 종료일", example = "")
        private LocalDate endDay;


        @ApiModelProperty(value = "재생시간", example = "1")
        private LocalTime playTime;

        @ApiModelProperty(value = "제목", example = "")
        private String title;

        @ApiModelProperty(value = "순서", example = "1")
        private int sequence;

        @ApiModelProperty(value = "비디오 파일", example = "~.mp4")
        private String path;

        @ApiModelProperty(value = "업로드 날짜", example = "~.mp4")
        private LocalDate uploadDay;


        public ViewResponseDTO(Education education) {
            this.id = education.getId();
            this.videoPath = education.getVideoPath();
            this.startDay = education.getStartDay();
            this.endDay = education.getEndDay();
            this.playTime = education.getPlayTime();
            this.title = education.getTitle();
            this.uploadDay = education.getUploadDay();

        }
    }
}