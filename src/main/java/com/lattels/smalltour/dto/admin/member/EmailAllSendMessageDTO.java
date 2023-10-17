package com.lattels.smalltour.dto.admin.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAllSendMessageDTO {
    private String title;
    private String content;
    private boolean sendAll; //일괄전송
}
