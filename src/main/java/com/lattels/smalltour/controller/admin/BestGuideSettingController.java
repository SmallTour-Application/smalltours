package com.lattels.smalltour.controller.admin;

import com.lattels.smalltour.dto.BestGuideDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.StatsDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.service.BestGuideService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/setting/best-guide")
@Api(tags = "Best Guide Setting Controller", description = "우수 가이드 설정 컨트롤러")
public class BestGuideSettingController {

    private final BestGuideService bestGuideService;

    /*
     * 가이드 정보
     */
    @PostMapping(value = "/info")
    @ApiOperation(value = "가이드 정보")
    public ResponseEntity<List<BestGuideDTO.GuideInfoResponseDTO>> getGuideInfo(@ApiIgnore Authentication authentication) {

        try {
            List<BestGuideDTO.GuideInfoResponseDTO> guideInfoResponseDTOList = bestGuideService.getGuideInfo(authentication);
            return ResponseEntity.ok().body(guideInfoResponseDTOList);
        } catch (Exception e) {
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }

    /*
     * 우수 가이드 추가
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "우수 가이드 추가")
    public ResponseEntity<Object> addBestGuide(@ApiIgnore Authentication authentication, @RequestBody MemberDTO.IdRequestDTO idRequestDTO) {

        try {
            bestGuideService.addBestGuide(authentication, idRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }


}
