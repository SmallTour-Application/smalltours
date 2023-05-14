package com.lattels.smalltour.dto.question;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lattels.smalltour.model.Question;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 내 질문 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    @ApiParam("질문 ID")
    private int questionId;

    @ApiParam("패키지 ID")
    private int tourId;

    @ApiParam("패키지명")
    private String tourName;

    @ApiParam("가이드 ID")
    private int guideId;

    @ApiParam("가이드명")
    private String guideName;

    @ApiParam("질문자 ID")
    private int memberId;

    @ApiParam("질문자 닉네임")
    private String nickname;

    @ApiParam("질문 제목")
    private String title;

    @ApiParam("질문 내용")
    private String content;

    @ApiParam("질문 답변")
    private String answer;

    @ApiParam("공개 여부")
    @JsonProperty("public")
    private boolean isPublic;

    @ApiParam("작성일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDay;

    @ApiParam("이미지 경로")
    private String image;

    public QuestionDTO(Question question) {
        this.questionId = question.getId();
        this.tourId = question.getTours().getId();
        this.tourName = question.getTours().getTitle();
        this.guideId = question.getTours().getGuide().getId();
        this.guideName = question.getTours().getGuide().getName();
        this.memberId = question.getMember().getId();
        this.nickname = question.getMember().getNickname();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.answer = question.getAnswer().isEmpty() ? null : question.getAnswer().get(0).getContent();
        this.isPublic = question.isPublic();
        this.createdDay = question.getCreatedDay();
        this.image = question.getImage();
    }

}