package com.lattels.smalltour.dto.admin.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lattels.smalltour.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMemberDTO {


    private int id; // 사용자에게 고유하게 부여되는 값

    private int memberId;

    private LocalDateTime loginDateTime;

    private String connectionType;

    private String os;

    private String broswer;

    private String ip;

    private int state;


}
