package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.EducationLogDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.model.Education;
import com.lattels.smalltour.model.EducationLog;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.EducationLogRepository;
import com.lattels.smalltour.persistence.EducationRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EducationLogService {

    private final MemberRepository memberRepository;

    private final EducationRepository educationRepository;

    private final EducationLogRepository educationLogRepository;


    /*
    * 비디오 시청 결과
    */
    public void saveViewedResult(int memberId, EducationLogDTO.ViewedResultRequestDTO viewedResultRequestDTO) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 등록된 교육인지 검사
        Education education = educationRepository.findById(viewedResultRequestDTO.getEducationId());
        Preconditions.checkNotNull(education, "등록된 교육이 아닙니다. (교육 ID : %s)", viewedResultRequestDTO.getEducationId());

        // 시청 기록이 없다면
        if (educationLogRepository.findByEducationIdAndGuideId(education.getId(), memberId) == null) {
            EducationLog educationLog = EducationLog.builder()
                    .educationId(education.getId())
                    .guideId(memberId)
                    .lastView(viewedResultRequestDTO.getLastView())
                    .state(setState(viewedResultRequestDTO, education))
                    .build();
            // 수강 완료했거나 기한을 넘겼다면 완료일 저장
            if (educationLog.getState() >= EducationLogDTO.EducationLogState.COMPLETED) {
                educationLog.setCompletedDate(LocalDate.now());
            }

            educationLogRepository.save(educationLog);
        }
        // 기록이 있다면
        else {
            EducationLog educationLog = educationLogRepository.findByEducationIdAndGuideId(education.getId(), memberId);
            educationLog.setLastView(viewedResultRequestDTO.getLastView());
            // state가 시청 중이었다면
            if (educationLog.getState() == EducationLogDTO.EducationLogState.IN_TRAINING) {
                educationLog.setState(setState(viewedResultRequestDTO, education));
            }
            if (educationLog.getState() > EducationLogDTO.EducationLogState.IN_TRAINING) {
                educationLog.setCompletedDate(LocalDate.now());
            }

            educationLogRepository.save(educationLog);
        }

    }

    // state 계산
    public int setState(EducationLogDTO.ViewedResultRequestDTO dto, Education education) {

        // 수강 완료했다면
        if (dto.getLastView().equals(education.getPlayTime())) {
            // 기한을 넘겼으면
            if (LocalDate.now().isAfter(education.getEndDay())) {
                return EducationLogDTO.EducationLogState.LATE;
            }
            // 넘기지 않았다면
            return EducationLogDTO.EducationLogState.COMPLETED;
        }
        // 완료하지 않앗다면
        else {
            return EducationLogDTO.EducationLogState.IN_TRAINING;
        }

    }

}
