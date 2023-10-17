
package com.lattels.smalltour.service;

import com.lattels.smalltour.model.EmailToken;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {
    private final EmailTokenService emailTokenService;
    private final MemberRepository memberRepository;
    

    @Transactional
    public boolean verifyEmail(String token) throws Exception{
        EmailToken findEmailToken = emailTokenService.findByIdAndExpirationDateAfterAndExpired(token);
        // 성공 시 유저의 인증내용 변경
        Optional<Member> findMember = memberRepository.findById(findEmailToken.getMemberId());
        findEmailToken.setTokenToUsed(); // 사용 완료
        if(findMember.isPresent()){
            Member memberEntity = findMember.get();
            memberEntity.setState(1); // 이메일 인증됐어요
            memberRepository.save(memberEntity); // 엔티티 수정
            return true;
        }else{
            throw new RuntimeException("token error");
        }
    }
}


