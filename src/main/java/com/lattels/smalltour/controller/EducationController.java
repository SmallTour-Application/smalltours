package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.EducationDTO;
import com.lattels.smalltour.service.EducationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/education")
@Api(tags = "Education Controller", description = "가이드 교육 컨트롤러")
public class EducationController {

    private final EducationService educationService;

    /*
     * 교육 리스트 불러오기
     */
    @PostMapping(value = "/list")
    @ApiOperation(value = "교육 리스트 불러오기")
    public ResponseEntity<List<EducationDTO.ListResponseDTO>> getEducationList(@ApiIgnore Authentication authentication, @PageableDefault(size = 10) Pageable pageable) {

        List<EducationDTO.ListResponseDTO> educationDTOList = educationService.getEducationList(Integer.parseInt(authentication.getPrincipal().toString()), pageable);
        return ResponseEntity.ok().body(educationDTOList);

    }

    /*
     * 동영상 정보 불러오기
     */
    @PostMapping(value = "/view")
    @ApiOperation(value = "동영상 정보 불러오기")
    public ResponseEntity<EducationDTO.ViewResponseDTO> getEducationInfo(@ApiIgnore Authentication authentication, EducationDTO.IdRequestDTO idRequestDTO) {

        EducationDTO.ViewResponseDTO viewResponseDTO = educationService.getEducationInfo(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().body(viewResponseDTO);

    }

}
