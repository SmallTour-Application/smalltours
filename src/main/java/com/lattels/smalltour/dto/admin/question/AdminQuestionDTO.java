package com.lattels.smalltour.dto.admin.question;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lattels.smalltour.model.Question;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 질문 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminQuestionDTO {

    @ApiParam("질문 ID")
    private int questionId;

    @ApiParam("패키지 ID")
    private int tourId;

    @ApiParam("패키지명")
    private String tourName;

    @ApiParam("회원 Id")
    private int memberId;

    @ApiParam("질문자 닉네임")
    private String nickname;

    @ApiParam("질문 제목")
    private String title;

    @ApiParam("질문 내용")
    private String content;


    @ApiParam("질문 답변 여부")
    private String answer;


    @ApiParam("공개여부")
    private boolean isPublic;

    @ApiParam("작성일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDay;


    public AdminQuestionDTO(Question question) {
        this.questionId = question.getId();
        this.tourId = question.getTours().getId();
        this.tourName = question.getTours().getTitle();
        this.memberId = question.getMember().getId();
        this.nickname = question.getMember().getNickname();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.answer = question.getAnswer().isEmpty() ? "미처리" : "처리됨";
        this.createdDay = question.getCreatedDay();
        this.isPublic = question.isPublic();
    }

}