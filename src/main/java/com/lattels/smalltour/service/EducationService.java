package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.EducationDTO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EducationService {

    private final MemberRepository memberRepository;

    private final EducationRepository educationRepository;

    private final EducationLogRepository educationLogRepository;

    @Value("${file.path.education}")
    private String educationFilePath;

    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;

    public File getEducationDirectoryPath() {
        File file = new File(educationFilePath);
        file.mkdirs();

        return file;
    }

    /*
    * 교육 리스트 불러오기
    */
    public List<EducationDTO.ListResponseDTO> getEducationList(int memberId, Pageable pageable) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        Page<Education> educationPage = educationRepository.findAllByGuideJoinDay(memberId, pageable);
        List<Education> educationList = educationPage.getContent();
        List<EducationDTO.ListResponseDTO> listResponseDTOList = new ArrayList<>();

        for (Education education : educationList) {
            EducationDTO.ListResponseDTO listResponseDTO = new EducationDTO.ListResponseDTO(education);
            // educationLog 가져오기
            EducationLog educationLog = educationLogRepository.findByEducationIdAndGuideId(education.getId(), memberId);
            // 비어있다면 3(시청 전)
            if (educationLog == null) {
                listResponseDTO.setState(EducationLogDTO.EducationLogState.DO_NOT);
            }
            else {
                listResponseDTO.setState(educationLog.getState());
            }

            listResponseDTOList.add(listResponseDTO);
        }

        return listResponseDTOList;

    }

    /*
    * 교육 정보 불러오기
    */
    public EducationDTO.ViewResponseDTO getEducationInfo(int memberId, EducationDTO.IdRequestDTO idRequestDTO) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 등록된 교육인지 검사
        Education education = educationRepository.findById(idRequestDTO.getId());
        Preconditions.checkNotNull(education, "등록된 교육이 아닙니다. (교육 ID : %s)", idRequestDTO.getId());

        // 반환할 리스트에 education 정보 저장
        EducationDTO.ViewResponseDTO viewResponseDTO = new EducationDTO.ViewResponseDTO(education);
        viewResponseDTO.setVideoPath(domain + port + "/img/education/" + education.getVideoPath());

        // educationLog 가져오기
        EducationLog educationLog = educationLogRepository.findByEducationIdAndGuideId(education.getId(), memberId);
        EducationLogDTO.InfoResponseDTO educationLogDTO;
        // 비어있다면 null 저장
        if (educationLog == null) {
            educationLogDTO = null;
        }
        else {
            educationLogDTO = new EducationLogDTO.InfoResponseDTO(educationLog);
        }

        // 반환할 DTO에 eduactionLogDTO 저장
        viewResponseDTO.setEducationLogDTO(educationLogDTO);


        return viewResponseDTO;

    }
}
