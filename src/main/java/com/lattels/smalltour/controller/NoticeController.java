package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.NoticeDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@Api(tags = "Notice Controller", description = "공지 컨트롤러")
public class NoticeController {

    private final NoticeService noticeService;

    // 글 작성
    @PostMapping(value = "/write")
    @ApiOperation(value = "공지 글 작성")
    public ResponseEntity<Object> writeNotice(@ApiIgnore Authentication authentication, @Valid @RequestBody NoticeDTO.WriteRequestDTO writeRequestDTO) {

        try {
            noticeService.writeNotice(authentication, writeRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }

    // 글 수정
    @PostMapping(value = "/update")
    @ApiOperation(value = "공지 글 수정")
    public ResponseEntity<Object> updateNotice(@ApiIgnore Authentication authentication, @Valid @RequestBody NoticeDTO.UpdateRequestDTO updateRequestDTO) {

        try {
            noticeService.updateNotice(authentication, updateRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }

    // 글 삭제
    @PostMapping(value = "/delete")
    @ApiOperation(value = "공지 글 삭제")
    public ResponseEntity<Object> deleteNotice(@ApiIgnore Authentication authentication, @Valid @RequestBody NoticeDTO.IdRequestDTO idRequestDTO) {

        try {
            noticeService.deleteNotice(authentication, idRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }

    // 글 목록
    @GetMapping(value = "/unauth/list")
    @ApiOperation(value = "공지 글 목록 가져오기")
    public ResponseEntity<List<NoticeDTO.ListResponseDTO>> getNoticeList(@PageableDefault(size = 10) Pageable pageable) {

        try {
            List<NoticeDTO.ListResponseDTO> listResponseDTOList = noticeService.getNoticeList(pageable);
            return ResponseEntity.ok().body(listResponseDTOList);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }


    // 글 보기
    @GetMapping(value = "/unauth/view")
    @ApiOperation(value = "공지 글 보기")
    public ResponseEntity<NoticeDTO.ViewResponseDTO> viewNotice(@Valid NoticeDTO.IdRequestDTO idRequestDTO) {

        try {
            NoticeDTO.ViewResponseDTO viewResponseDTO= noticeService.viewNotice(idRequestDTO);
            return ResponseEntity.ok().body(viewResponseDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }
}
