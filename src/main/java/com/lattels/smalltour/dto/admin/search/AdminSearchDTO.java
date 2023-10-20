package com.lattels.smalltour.dto.admin.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSearchDTO {
    private int count; // 검색결과 갯수
    private List<ContentMember> contentMember; //회원으로 검색


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentMember {
        private int id; //해당 멤버 id값
        private String memberName;
        private String memberEmail;
        private String memberTel;
        private LocalDate birthDay;
        private LocalDateTime joinDay;
        private int role;
    }

}