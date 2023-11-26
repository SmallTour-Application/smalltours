package com.lattels.smalltour.service.admin;


import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.admin.question.AdminQuestionDTO;
import com.lattels.smalltour.dto.admin.question.AdminQuestionListDTO;
import com.lattels.smalltour.dto.admin.tour.AdminToursDTO;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Question;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.AnswerRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.QuestionRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminPackageService {
    private final MemberRepository memberRepository;
    private final ToursRepository toursRepository;

    @Value("${file.path}")
    private String filePath;

    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;

    @Value("${file.path.tours.images}")
    private String filePathToursImages;

    public File getQuestionDirectoryPath() {
        File file = new File(filePath);
        file.mkdirs();

        return file;
    }
    /**
     * 질문의 답변은 정환씨가 했던 QuestionAnswerService를 사용하면 됨
     */

    /**
     * 관리자 인지 체크
     */
    private void checkAdmin(final int adminId){
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자를 찾을수없습니다."));
        if(admin.getRole() != 3){
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }
    }

    /**
     * 전체 패키지 목록 가져오기
     * 번호,패키지명,생성일시,수정일시,상태 수정 및 삭제는 따로 메서드만들어서
     * 0:미승인 1:승인 2:일시정지 3:삭제
     */


    public AdminToursDTO getToursList(int adminId, Integer tourId,String title, int page, int count, Integer month, Integer year, Integer state) {
        checkAdmin(adminId);
        Pageable pageable = PageRequest.of(page, count);
        long resultTourInfo = 0;

        Page<Object[]> tourInfo;
        if(tourId == null && month == null && year == null && state == null && title == null) {
            tourInfo = toursRepository.findByConditionALL(month,year,tourId,state,title,pageable);
            resultTourInfo = toursRepository.countByAllConditions(month,year,tourId,state,title);
        }else{
            tourInfo = toursRepository.findByConditions(month,year,tourId,state,title,pageable);
            resultTourInfo = toursRepository.countByConditions(month,year,tourId,state,title);
        }

        List<AdminToursDTO.AdminToursList> adminTours = new ArrayList<>();

        for (Object[] tours : tourInfo) {
            int approvalValue = Integer.parseInt(String.valueOf(tours[4]));
            String approvalStatus;
            switch (approvalValue) {
                case 0:
                    approvalStatus = "미승인";
                    break;
                case 1:
                    approvalStatus = "승인";
                    break;
                case 2:
                    approvalStatus = "일시정지";
                    break;
                case 3:
                    approvalStatus = "삭제";
                    break;
                default:
                    approvalStatus = "뜨면안됨";
                    break;
            }

            AdminToursDTO.AdminToursList adminToursList = AdminToursDTO.AdminToursList.builder()
                    .tourId(Integer.parseInt(String.valueOf(tours[0])))
                    .tourName(String.valueOf(tours[1]))
                    .createdDay(((Timestamp) tours[2]).toLocalDateTime())
                    .updateDay(((Timestamp) tours[3]).toLocalDateTime())
                    .approval(approvalStatus)
                    .build();
            adminTours.add(adminToursList);
        }

        return AdminToursDTO.builder()
                .count(resultTourInfo)
                .adminToursList(adminTours)
                .build();
    }

    public void updateTour(int adminId, int tourId, String title, String subtitle, String description,String meetingPoint,Integer maxGroupSize, Integer minGroupSize) {
        checkAdmin(adminId);

        if (title != null) {
            toursRepository.updateTourTitle(tourId, title);
        }
        if (subtitle != null) {
            toursRepository.updateTourSubtitle(tourId, subtitle);
        }
        if (description != null) {
            toursRepository.updateTourDescription(tourId, description);
        }
        if (meetingPoint != null) {
            toursRepository.updateTourMeetingPoing(tourId, meetingPoint);
        }
        if (maxGroupSize != null) {
            toursRepository.updateTourMaxGroupSize(tourId, maxGroupSize);
        }
        if (minGroupSize != null) {
            toursRepository.updateTourMinGroupSize(tourId, minGroupSize);
        }

        if(title != null && subtitle != null && description != null){
            toursRepository.updateTourDetails(tourId, title, subtitle, description,meetingPoint,maxGroupSize,minGroupSize);

        }

    }

    public void deleteTour(int adminId, int tourId) {
        checkAdmin(adminId);
        toursRepository.deleteTourById(tourId);
    }
}