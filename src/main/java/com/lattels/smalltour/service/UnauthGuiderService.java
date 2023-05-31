package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.GuideProfileViewDTO;
import com.lattels.smalltour.dto.MemberAndGuideProfileDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.model.GuideProfile;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.GuideProfileRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
//가이드 회원가입 페이지
//Member + guideProfile 합쳐서 회원가입 진행함.
public class UnauthGuiderService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final GuideProfileRepository guideProfileRepository;

    @Value("${file.path}")
    private String filePath;

    @Transactional
    public MemberAndGuideProfileDTO addGuide(MemberAndGuideProfileDTO.SignUp memberAndGuideProfileDTO) throws IOException {
        if(memberAndGuideProfileDTO == null || memberAndGuideProfileDTO.getEmail() == null){
            log.warn("MemberService.addGuide() : memberEntity에 email이 없습니다.");
            throw new RuntimeException("MemberService.addGuide() : member에 email이 없습니다.");
        }

        final String email = memberAndGuideProfileDTO.getEmail();
        if(memberRepository.existsByEmail(email)){
            log.warn("MemberService.addGuide() : 해당 email이 이미 존재합니다.");
            throw new RuntimeException("MemberService.addGuide() : 해당 email이 이미 존재합니다.");
        }

        boolean check = checkNickname(memberAndGuideProfileDTO.getNickname());
        if(!check){
            log.warn("MemberService.addGuide() : 중복되는 닉네임입니다.");
            throw new RuntimeException("MemberService.addGuide() : 중복되는 닉네임입니다.");
        }

        try {
            Member member = Member.builder()
                    .email(memberAndGuideProfileDTO.getEmail())
                    .password(passwordEncoder.encode(memberAndGuideProfileDTO.getPassword()))
                    .name(memberAndGuideProfileDTO.getName())
                    .tel(memberAndGuideProfileDTO.getTel())
                    .nickname(memberAndGuideProfileDTO.getNickname())
                    .joinDay(LocalDateTime.now())
                    .birthDay(memberAndGuideProfileDTO.getBirthDay())
                    .gender(memberAndGuideProfileDTO.getGender())
                    .role(2) // role = 2 (가이드) 고정
                    .build();

            member = memberRepository.save(member);
            int memberId = member.getId();

            // 회원가입시 profile부분
            if (memberAndGuideProfileDTO.checkProfileImgRequestNull() && !memberAndGuideProfileDTO.getProfileImgRequest().isEmpty()) {
                MultipartFile multipartFile = memberAndGuideProfileDTO.getProfileImgRequest().get(0);
                profileImage(multipartFile, memberId, "member");
            }

            // guideProfile에 resume부분
            if (memberAndGuideProfileDTO.checkResumeFileRequestNull() && !memberAndGuideProfileDTO.getResumeFile().isEmpty()) {
                MultipartFile multipartFile = memberAndGuideProfileDTO.getResumeFile().get(0);
                String resumePath = processFile(multipartFile, memberId, "resume");
                memberAndGuideProfileDTO.setResume(resumePath);
            }

            // guideProfile에 portfolio부분
            if (memberAndGuideProfileDTO.checkPortfolioFileRequestNull() && !memberAndGuideProfileDTO.getPortfolioFile().isEmpty()) {
                MultipartFile multipartFile = memberAndGuideProfileDTO.getPortfolioFile().get(0);
                String portfolioPath = processFile(multipartFile, memberId, "portfolio");
                memberAndGuideProfileDTO.setPortfolioPath(portfolioPath);
            }

            // 가이드 프로필 생성
            GuideProfile guideProfile = GuideProfile.builder()
                    .guideId(member.getId())
                    .resume(memberAndGuideProfileDTO.getResume())
                    .introduce(memberAndGuideProfileDTO.getIntroduce())
                    .portfolioPath(memberAndGuideProfileDTO.getPortfolioPath())
                    .build();

            guideProfileRepository.save(guideProfile);

            MemberAndGuideProfileDTO memAndGuideDTO = MemberAndGuideProfileDTO.builder()
                    .id(memberId)
                    .email(member.getEmail())
                    .password(passwordEncoder.encode(member.getPassword()))
                    .name(member.getName())
                    .tel(member.getTel())
                    .nickname(member.getNickname())
                    .joinDay(LocalDateTime.now()) // 현재 시간
                    .birthDay(member.getBirthDay())
                    .gender(member.getGender())
                    .profile(member.getProfile())
                    .resume(guideProfile.getResume())
                    .introduce(guideProfile.getIntroduce())
                    .portfolioPath(guideProfile.getPortfolioPath())
                    .build();

            return memAndGuideDTO;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("MemberService.addGuide() : 에러 발생.");
        }
    }

    //resume 랑 portfolio_path업로드부분
    //이력서랑 포폴이니까 얘는 .pdf로 받게함
    private String processFile(MultipartFile multipartFile, int memberId, String dirName) throws IOException {
        if (!multipartFile.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);

            String absolutePath = "C:" + File.separator + "smallTour" + File.separator + dirName;
            String path = absolutePath;
            File file = new File(path);

            if (!file.exists()) {
                boolean wasSuccessful = file.mkdirs();
                if (!wasSuccessful) {
                    log.warn("file : was not successful");
                }
            }
            String originalFileExtension;
            String contentType = multipartFile.getContentType();
            if (!ObjectUtils.isEmpty(contentType)) {
                if (contentType.contains("application/pdf")) {
                    originalFileExtension = ".pdf";
                } else {
                    log.warn("MemberService.addGuide() : 지원하지 않는 파일 형식입니다.");
                    return null;
                }
            } else {
                return null;
            }

            String new_file_name = String.valueOf(memberId);
            String new_file_path = absolutePath + File.separator + new_file_name + originalFileExtension;
            file = new File(new_file_path);
            multipartFile.transferTo(file);
            file.setWritable(true);
            file.setReadable(true);
            return new_file_path;
        }
        return null;
    }

    //Member 에 Profile
    private void profileImage(MultipartFile multipartFile, int memberId, String dirName) throws IOException {
        if (!multipartFile.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);

            String absolutePath = "C:" + File.separator + "smallTour" + File.separator + dirName;
            String path = absolutePath;
            File file = new File(path);

            if (!file.exists()) {
                boolean wasSuccessful = file.mkdirs();
                if (!wasSuccessful) {
                    log.warn("file : was not successful");
                }
            }
            while (true) {
                String originalFileExtension;
                String contentType = multipartFile.getContentType();

                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                } else {
                    if (contentType.contains("image/jpeg")) {
                        originalFileExtension = ".jpg";
                    } else if (contentType.contains("image/png")) {
                        originalFileExtension = ".png";
                    } else {
                        log.warn("MemberService.addGuide() : 지원하지 않는 이미지 형식입니다.");
                        break;
                    }
                }

                String new_file_name = String.valueOf(memberId);
                Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("멤버가없어요"));
                member.setProfile(new_file_name + originalFileExtension);
                memberRepository.save(member);

                file = new File(absolutePath + File.separator + new_file_name + originalFileExtension);
                multipartFile.transferTo(file);

                file.setWritable(true);
                file.setReadable(true);
                break;
            }
        }
    }

    //닉네임 중복 체크
    private boolean checkNickname(final String nickname) {
        if(nickname == null || nickname.equals("")){
            log.warn("MemberService.checkNickname() : nickname 값이 이상해요");
            throw new RuntimeException("MemberService.checkNickname() : nickname 값이 이상해요");
        }

        int count = memberRepository.findByNickname(nickname);
        if(count > 0){
            return false;
        }
        return true;
    }

    // 회원의 state(이메일인증확인) 0인지 1인지
    //1이어야 모든 사이트 서비스 이용가능(게시판,리뷰,결제,강의듣기 등등)
    private void checkMemberState(int memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        if (member.getState() != 1) {
            throw new RuntimeException("이메일 인증을 해주셔야 합니다.");
        }
    }
}