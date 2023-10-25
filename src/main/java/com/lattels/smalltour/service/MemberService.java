package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.FavoriteTourDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.favoriteGuideDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.model.FavoriteGuide;
import com.lattels.smalltour.model.FavoriteTour;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.FavoriteGuideRepository;
import com.lattels.smalltour.persistence.FavoriteTourRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final FavoriteGuideRepository favoriteGuideRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FavoriteTourRepository favoriteTourRepository;
    private final ToursRepository toursRepository;

    @Value("${file.path}")
    private String filePath;

    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;


    public File getMemberDirectoryPath() {
        File file = new File(filePath);
        file.mkdirs();

        return file;
    }



    //프로필 확인
    public MemberDTO viewProfile(MemberDTO memberDTO){
        try{
            //Optional<Member> member = memberRepository.findById(memberDTO.getId());
            Member member = memberRepository.findByMemberId(memberDTO.getId());

            MemberDTO responseMemberDTO = MemberDTO.builder()
                    .email(member.getEmail())
                    .password(passwordEncoder.encode(member.getPassword()))
                    .name(member.getName())
                    .tel(member.getTel())
                    .nickname(member.getNickname())
                    .birthDay(member.getBirthDay())
                    .joinDay(LocalDateTime.now())
                    .gender(member.getGender())
                    .role(member.getRole())
                    .build();

            //이미지가 있으면
            if (member.getProfile() != null) {
                responseMemberDTO.setProfile(domain + port + "/img/member/"+ member.getProfile());
            }


            return responseMemberDTO;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("MemberService.viewProfile() : 에러 발생.");
        }

    }



    //전화번호 업데이트
    @Transactional
    public String updateTel(final int id, final String chgTel){
        if(id < 1){
            log.warn("MemberService.updateTel() : Id 값이 이상해요");
            throw new RuntimeException("MemberService.updateTel() : Id 값이 이상해요");
        }
        if(chgTel == null || chgTel.equals("")){
            log.warn("MemberService.updateTel() : 전화번호 값을 집어넣으세요");
            throw new RuntimeException("MemberService.updateTel() : 전화번호 값을 집어넣으세요");
        }
        int count = memberRepository.findByTel(chgTel); // 바꾸려는 전화번호가 이미 있는지 확인
        if(count > 0){
            // 이미 같은 전화번호가 있으면
            log.warn("MemberService.updateTel() : 이미 같은 전화번호가 있어요");
            throw new RuntimeException("MemberService.updateTel() : 이미 같은 전화번호가 있어요");
        }

        // 같은 전화번호가 없으면 전화번호 수정
        try{
            final Member member = memberRepository.findByMemberId(id);
            member.setTel(chgTel);
            memberRepository.save(member); // 수정
            // 현재 저장되어 있는 값 가져오기
            final String tel = memberRepository.findTelByMemberId(id);
            return tel;

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("MemberService.updateContact() : 올바른 양식으로 입력해 주세요.");
        }

    }
    //닉네임 변경
    @Transactional
    public String updateNickName(final int id, final String chgNickName){
        if(id < 1){
            log.warn("MemberService.updateNickName() : Id 값이 이상해요");
            throw new RuntimeException("MemberService.updateNickName() : Id 값이 이상해요");
        }
        if(chgNickName == null || chgNickName.equals("")){
            log.warn("MemberService.updateNickName() : 닉네임 값을 집어넣으세요");
            throw new RuntimeException("MemberService.updateNickName() : 닉네임값을 집어넣으세요");
        }
        int count = memberRepository.findByNickname(chgNickName);
        if(count > 0){
            // 이미 같은 닉네임이 있으면
            log.warn("MemberService.updateNickName() : 이미 같은 닉네임이 있어요");
            throw new RuntimeException("MemberService.updateNickName() : 이미 같은 닉네임이 있어요");
        }


        try{
            final Member member = memberRepository.findByMemberId(id);
            member.setNickname(chgNickName);
            memberRepository.save(member); // 수정
            // 현재 저장되어 있는 값 가져오기
            final String NickName = memberRepository.findNickNameByMemberId(id);
            return NickName;

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("MemberService.updateNickName() : 올바른 양식으로 입력해 주세요.");
        }

    }



    // 현재 비밀번호와 변경할 비밀번호 받아서 비밀번호 변경
    @Transactional
    public boolean updatePw(final int id, final String curPw, final String chgPw, PasswordEncoder passwordEncoder){
        if(curPw == null || curPw.equals("") || chgPw == null | chgPw.equals("")){
            log.warn("MemberService.changePw() : 들어온 값이 이상해요");
            throw new RuntimeException("MemberService.changePw() : 들어온 값이 이상해요");
        }
        if(id < 1){
            log.warn("MemberService.changePw() : memberId 값이 이상해요");
            throw new RuntimeException("MemberService.changePw() : memberId 값이 이상해요");
        }
        // 현재 비밀번호가 맞는지 검사
        String originPassword = memberRepository.findPasswordByMemberId(id); //DB에 들어가있는 PW
        if(!passwordEncoder.matches(curPw, originPassword)){
            //비밀번호가 다르면
            log.warn("MemberService.changePw() : 비밀번호가 달라요");
            throw new RuntimeException("MemberService.changePw() : 비밀번호가 달라요");
        }
        //비밀번호가 맞으면 비밀번호 변경
        final Member member = memberRepository.findByMemberId(id);
        member.changePassword(passwordEncoder.encode(chgPw));
        memberRepository.save(member);
        return true;
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



    // 프로필 이미지 변경
    public MemberDTO updateProfileImg(int memberId, MemberDTO.UpdateProfile memberDTO) {

        try {

            // 이미지가 없는 경우
            if (memberDTO == null || !memberDTO.checkProfileImgRequestNull()) {
                log.warn("MemberService.updateProfileImg() : 사진이 없습니다.");
                throw new RuntimeException("MemberService.updateProfileImg() : 사진이 없습니다.");
            }

                Member member = memberRepository.findByMemberId(memberId);

            // Member가 null인 경우
                if (member == null) {
                    log.warn("MemberService.updateProfileImg() : 회원 정보를 찾을 수 없습니다.");
                    throw new RuntimeException("MemberService.updateProfileImg() : 회원 정보를 찾을 수 없습니다.");
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

                MultipartFile multipartFile = memberDTO.getProfileImgRequest().get(0);
                String current_date = null;

                if (multipartFile != null && !multipartFile.isEmpty()) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    current_date = now.format(dateTimeFormatter);

                    // String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
                    String absolutePath =filePath;

                    //String path = "images" + File.separator + current_date;
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
                            } else if (contentType.contains("images/png")) {
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
                }else {
                    log.warn("MemberService.updateProfileImg() : multipartFile이 null이거나 비어 있습니다");
                    throw new RuntimeException("MemberService.updateProfileImg() : multipartFile이 null이거나 비어 있습니다");

            }         memberRepository.save(member);
            return MemberDTO.builder().profile(member.getProfile()).build();
        }   catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("MemberGuideService.updateContact() : 에러 발생.");
        }
    }



    //좋아요 누른 가이드 가져오기
    @Transactional(readOnly = true)
    public List<favoriteGuideDTO> getFavoriteGuides(int memberId, int page, int size) {
        List<favoriteGuideDTO> favoriteGuideDTOList = new ArrayList<>();


        Member member = memberRepository.findByIdAndRole(memberId, 0)
                .orElseThrow(() -> new IllegalArgumentException("회원이 사용자가 아닙니다. 가이드 혹은 관리자 계정입니다."));

        Pageable pageable = PageRequest.of(page - 1, size);


        //현재 페이지의 내용을 목록으로 가져오기
        //전체 100개의 데이터 항목 중에서 3번째 페이지(데이터 항목 21-30)를 가져왔다면,
        // getContent()를 호출하면 그 3번째 페이지에 포함된 데이터 항목 21부터 30까지만 List로 반환
        List<FavoriteGuide> favoriteGuides = favoriteGuideRepository.findByMemberAndGuideRole(member, pageable).getContent();

        for (FavoriteGuide favoriteGuide : favoriteGuides) {
            Member guide = favoriteGuide.getGuide();

            long favoriteCount = favoriteGuideRepository.countByGuideId(guide.getId());
            favoriteGuideDTO guideDTO = favoriteGuideDTO.builder()
                    .guideId(guide.getId())
                    .guideName(guide.getName())
                    .guideImg(guide.getProfile())
                    .favorite((int)favoriteCount) //내가 좋아요 누른 가이드에 좋아요 수(다른사람도 누른)
                    .build();

            favoriteGuideDTOList.add(guideDTO);
        }

        return favoriteGuideDTOList;
    }


    @Transactional(readOnly = true)
    public List<FavoriteTourDTO> getFavoriteTours(int memberId, int page, int size) {
        List<FavoriteTourDTO> favoriteTourDTOList = new ArrayList<>();

        Member member = memberRepository.findByIdAndRole(memberId, 0)
                .orElseThrow(() -> new IllegalArgumentException("회원이 사용자가 아닙니다. 가이드 혹은 관리자 계정입니다."));

        Pageable pageable = PageRequest.of(page - 1, size);


        List<FavoriteTour> favoriteTours = favoriteTourRepository.findByMemberId(member.getId(), pageable).getContent();

        for (FavoriteTour favoriteTour : favoriteTours) {
            Tours tour = toursRepository.findByToursId(favoriteTour.getTourId());
            
            if (tour == null) {
                throw new IllegalArgumentException("해당상품이 없습니다.");
            }

            //상품에 좋아요 갯수 확인
            long countByTourId = favoriteTourRepository.countByTourId(tour.getId());

            //approval:0 미승인 1:승인 2:일시정지 3:삭제
            if(tour.getApprovals() == 1){
                FavoriteTourDTO tourDTO = FavoriteTourDTO.builder()
                        .tourId(tour.getId())
                        .tourName(tour.getTitle())
                        .tourThumb(tour.getThumb())
                        .favorite((int)countByTourId)
                        .build();

                favoriteTourDTOList.add(tourDTO);
            }
        }

        return favoriteTourDTOList;
    }

    /*
    * 권한 업데이트
    */
    public void updateMemberRole(Authentication authentication, MemberDTO.RoleUpdateRequestDTO roleUpdateRequestDTO) {

        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member member = memberRepository.findByMemberId(memberId);
        Member guide = memberRepository.findByMemberId(roleUpdateRequestDTO.getMemberId());
        // 등록된 회원인지 검사
        if (member == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (member.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }
        // 받아온 아이디가 존재하는 유저 아이디인지 검사
        if (guide == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 수정하려는 권한이 이미 할당되어 있다면
        if (guide.getRole() == roleUpdateRequestDTO.getRole()) {
            throw new ResponseMessageException(ErrorCode.ALREADY_SAVED_ROLE);
        }
        guide.setRole(roleUpdateRequestDTO.getRole());
        memberRepository.save(guide);

    }

    /*
    * 이름으로 유저 리스트 가져오기
    */
    public List<MemberDTO.RoleSettingResponseDTO> getMemberList(Authentication authentication, MemberDTO.NameRequestDTO nameRequestDTO) {

        Member member = memberRepository.findByMemberId(Integer.parseInt(authentication.getPrincipal().toString()));

        // 등록된 회원인지 검사
        if (member == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (member.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }

        // 받아온 이름으로 MemberList 가져오기
        List<Member> members = memberRepository.findAllByName(nameRequestDTO.getName());
        // 반환할 DTO List 생성
        List<MemberDTO.RoleSettingResponseDTO> responseDTOList = new ArrayList<>();

        for (Member member1 : members) {
            MemberDTO.RoleSettingResponseDTO responseDTO = new MemberDTO.RoleSettingResponseDTO(member1);
            responseDTOList.add(responseDTO);
        }

        return responseDTOList;

    }
}
