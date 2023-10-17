
package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.admin.member.EmailAllSendMessageDTO;
import com.lattels.smalltour.dto.admin.member.EmailMessageDTO;
import com.lattels.smalltour.model.EmailMessage;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.EmailMessageRepository;

import com.lattels.smalltour.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailMessageService {
    private final EmailSenderService emailSenderService;
    private final EmailMessageRepository emailMessageRepository;
    private final MemberRepository memberRepository;

    @Value("${spring.mail.username}")
    private String from; //프로퍼티에서 mail.username 가져옴

    private void checkAdmin(final int adminId) {
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자를 찾을수없습니다."));
        if (admin.getRole() != 3) {
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }
    }


    /**
     단체 메시지 전송 공통부분
     */
    private void common(int id, EmailAllSendMessageDTO emailAllSendMessageDTO,List<Member> member){
        checkAdmin(id);
        List<EmailMessage> allMember = new ArrayList<>();
        for(Member members : member){
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(members.getEmail());
            mailMessage.setSubject(emailAllSendMessageDTO.getTitle());
            mailMessage.setFrom(from + "@naver.com"); // 이거 해줘야 오류안남,보내는사람
            mailMessage.setText(emailAllSendMessageDTO.getContent());
            emailSenderService.sendEmail(mailMessage);

            EmailMessage emailMessage = EmailMessage.builder()
                    .adminId(id)
                    .content(emailAllSendMessageDTO.getContent())
                    .title(emailAllSendMessageDTO.getTitle())
                    .member(members)
                    .build();
            allMember.add(emailMessage);
        }
        emailMessageRepository.saveAll(allMember);
    }


    /**
     개별 메시지 전송
     */
    public EmailMessageDTO sendEmailMessage(int id,int memberId, EmailMessageDTO emailMessageDTO) throws Exception {
        checkAdmin(id);
        try {
                // 이메일 전송
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(emailMessageDTO.getEmail());
                mailMessage.setSubject(emailMessageDTO.getTitle());
                mailMessage.setFrom(from + "@naver.com"); // 이거 해줘야 오류안남,보내는사람
                mailMessage.setText(emailMessageDTO.getContent());
                emailSenderService.sendEmail(mailMessage);

                Member member = memberRepository.findByReciverMemberId(memberId);
                if (member == null) {
                    throw new IllegalArgumentException("해당 회원 번호는 존재하지 않습니다.");
                }

                if (!member.getEmail().equals(emailMessageDTO.getEmail())) {
                    throw new IllegalArgumentException("해당 이메일은 존재하지 않습니다.");
                }

                if (member.getId() != memberId) {
                    throw new IllegalArgumentException("이메일과 회원 번호가 일치하지 않습니다.");
                }

            EmailMessage emailMessage = new EmailMessage();
                emailMessage.setAdminId(id);
                emailMessage.setTitle(emailMessageDTO.getTitle());
                emailMessage.setContent(emailMessageDTO.getContent());
                emailMessage.setMember(member);

                emailMessageRepository.save(emailMessage);

        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new RuntimeException("메일 전송 실패");
        }

        return emailMessageDTO;
    }


    /**
     단체 메시지 전송(멤버)
     */
    public EmailAllSendMessageDTO sendAllMemberEmailMessage(int id, EmailAllSendMessageDTO emailAllSendMessageDTO){
        try {
            if(emailAllSendMessageDTO.isSendAll()){
                List<Member> member = memberRepository.findByMemberAll();
                common(id,emailAllSendMessageDTO,member);
            }
            else{
                throw new IllegalArgumentException("단체이메일 전송에 문제가 생겼습니다.");
            }

        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new RuntimeException("단체 메일 전송 실패");
        }

        return emailAllSendMessageDTO;
    }

    /**
     단체 메시지 전송(가이드)
     */
    public EmailAllSendMessageDTO sendAllGuideEmailMessage(int id, EmailAllSendMessageDTO emailAllSendMessageDTO){
        try {
            if(emailAllSendMessageDTO.isSendAll()){
                List<Member> member = memberRepository.findByGuideAll();
                common(id,emailAllSendMessageDTO,member);
            }
            else{
                throw new IllegalArgumentException("단체이메일 전송에 문제가 생겼습니다.");
            }

        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new RuntimeException("단체 메일 전송 실패");
        }

        return emailAllSendMessageDTO;
    }


}

