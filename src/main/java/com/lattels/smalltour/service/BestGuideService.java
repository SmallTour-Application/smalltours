package com.lattels.smalltour.service;

import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.bestguide.BestGuideCountDTO;
import com.lattels.smalltour.dto.bestguide.BestGuideDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.model.BestGuide;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.BestGuideRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * @param authentication 로그인 정보
     * @param pageable 페이징 정보
     * @return BestGuideCountDTO List
     */
    public List<BestGuideCountDTO> getGuideInfo(Authentication authentication, Pageable pageable) {

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

        Page<BestGuideCountDTO> pages = bestGuideRepository.countPerGuide(pageable);
        List<BestGuideCountDTO> bestGuideCountDTOS =pages.getContent();

        return bestGuideCountDTOS;

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
        // 받아온 아이디가 가이드인지 검사
        if (guide.getRole() != MemberDTO.MemberRole.GUIDE) {
            throw new ResponseMessageException(ErrorCode.GUIDE_INVALID_PERMISSION);
        }

        BestGuide bestGuide = BestGuide.builder()
                .guide(guide)
                .setDay(LocalDate.now())
                .build();
        bestGuideRepository.save(bestGuide);

    }

    /**
     * 우수 가이드 삭제
     * @param authentication 로그인 정보
     * @param idRequestDTO 회원 ID
     */
    public void deleteBestGuide(Authentication authentication, BestGuideDTO.IdRequestDTO idRequestDTO) {

        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member admin = memberRepository.findByMemberId(memberId);
        BestGuide bestGuide = bestGuideRepository.findById(idRequestDTO.getId());
        // 등록된 회원인지 검사
        if (admin == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (admin.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }
        // 받아온 아이디가 유효한지 검사
        if (bestGuide == null) {
            throw new ResponseMessageException(ErrorCode.BEST_GUIDE_NOT_FOUND);
        }

        bestGuideRepository.delete(bestGuide);

    }

    /**
     * 가이드 검색
     * @param authentication  로그인 정보
     * @param searchRequestDTO 가이드 검색 DTO
     * @param pageable 페이징 정보
     * @return 가이드 정보 목록
     */
    public List<BestGuideCountDTO> searchBestGuide(Authentication authentication, BestGuideDTO.SearchRequestDTO searchRequestDTO, Pageable pageable) {

        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member admin = memberRepository.findByMemberId(memberId);
        // 등록된 회원인지 검사
        if (admin == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (admin.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }

        Page<Member> memberPages = null;
        switch (searchRequestDTO.getType()) {
            case 0 : memberPages = memberRepository.findByRole(MemberDTO.MemberRole.GUIDE, pageable);
            break;
            case 1 : memberPages = memberRepository.findByNameContainsAndRole(searchRequestDTO.getWord(), MemberDTO.MemberRole.GUIDE, pageable);
            break;
            case 2 : memberPages = memberRepository.findByNicknameContainsAndRole(searchRequestDTO.getWord(), MemberDTO.MemberRole.GUIDE, pageable);
            break;
            case 3 : memberPages = memberRepository.findByEmailContainsAndRole(searchRequestDTO.getWord(), MemberDTO.MemberRole.GUIDE, pageable);
            break;
            default: throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

        List<Member> members = memberPages.getContent();
        List<BestGuideCountDTO> bestGuideCountDTOS = new ArrayList<>();
        // 가이드 정보 DTO에 담기
        for (Member guide : members) {
            // 우수 가이드 선정 여부 체크
            long cnt = bestGuideRepository.countAllByGuide(guide);
            BestGuideCountDTO bestGuideCountDTO = new BestGuideCountDTO(guide, cnt);
            bestGuideCountDTOS.add(bestGuideCountDTO);
        }

        return bestGuideCountDTOS;
    }
}
