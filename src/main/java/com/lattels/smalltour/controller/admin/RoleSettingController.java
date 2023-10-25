package com.lattels.smalltour.controller.admin;

import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.service.MemberService;
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

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/setting/role")
@Api(tags = "Role Setting Controller", description = "권한 설정 컨트롤러")
public class RoleSettingController {

    private final MemberService memberService;

    /*
     * 권한 업데이트
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "권한 업데이트")
    public ResponseEntity<Object> updateMemberRole(@ApiIgnore Authentication authentication, @RequestBody @Valid MemberDTO.RoleUpdateRequestDTO roleUpdateRequestDTO) {

        try {
            memberService.updateMemberRole(authentication, roleUpdateRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }

    /*
     * 이름으로 유저 리스트 가져오기
     */
    @PostMapping(value = "/user-list")
    @ApiOperation(value = "이름으로 유저 리스트 가져오기")
    public ResponseEntity<List<MemberDTO.RoleSettingResponseDTO>> getMemberList(@ApiIgnore Authentication authentication, @RequestBody @Valid MemberDTO.NameRequestDTO nameRequestDTO) {

        try {
            List<MemberDTO.RoleSettingResponseDTO> responseDTOList =  memberService.getMemberList(authentication, nameRequestDTO);
            return ResponseEntity.ok().body(responseDTOList);
        } catch (Exception e) {
            throw new ResponseMessageException(ErrorCode.INVALID_PARAMETER);
        }

    }

}
