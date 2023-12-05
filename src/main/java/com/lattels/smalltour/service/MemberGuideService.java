package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.MemberAndGuideProfileDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.model.GuideProfile;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.GuideProfileRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
//가이드 회원가입 페이지
//Member + guideProfile 합쳐서 회원가입 진행함.
public class MemberGuideService {

    private final MemberRepository memberRepository;

    private final GuideProfileRepository guideProfileRepository;

    @Value("${file.path}")
    private String filePath;

    @Value("${file.path.resumePortfolio}")
    private String filePathRP;

    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;

    public File getMemberDirectoryPath() {
        File file = new File(filePath);
        file.mkdirs();

        return file;
    }

    public File getPortResumeDirectoryPath() {
        File file = new File(filePathRP);
        file.mkdirs();

        return file;
    }


    // 프로필 이미지 변경
    public MemberAndGuideProfileDTO.UpdateProfile updateProfileImg(int memberId, MemberAndGuideProfileDTO.UpdateProfile memberGuideDTO) {
        try {

            // 이미지가 없는 경우
            if (memberGuideDTO == null || !memberGuideDTO.checkProfileImgRequestNull()) {
                log.warn("MemberService.updateProfileImg() : 사진이 없습니다.");
                throw new RuntimeException("MemberService.updateProfileImg() : 사진이 없습니다.");
            }

            Member member = memberRepository.findByMemberId(memberId);

            // Member가 null인 경우
            if (member == null) {
                log.warn("MemberGuideService.updateProfileImg() : 회원 정보를 찾을 수 없습니다.");
                throw new RuntimeException("MemberGuideService.updateProfileImg() : 회원 정보를 찾을 수 없습니다.");
            }

            // 기존 이미지 삭제
            if (member.getProfile() != null) {
                String tempPath = filePath;
                File delFile = new File(tempPath);
                // 해당 파일이 존재하는지 한번 더 체크 후 삭제
                if(delFile.isFile()){
                    delFile.delete();
                }
            }

            MultipartFile multipartFile = memberGuideDTO.getProfileImgRequest().get(0);
            String current_date = null;

            if (multipartFile != null && !multipartFile.isEmpty()) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                current_date = now.format(dateTimeFormatter);

                String absolutePath = filePath;
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
                            break;
                        }
                    }

                    String new_file_name = String.valueOf(memberId);
                    member.setProfile(new_file_name + originalFileExtension);
                    file = new File(absolutePath + File.separator + new_file_name + originalFileExtension);
                    multipartFile.transferTo(file);

                    file.setWritable(true);
                    file.setReadable(true);
                    break;
                }
            } else {
                log.warn("MemberGuideService.updateProfileImg() : multipartFile이 null이거나 비어 있습니다");
                throw new RuntimeException("MemberGuideService.updateProfileImg() : multipartFile이 null이거나 비어 있습니다");
            }

            memberRepository.save(member);
            return MemberAndGuideProfileDTO.UpdateProfile.builder().profile(member.getProfile()).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("MemberGuideService.updateContact() : 에러 발생.");
        }
    }
    public MemberAndGuideProfileDTO.UpdateResumePortfolio updateResumeAndPortfolio(int memberId, MemberAndGuideProfileDTO.UpdateResumePortfolio memberAndGuideDTO) throws IOException {
        GuideProfile guideProfile = guideProfileRepository.findByGuideId(memberId);

        // 이력서
        if (memberAndGuideDTO.checkResumeFileRequestNull() && !memberAndGuideDTO.getResumeFile().isEmpty()) {
            MultipartFile multipartFile = memberAndGuideDTO.getResumeFile().get(0);
            String resumePath = processFile(multipartFile, memberId, "resume");
            guideProfile.setResume(resumePath);
        }
        // 포트폴리오
        if (memberAndGuideDTO.checkPortfolioFileRequestNull() && !memberAndGuideDTO.getPortfolioFile().isEmpty()) {
            MultipartFile multipartFile = memberAndGuideDTO.getPortfolioFile().get(0);
            String portfolioPath = processFile(multipartFile, memberId, "portfolio");
            guideProfile.setPortfolioPath(portfolioPath);
        }
        guideProfileRepository.save(guideProfile);
        return MemberAndGuideProfileDTO.UpdateResumePortfolio.builder().resume(guideProfile.getResume()).portfolioPath(guideProfile.getPortfolioPath()).build();
    }


    public MemberAndGuideProfileDTO viewProfile(MemberAndGuideProfileDTO memberAndGuideProfileDTO){


        Optional<Member> memberOpt = memberRepository.findById(memberAndGuideProfileDTO.getId());
        GuideProfile guideProfileOpt = guideProfileRepository.findByGuideId(memberAndGuideProfileDTO.getId());

        if (!memberOpt.isPresent()) {
            return null;
        }

        Member member = memberOpt.get();
        GuideProfile guideProfile = guideProfileOpt;

        return MemberAndGuideProfileDTO.builder()
                .id(member.getId())
                .password(member.getPassword())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .tel(member.getTel())
                .role(member.getRole())
                .state(member.getState())
                .birthDay(member.getBirthDay())
                .joinDay(member.getJoinDay())
                .gender(member.getGender())
                .profile(domain + port + "/img/guide/member/" + member.getProfile())
                .guideId(guideProfile.getGuideId())
                .resume(domain + port +"/img/guide/portResume/"+ guideProfile.getResume())
                .introduce(guideProfile.getIntroduce())
                .portfolioPath(domain + port + "/img/guide/portResume/" + guideProfile.getPortfolioPath())
                .build();
    }


    //resume + portfolio
    private String processFile(MultipartFile multipartFile, int memberId, String dirName) throws IOException {
        if (!multipartFile.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);

            String absolutePath = filePathRP;
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
}