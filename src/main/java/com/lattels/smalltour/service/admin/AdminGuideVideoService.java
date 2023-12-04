package com.lattels.smalltour.service.admin;


import com.lattels.smalltour.dto.admin.education.EducationDTO;
import com.lattels.smalltour.dto.admin.education.EducationGuideDTO;
import com.lattels.smalltour.dto.admin.education.EducationVideoDTO;
import com.lattels.smalltour.model.Education;
import com.lattels.smalltour.model.EducationLog;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.EducationLogRepository;
import com.lattels.smalltour.persistence.EducationRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.util.FileUploadProgressListener;
import com.lattels.smalltour.util.MultipartUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminGuideVideoService {
    private static final Logger logger = LoggerFactory.getLogger(AdminGuideVideoService.class);
    private final EducationRepository educationRepository;
    private final MemberRepository memberRepository;
    private final EducationLogRepository educationLogRepository;

    @Autowired
    private FileUploadProgressListener progressListener;

    @Autowired
    private HttpSession httpSession;

    @Value("${ffprobe.location}")
    private String ffprobePath;


    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;

    @Value("${file.path.education}")
    private String educationFilePath;

    public File getEducationDirectoryPath() {
        File file = new File(educationFilePath);
        file.mkdirs();

        return file;
    }

    /**
     * 관리자 인지 체크
     */
    private void checkAdmin(final int adminId){
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자를 찾을수없습니다."));
        if(admin.getRole() != 3){
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }
    }


    // 가이드 교육 수강 현황(0:수강완료 1:미수강 2:수강중)
    public EducationGuideDTO GuideEducationLog(int adminId, int page, int countPerPage, int state , int guideId){
        int i = 1;
        checkAdmin(adminId);

        Member member = memberRepository.findByMemberInfoId(guideId);
        if (member.getRole() != 2 || member == null) {
            throw new RuntimeException("해당 가이드를 찾을 수 없습니다.");
        }

        //강좌갯수
        int educationLogCount = educationRepository.countStateEducationLog(state,guideId);
        if(educationLogCount == 0){
            throw new IllegalArgumentException("현재 가이드에 해당하는 정보가 없습니다.");
        }
        // 페이지
        Pageable pageable = PageRequest.of(page, countPerPage);
        //로그 불러오기
        List<Object[]> results = educationRepository.findByGuideEducationGuideContent(pageable,state,guideId);
        List<EducationGuideDTO.EducationGuideListDTO> educationGuideListDTO = new ArrayList<>();
        for (Object[] result : results) {
            EducationGuideDTO.EducationGuideListDTO educationGuideLogListDTO = EducationGuideDTO.EducationGuideListDTO.builder()
                    .id(i++)
                    .educationTitle(((String)result[0]))
                    .startDay(((java.sql.Date)result[1]).toLocalDate())
                    .endDay(((java.sql.Date)result[2]).toLocalDate())
                    .state(state)
                    .completeDate((result[4] == null) ? null : ((java.sql.Date)result[4]).toLocalDate())
                    .build();
            educationGuideListDTO.add(educationGuideLogListDTO);
        }

        return EducationGuideDTO.builder()
                .count(educationLogCount)
                .educationGuideLogList(educationGuideListDTO)
                .build();
    }

    //state에 따라 다름
    public EducationGuideDTO getGuideEducationList(int adminId, int page, int countPerPage,int state,int memberId){
        return GuideEducationLog(adminId,page,countPerPage,state,memberId);
    }

    //가이드 강좌 수강 상태 변경(0:수강중 1:수강완료 2:미수강)
    public void updatestatus(int adminId,int guideId,int educationId,int state){
        checkAdmin(adminId);
        //education가져오기
        EducationLog educationLog = educationLogRepository.findByEducationIdAndGuideId(educationId,guideId);
        educationLog.setState(state);
        educationLogRepository.save(educationLog);
    }

    //교육기록 삭제
    public void deleteEducationInfo(int adminId, int educationId, int guideId){
        checkAdmin(adminId);
        EducationLog educationLog = educationLogRepository.findByEducationIdAndGuideId(educationId,guideId);
        educationLogRepository.delete(educationLog);
    }



}