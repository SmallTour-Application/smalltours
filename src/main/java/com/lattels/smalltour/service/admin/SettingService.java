package com.lattels.smalltour.service.admin;

import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.SettingDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Setting;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.SettingRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingService {

    private final MemberRepository memberRepository;

    private final SettingRepository settingRepository;

    /*
    * 설정 추가
    */

    public void addSetting(Authentication authentication, SettingDTO settingDTO) {

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

        List<Setting> settings = settingRepository.findAll();
        if (!settings.isEmpty()) {
            settingDTO.setId(settings.get(0).getId());
            updateSetting(authentication, settingDTO);
        }
        else {

            Setting setting = new Setting();
            if (!(settingDTO.getPackageCommission()).equals("")) {
                setting.setPackageCommission(Double.parseDouble(settingDTO.getPackageCommission()));
            }
            settingRepository.save(setting);
        }

    }

    /*
    * 설정 업데이트
    */
    public SettingDTO updateSetting(Authentication authentication, SettingDTO settingDTO) {

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

        Setting setting = settingRepository.findById(settingDTO.getId());
        if (!(settingDTO.getPackageCommission()).equals("")) {
            setting.setPackageCommission(Double.parseDouble(settingDTO.getPackageCommission()));
        }

        settingRepository.save(setting);
        return new SettingDTO(setting);
    }

    /*
    * 세팅 불러오기
    */
    public SettingDTO getSetting(Authentication authentication) {

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
        List<Setting> settings = settingRepository.findAll();

        SettingDTO settingDTO = new SettingDTO(settings.get(0));


        return settingDTO;
    }
}
