package com.lattels.smalltour.controller.admin;

import com.lattels.smalltour.dto.SettingDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.service.admin.SettingService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/setting")
@Api(tags = "Setting Controller", description = "설정 컨트롤러")
public class SettingController {

    private final SettingService settingService;

    /*
     * 설정하기
     */
    @PostMapping(value = "/set-up")
    @ApiOperation(value = "설정 추가")
    public ResponseEntity<Object> setUp(@ApiIgnore Authentication authentication, @RequestBody SettingDTO settingDTO) {

        try {
            settingService.setUp(authentication, settingDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }

    /*
     * 설정 불러오기
     */
    @PostMapping(value = "/get")
    @ApiOperation(value = "설정 불러오기")
    public ResponseEntity<SettingDTO> getSetting(@ApiIgnore Authentication authentication) {

        try {
            SettingDTO responseDTO = settingService.getSetting(authentication);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }


}
