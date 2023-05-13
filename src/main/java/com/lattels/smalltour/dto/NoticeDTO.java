package com.lattels.smalltour.dto;

import com.lattels.smalltour.model.Notice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "아이디 요청 DTO")
    public static class IdRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "공지 ID", example = "1")
        private int id;

    }

    // 공지 작성 요청 DTO
    @Getter
    @NoArgsConstructor
    @ApiModel(value = "공지 작성 요청 DTO")
    public static class WriteRequestDTO {

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "제목", example = "제목입니다")
        private String title;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "내용", example = "내용입니다")
        private String content;

    }

    // 공지 수정 요청 DTO
    @Getter
    @NoArgsConstructor
    @ApiModel(value = "공지 수정 요청 DTO")
    public static class UpdateRequestDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "공지 ID", example = "1")
        private int id;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "제목", example = "제목입니다")
        private String title;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "내용", example = "내용입니다")
        private String content;

    }

    // 공지 리스트 응답 DTO
    @Getter
    @NoArgsConstructor
    @ApiModel(value = "공지 리스트 응답 DTO")
    public static class ListResponseDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "공지 ID", example = "1")
        private int id;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "멤버 ID", example = "1")
        private int memberId;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "제목", example = "제목입니다")
        private String title;

        @ApiModelProperty(value = "작성일", example = "2023-04-13 01:47:52.000")
        private LocalDateTime createdDay;

        @ApiModelProperty(value = "수정일", example = "2023-04-13 01:47:52.000")
        private LocalDateTime updatedDay;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "조회수", example = "1")
        private int view;

        public ListResponseDTO(Object[] objects) {
            this.id = (int) objects[0];
            this.memberId = (int) objects[1];
            this.title = (String) objects[2];
            this.createdDay = timestampToLocalDateTime((Timestamp) objects[3]);
            this.updatedDay = timestampToLocalDateTime((Timestamp) objects[4]);
            this.view = (int) objects[5];
        }

//        public ListResponseDTO(Notice notice) {
//            this.id = notice.getId();
//            this.memberId = notice.getMemberId();
//            this.title = notice.getTitle();
//            this.content = notice.getContent();
//            this.createdDay = notice.getCreatedDay();
//            this.updatedDay = notice.getUpdatedDay();
//            this.views = notice.getViews();
//        }


//        // Timestamp -> LocalDateTime 변환
        public LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }


    }

    // 공지 보기 응답 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @ApiModel(value = "공지 보기 응답 DTO")
    public static class ViewResponseDTO {

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "공지 ID", example = "1")
        private int id;

        @Positive(message = "양수만 가능합니다.")
        @ApiModelProperty(value = "멤버 ID", example = "1")
        private int memberId;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "제목", example = "제목입니다")
        private String title;

        @NotBlank(message = "필수 입력 값입니다.")
        @ApiModelProperty(value = "내용", example = "내용입니다")
        private String content;

        @ApiModelProperty(value = "작성일", example = "2023-04-13 01:47:52.000")
        private LocalDateTime createdDay;

        @ApiModelProperty(value = "수정일", example = "2023-04-13 01:47:52.000")
        private LocalDateTime updatedDay;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "조회수", example = "1")
        private int view;

        public ViewResponseDTO(Notice notice) {
            this.id = notice.getId();
            this.memberId = notice.getMemberId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.createdDay = notice.getCreatedDay();
            this.updatedDay = notice.getUpdatedDay();
            this.view = notice.getView();
        }

    }

    @Positive(message = "양수만 가능합니다.")
    @ApiModelProperty(value = "멤버 ID", example = "1")
    private int memberId;

        @ApiModelProperty(value = "작성일", example = "2023-04-13 01:47:52.000")
        private LocalDateTime createdDay;

        @ApiModelProperty(value = "수정일", example = "2023-04-13 01:47:52.000")
        private LocalDateTime updatedDay;

        @PositiveOrZero(message = "0 또는 양수만 가능합니다.")
        @ApiModelProperty(value = "조회수", example = "1")
        private int view;


}
