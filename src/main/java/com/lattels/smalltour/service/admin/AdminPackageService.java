package com.lattels.smalltour.service.admin;


import com.lattels.smalltour.dto.admin.tours.AdminDetailImgPackageDTO;
import com.lattels.smalltour.dto.admin.tours.AdminDetailPackageDTO;
import com.lattels.smalltour.dto.admin.tours.AdminPackageDTO;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Value("${file.path.tours}")
    private String toursFilePath;

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
    private void checkAdmin(final int adminId) {
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자를 찾을수없습니다."));
        if (admin.getRole() != 3) {
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }
    }

    /**
     * 전체 패키지 목록 가져오기
     * 번호,패키지명,생성일시,수정일시,상태 수정 및 삭제는 따로 메서드만들어서
     * 0:미승인 1:승인 2:일시정지 3:삭제
     */
    public AdminPackageDTO getToursList(int adminId, Integer tourId, String title, int page, int count, Integer month, Integer year, Integer state) {
        checkAdmin(adminId);
        Pageable pageable = PageRequest.of(page, count);
        long resultTourInfo = 0;

        Page<Object[]> tourInfo;
        if (tourId == null && month == null && year == null && state == null && title == null) {
            tourInfo = toursRepository.findByConditionALL(month, year, tourId, state, title, pageable);
            resultTourInfo = toursRepository.countByAllConditions(month, year, tourId, state, title);
        } else {
            tourInfo = toursRepository.findByConditions(month, year, tourId, state, title, pageable);
            resultTourInfo = toursRepository.countByConditions(month, year, tourId, state, title);
        }

        List<AdminPackageDTO.AdminToursList> adminTours = new ArrayList<>();

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

            LocalDateTime updateDay = null;
            if (tours[3] != null) {
                updateDay = ((Timestamp) tours[3]).toLocalDateTime();
            }
            String formattedUpdateDay = updateDay == null ? "0000-00-00 00:00:00" : updateDay.toString();

            AdminPackageDTO.AdminToursList adminToursList = AdminPackageDTO.AdminToursList.builder()
                    .tourId(Integer.parseInt(String.valueOf(tours[0])))
                    .tourName(String.valueOf(tours[1]))
                    .createdDay(((Timestamp) tours[2]).toLocalDateTime())
                    .updateDay(formattedUpdateDay)
                    .approval(approvalStatus)
                    .price(Integer.parseInt(String.valueOf(tours[5])))
                    .maxPeople(Integer.parseInt(String.valueOf(tours[6])))
                    .minPeople(Integer.parseInt(String.valueOf(tours[7])))
                    .duration(Integer.parseInt(String.valueOf(tours[8])))
                    .guideName(String.valueOf(tours[9]))
                    .build();


            adminTours.add(adminToursList);
        }

        return AdminPackageDTO.builder()
                .count(resultTourInfo)
                .adminToursList(adminTours)
                .build();
    }



    public void updateTour(int adminId, int tourId, String title, String subtitle, String description, String meetingPoint, Integer maxGroupSize, Integer minGroupSize) {
        checkAdmin(adminId);

        if (maxGroupSize != null && minGroupSize != null && minGroupSize > maxGroupSize) {
            throw new IllegalArgumentException("최대인원을 넘어설수없습니다. 최소인원 업데이트할 경우");
        }

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

        if (title != null && subtitle != null && description != null) {
            toursRepository.updateTourDetails(tourId, title, subtitle, description, meetingPoint, maxGroupSize, minGroupSize);

        }

    }

    public void updateApprovals(int adminId,Integer tourId, Integer approvals) {
        checkAdmin(adminId);
        if(tourId != null && approvals != null)
        {
            toursRepository.updateTourApprovals(tourId,approvals);  //0:미승인 1:승인 2:일시정지 3:삭제
        }

    }


    public void deleteTour(int adminId, int tourId) {
        checkAdmin(adminId);
        toursRepository.deleteTourById(tourId);
    }


    /**
     * 패키지 관리(패키지관리)
     * 상세내용
     */
    public AdminDetailPackageDTO getToursDetailList(int adminId, Integer tourId, int page, int count) {
        checkAdmin(adminId);
        Pageable pageable = PageRequest.of(page, count);
        LocalDate checkDate = LocalDate.now(); // 현재 날짜 or 필요한 날짜를 변수로 설정
        Page<Object[]> tourDetailInfo = toursRepository.findPageTourWithAvgRatingAndGuideLock(tourId, checkDate, pageable);

        long resultDetailTourInfo = tourDetailInfo.getTotalElements();
        List<AdminDetailPackageDTO.AdminToursDetailList> adminDetailTours = new ArrayList<>();

        for (Object[] tours : tourDetailInfo) {
            AdminDetailPackageDTO.AdminToursDetailList adminDetailToursList = AdminDetailPackageDTO.AdminToursDetailList.builder()
                    .tourId(Integer.parseInt(String.valueOf(tours[0])))
                    .tourName(String.valueOf(tours[1]))
                    .guideId(Integer.parseInt(String.valueOf(tours[2])))
                    .tourSeller(String.valueOf(tours[3]))
                    .rating(Float.parseFloat(String.valueOf(tours[4])))
                    .duration(Integer.parseInt(String.valueOf(tours[5])))
                    .price(Integer.parseInt(String.valueOf(tours[6])))
                    .maxPeople(Integer.parseInt(String.valueOf(tours[7])))
                    .minPeople(Integer.parseInt(String.valueOf(tours[8])))
                    .status(String.valueOf(tours[9]))
                    .build();
            adminDetailTours.add(adminDetailToursList);
        }

        return AdminDetailPackageDTO.builder()
                .count(resultDetailTourInfo)
                .adminToursList(adminDetailTours)
                .build();
    }


    /**
     * 패키지 관리(패키지관리) 수정
     */
    public void updateDetailTour(int adminId, int tourId, String title, Integer duration, Integer price, Integer maxPeople, Integer minPeople) {
        checkAdmin(adminId); // 관리자 권한 확인
        LocalDate checkDate = LocalDate.now();

        List<Object[]> tourDetailInfo = toursRepository.findTourWithAvgRatingAndGuideLock(tourId, checkDate);

        if (tourDetailInfo.isEmpty()) {
            throw new IllegalStateException("투어 정보를 찾을 수 없습니다.");
        }

        if (maxPeople != null && minPeople != null && minPeople > maxPeople) {
            throw new IllegalArgumentException("최대인원을 넘어설수없습니다. 최소인원 업데이트할 경우");
        }


        Object[] tourDetails = tourDetailInfo.get(0);
        String reservationStatus = (String) tourDetails[8];

        if ("예약불가".equals(reservationStatus)) {
            throw new IllegalStateException("이 투어는 현재 예약 불가라 수정 할 수 없습니다.");
        }

        if (title != null) {
            toursRepository.updateTourTitle(tourId, title);
        }
        if (duration != null) {
            toursRepository.updateTourDuration(tourId, duration);
        }
        if (price != null) {
            toursRepository.updateTourPrice(tourId, price);
        }
        if (maxPeople != null) {
            toursRepository.updateTourMaxGroupSize(tourId, maxPeople);
        }
        if (minPeople != null) {
            toursRepository.updateTourMinGroupSize(tourId, minPeople);
        }

    }

    /**
     * 패키지 관리(패키지관리)
     * 상세보기 (이미지 포함)
     */
    public AdminDetailImgPackageDTO getToursDetailImg(int adminId, Integer tourId) {
        checkAdmin(adminId);
        LocalDate checkDate = LocalDate.now(); // 현재 날짜 or 필요한 날짜를 변수로 설정
        List<Object[]> tempTourDetailInfo = toursRepository.findTourImgWithAvgRatingAndGuideLock(tourId);
        Object[] tourDetailInfo = tempTourDetailInfo.get(0);

        log.info(String.valueOf(tourDetailInfo[0]));

        AdminDetailImgPackageDTO adminToursImgDetailList = AdminDetailImgPackageDTO.builder()
                .tourId(Integer.parseInt(String.valueOf(tourDetailInfo[0])))
                .tourName(String.valueOf(tourDetailInfo[1]))
                .guideId(Integer.parseInt(String.valueOf(tourDetailInfo[2])))
                .tourSeller(String.valueOf(tourDetailInfo[3]))
                .rating((tourDetailInfo[4] != null) ? Float.parseFloat(String.valueOf(tourDetailInfo[4])) : 0)
                .duration(Integer.parseInt(String.valueOf(tourDetailInfo[5])))
                .price(Integer.parseInt(String.valueOf(tourDetailInfo[6])))
                .maxPeople(Integer.parseInt(String.valueOf(tourDetailInfo[7])))
                .minPeople(Integer.parseInt(String.valueOf(tourDetailInfo[8])))
                .status(String.valueOf(tourDetailInfo[9]))
                .build();

        if (String.valueOf(tourDetailInfo[8]) != null) {
            adminToursImgDetailList.setProfile(domain + port + "/img/tours/" + String.valueOf(tourDetailInfo[10]));
            adminToursImgDetailList.setCreatedDay(String.valueOf(tourDetailInfo[11]));
        }

        return adminToursImgDetailList;
    }
}

