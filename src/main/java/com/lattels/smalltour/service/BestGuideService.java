package com.lattels.smalltour.service;

import com.lattels.smalltour.dto.BestGuideDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.model.BestGuide;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.BestGuideRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BestGuideService {

    private final MemberRepository memberRepository;

    private final BestGuideRepository bestGuideRepository;

    public List<BestGuideDTO.GuideInfoResponseDTO> getGuideInfo(Authentication authentication) {

        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member member = memberRepository.findByMemberId(memberId);
        // 등록된 회원인지 검사
        if (member == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (member.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }

        // 가이드 정보 가져오기
        List<Member> guideList = memberRepository.findByGuideAll();
        List<BestGuideDTO.GuideInfoResponseDTO> guideInfoResponseDTOList = new ArrayList<>();
        // 가이드 정보 DTO에 담기
        for (Member guide : guideList) {
            BestGuideDTO.GuideInfoResponseDTO guideInfoResponseDTO = new BestGuideDTO.GuideInfoResponseDTO(guide);
            // 우수 가이드 선정 여부 체크
            boolean bestGuide = bestGuideRepository.existsBestGuideByGuideId(guide.getId());
            guideInfoResponseDTO.setBestGuide(bestGuide);
            guideInfoResponseDTOList.add(guideInfoResponseDTO);
        }

        return guideInfoResponseDTOList;

    }

    /*
    * 우수 가이드 추가
    */
    public void addBestGuide(Authentication authentication, MemberDTO.IdRequestDTO idRequestDTO) {

        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member member = memberRepository.findByMemberId(memberId);
        Member guide = memberRepository.findByMemberId(idRequestDTO.getMemberId());
        // 등록된 회원인지 검사
        if (member == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (member.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }
        // 받아온 아이디가 관리자인지 검사
        if (guide.getRole() != MemberDTO.MemberRole.GUIDE) {
            throw new ResponseMessageException(ErrorCode.GUIDE_INVALID_PERMISSION);
        }

        BestGuide bestGuide = BestGuide.builder()
                .guideId(guide.getId())
                .setDay(LocalDate.now())
                .build();
        bestGuideRepository.save(bestGuide);

    }

}
