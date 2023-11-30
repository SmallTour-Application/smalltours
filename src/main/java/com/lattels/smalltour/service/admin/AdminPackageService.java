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

            AdminPackageDTO.AdminToursList adminToursList = AdminPackageDTO.AdminToursList.builder()
                    .tourId(Integer.parseInt(String.valueOf(tours[0])))
                    .tourName(String.valueOf(tours[1]))
                    .createdDay(((Timestamp) tours[2]).toLocalDateTime())
                    .updateDay(((Timestamp) tours[3]).toLocalDateTime())
                    .approval(approvalStatus)
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
        Page<Object[]> tourDetailInfo = toursRepository.findTourWithAvgRatingAndGuideLock(tourId, checkDate, pageable);

        long resultDetailTourInfo = tourDetailInfo.getTotalElements();
        List<AdminDetailPackageDTO.AdminToursDetailList> adminDetailTours = new ArrayList<>();

        for (Object[] tours : tourDetailInfo) {
            AdminDetailPackageDTO.AdminToursDetailList adminDetailToursList = AdminDetailPackageDTO.AdminToursDetailList.builder()
                    .tourId(Integer.parseInt(String.valueOf(tours[0])))
                    .tourName(String.valueOf(tours[1]))
                    .tourSeller(String.valueOf(tours[2]))
                    .rating(Float.parseFloat(String.valueOf(tours[3])))
                    .duration(Integer.parseInt(String.valueOf(tours[4])))
                    .price(Integer.parseInt(String.valueOf(tours[5])))
                    .people(Integer.parseInt(String.valueOf(tours[6])))
                    .status(String.valueOf(tours[7]))
                    .build();
            adminDetailTours.add(adminDetailToursList);
        }

        return AdminDetailPackageDTO.builder()
                .count(resultDetailTourInfo)
                .adminToursList(adminDetailTours)
                .build();
    }


    /**
     *패키지 관리(패키지관리) 수정
     */
    public void updateDetailTour(int adminId, int tourId, String title, Integer duration, Integer price,Integer maxPeople,int page, int count) {
        checkAdmin(adminId); // 관리자 권한 확인
        LocalDate checkDate = LocalDate.now();
        Pageable pageable = PageRequest.of(page, count);
        Page<Object[]> tourDetailInfo = toursRepository.findTourWithAvgRatingAndGuideLock(tourId, checkDate, pageable);

        if (tourDetailInfo.hasContent()) {
            Object[] tourDetails = tourDetailInfo.getContent().get(0);
            String reservationStatus = (String) tourDetails[7];

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
            if (title != null && duration != null && price != null && maxPeople != null) {
                // 업데이트 메서드 호출
                toursRepository.updateTourDetails(tourId, title, duration, price, maxPeople);
            }
        } else {
            throw new IllegalStateException("해당 ID를 가진 투어가 존재하지 않습니다.");
        }
    }

    /**
     * 패키지 관리(패키지관리)
     * 상세보기 (이미지 포함)
     */
    public AdminDetailImgPackageDTO getToursDetailImg(int adminId, Integer tourId, int page, int count) {
        checkAdmin(adminId);
        Pageable pageable = PageRequest.of(page, count);
        LocalDate checkDate = LocalDate.now(); // 현재 날짜 or 필요한 날짜를 변수로 설정
        Page<Object[]> tourDetailInfo = toursRepository.findTourImgWithAvgRatingAndGuideLock(tourId, checkDate, pageable);

        List<AdminDetailImgPackageDTO.AdminToursImgDetailList> adminDetailImgTours = new ArrayList<>();

        for (Object[] tours : tourDetailInfo) {
            AdminDetailImgPackageDTO.AdminToursImgDetailList adminDetailToursList = AdminDetailImgPackageDTO.AdminToursImgDetailList.builder()
                    .tourId(Integer.parseInt(String.valueOf(tours[0])))
                    .tourName(String.valueOf(tours[1]))
                    .tourSeller(String.valueOf(tours[2]))
                    .rating(Float.parseFloat(String.valueOf(tours[3])))
                    .duration(Integer.parseInt(String.valueOf(tours[4])))
                    .price(Integer.parseInt(String.valueOf(tours[5])))
                    .people(Integer.parseInt(String.valueOf(tours[6])))
                    .status(String.valueOf(tours[7]))
                    .build();

            if(String.valueOf(tours[8]) != null){
                adminDetailToursList.setProfile(domain + port + "/img/tours/" + String.valueOf(tours[8]));
            }

            adminDetailImgTours.add(adminDetailToursList);
        }

        return AdminDetailImgPackageDTO.builder()
                .adminToursImg(adminDetailImgTours)
                .build();
    }

}