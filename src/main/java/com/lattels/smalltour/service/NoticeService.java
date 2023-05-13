package com.lattels.smalltour.service;

import com.lattels.smalltour.dto.NoticeDTO;
import com.lattels.smalltour.model.Notice;
import com.lattels.smalltour.persistence.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 공지 작성
    public void writeNotice(int memberId, NoticeDTO.WriteRequestDTO writeRequestDTO) {

        try {

            Notice notice = Notice.builder()
                    .memberId(memberId)
                    .title(writeRequestDTO.getTitle())
                    .content(writeRequestDTO.getContent())
                    .createdDay(LocalDateTime.now())
                    .view(0)
                    .build();
            // 엔티티 저장
            noticeRepository.save(notice);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("NoticeService.writeNotice() : 에러 발생");
        }

    }

    // 공지 글 수정
    public void updateNotice(int memberId, NoticeDTO.UpdateRequestDTO updateRequestDTO) {

        try {

            // 받아온 BoardDTO의 Id에 맞는 엔티티 가져옴
            Notice notice = noticeRepository.findByIdAndMemberId(updateRequestDTO.getId(), memberId);
            // 수정 값 입력
            notice.setTitle(updateRequestDTO.getTitle());
            notice.setContent(updateRequestDTO.getContent());
            notice.setUpdatedDay(LocalDateTime.now());
            // 저장
            noticeRepository.save(notice);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("NoticeService.updateNotice() : 에러 발생");
        }

    }

    // 공지 글 삭제
    public void deleteNotice(int memberId, NoticeDTO.IdRequestDTO idRequestDTO) {

        try {

            // 받아온 BoardDTO의 Id에 맞는 엔티티 가져옴
            Notice notice = noticeRepository.findByIdAndMemberId(idRequestDTO.getId(), memberId);
            // 삭제
            noticeRepository.delete(notice);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("NoticeService.deleteNotice() : 에러 발생");
        }

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
