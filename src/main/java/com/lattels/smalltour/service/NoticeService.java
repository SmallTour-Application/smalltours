package com.lattels.smalltour.service;

import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.NoticeDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Notice;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final MemberRepository memberRepository;

    // 공지 작성
    public void writeNotice(Authentication authentication, NoticeDTO.WriteRequestDTO writeRequestDTO) {

        Member member = memberRepository.findByMemberId(Integer.parseInt(authentication.getPrincipal().toString()));

        // 등록된 회원인지 검사
        if (member == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (member.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }

        Notice notice = Notice.builder()
                .memberId(member.getId())
                .title(writeRequestDTO.getTitle())
                .content(writeRequestDTO.getContent())
                .createdDay(LocalDateTime.now())
                .view(0)
                .build();
        // 엔티티 저장
        noticeRepository.save(notice);

    }

    // 공지 글 수정
    public void updateNotice(Authentication authentication, NoticeDTO.UpdateRequestDTO updateRequestDTO) {

        Member member = memberRepository.findByMemberId(Integer.parseInt(authentication.getPrincipal().toString()));

        // 등록된 회원인지 검사
        if (member == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (member.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }

        // 받아온 BoardDTO의 Id에 맞는 엔티티 가져옴
        Notice notice = noticeRepository.findByIdAndMemberId(updateRequestDTO.getId(), member.getId());
        // 수정 값 입력
        notice.setTitle(updateRequestDTO.getTitle());
        notice.setContent(updateRequestDTO.getContent());
        notice.setUpdatedDay(LocalDateTime.now());
        // 저장
        noticeRepository.save(notice);

    }

    // 공지 글 삭제
    public void deleteNotice(Authentication authentication, NoticeDTO.IdRequestDTO idRequestDTO) {

        Member member = memberRepository.findByMemberId(Integer.parseInt(authentication.getPrincipal().toString()));

        // 등록된 회원인지 검사
        if (member == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (member.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }

        // 받아온 BoardDTO의 Id에 맞는 엔티티 가져옴
        Notice notice = noticeRepository.findByIdAndMemberId(idRequestDTO.getId(), member.getId());
        // 삭제
        noticeRepository.delete(notice);

    }

    // 공지 리스트 가져오기
    public List<NoticeDTO.ListResponseDTO> getNoticeList(Pageable pageable) {

        try {

            Page<Object[]> pages = noticeRepository.getList(pageable);
            List<Object[]> objects = pages.getContent();
            List<NoticeDTO.ListResponseDTO> listResponseDTOList = new ArrayList<>();
            for (Object[] object : objects) {
                NoticeDTO.ListResponseDTO listResponseDTO = new NoticeDTO.ListResponseDTO(object);
                listResponseDTOList.add(listResponseDTO);
            }
            return listResponseDTOList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("NoticeService.getNoticeList() : 에러 발생");
        }

    }

    // 공지 보기
    public NoticeDTO.ViewResponseDTO viewNotice(NoticeDTO.IdRequestDTO idRequestDTO) {

        try {

            Notice notice = noticeRepository.findById(idRequestDTO.getId());
            NoticeDTO.ViewResponseDTO viewResponseDTO = new NoticeDTO.ViewResponseDTO(notice);
            return viewResponseDTO;

        } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("NoticeService.viewNotice() : 에러 발생");
    }

    }
}
