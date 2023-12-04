package com.lattels.smalltour.service.admin;

import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.SettingDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Setting;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.SettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingService {

    private final MemberRepository memberRepository;

    private final SettingRepository settingRepository;


    /**
     * 세팅하기
     * @param authentication 로그인 정보
     * @param settingDTO 세팅 값 DTO
     */
    public void setUp(Authentication authentication, SettingDTO settingDTO) {

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

        // 설정 값이 없다면
        if (settingDTO.getId() == 0) {
            Setting setting = Setting.builder()
                    .packageCommission(settingDTO.getPackageCommission())
                    .build();
            settingRepository.save(setting);
        }
        // 설정 값이 있다면
        else {
            Setting setting = settingRepository.findById(settingDTO.getId());
            setting.setPackageCommission(settingDTO.getPackageCommission());
            settingRepository.save(setting);
        }

    }


    /**
     * 세팅 정보 불러오기
     * @param authentication 로그인 정보
     * @return 세팅 정보
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

        // setting에 값이 없다면 새로 만들기
        if (settings.size() == 0) {
            Setting setting = Setting.builder()
                    .packageCommission(0)
                    .build();
            settingRepository.save(setting);
            settings = settingRepository.findAll();
        }

        SettingDTO settingDTO = new SettingDTO(settings.get(0));

        return settingDTO;
    }
}
