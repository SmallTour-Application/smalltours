package com.lattels.smalltour.service.admin;


import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.admin.Traffic.AdminFavoriteGuideCountUpdateDTO;
import com.lattels.smalltour.dto.admin.member.AdminAddMemberDTO;
import com.lattels.smalltour.dto.admin.member.ListMemberDTO;
import com.lattels.smalltour.dto.admin.search.AdminSearchDTO;
import com.lattels.smalltour.dto.search.SearchGuideDTO;
import com.lattels.smalltour.model.KakaoInfo;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FavoriteGuideRepository favoriteGuideRepository;
    private final KakaoInfoRepository kakaoInfoRepository;

    @Value("${file.path}")
    private String filePath;

    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;

    @Value("${file.path.tours.images}")
    private String filePathToursImages;

    // 이름 수정 updateName
    @Transactional
    public String updateName(final int adminId, final int memberId, final String chgName){

        checkAdmin(adminId);

        if(memberId < 1){
            log.warn("adminMemberService.updateName() : Id 값이 이상해요");
            throw new RuntimeException("MemberService.updateName() : Id 값이 이상해요");
        }
        if(chgName == null || chgName.equals("")){
            log.warn("adminMemberService.updateName() : 이름 값을 집어넣으세요");
            throw new RuntimeException("MemberService.updateName() : 이름 값을 집어넣으세요");
        }

        try{
            final Member member = memberRepository.findByMemberId(memberId);
            member.setName(chgName);
            memberRepository.save(member); // 수정
            // 현재 저장되어 있는 값 가져오기
            final String name = memberRepository.findNameByMemberId(memberId);
            return name;

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("adminMemberService.updateName() : 올바른 양식으로 입력해 주세요.");
        }

    }

    // 이메일수정 updateEmail
    @Transactional
    public String updateEmail(final int adminId, final int memberId, final String chgEmail){

        checkAdmin(adminId);

        if(memberId < 1){
            log.warn("adminMemberService.updateEmail() : Id 값이 이상해요");
            throw new RuntimeException("MemberService.updateEmail() : Id 값이 이상해요");
        }
        if(chgEmail == null || chgEmail.equals("")){
            log.warn("adminMemberService.updateEmail() : 이메일 값을 집어넣으세요");
            throw new RuntimeException("MemberService.updateEmail() : 이메일 값을 집어넣으세요");
        }
        int count = memberRepository.countByEmail(chgEmail);
        if(count > 0){
            // 이미 같은 이메일이 있으면
            log.warn("adminMemberService.updateEmail() : 이미 같은 이메일이 있어요");
            throw new RuntimeException("MemberService.updateEmail() : 이미 같은 이메일이 있어요");
        }

        try{
            final Member member = memberRepository.findByMemberId(memberId);
            member.setEmail(chgEmail);
            memberRepository.save(member); // 수정
            // 현재 저장되어 있는 값 가져오기
            String email = memberRepository.findEmailByMemberId(memberId);
            return email;

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("adminMemberService.updateEmail() : 올바른 양식으로 입력해 주세요.");
        }

    }

    public File getMemberDirectoryPath() {
        File file = new File(filePath);
        file.mkdirs();

        return file;
    }

    public File getTourDirectoryPath() {
        File file = new File(filePathToursImages);
        file.mkdirs();

        return file;
    }

    /**
     * 관리자 인지 체크
     */
    private void checkAdmin(final int adminId){
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자를 찾을수없습니다."));
        if(admin.getRole() != 3){
            throw new RuntimeException("관리자만 접근 가능합니다.");
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

    /**
     *해당 회원 상세정보 불러오기
     */
    public ListMemberDTO getInfoMembers(int adminId,int memberId) {
        try {
           checkAdmin(adminId);

            Member member = memberRepository.findByMemberInfoId(memberId);
            Member bestGuide = memberRepository.findByMemberDetailInfoId(memberId);
            Optional<KakaoInfo> kakaoInfo = kakaoInfoRepository.findByMemberId(member.getId());
            String kakaoConnectStatus = kakaoInfo.isPresent() ? "카카오 계정이랑 연동" : "연동안됨";

            if (member == null) {
                throw new RuntimeException("해당 멤버를 찾을 수 없습니다.");
            }

            String guideStatus = "";
            if(member.getRole() == 2){
                guideStatus = (bestGuide != null) ? "우수가이드" : "일반가이드";
            }else if(member.getRole() == 0){
                guideStatus = "일반 회원";
            }else{
                guideStatus = "미등록 가이드";
            }

            ListMemberDTO listMemberDTO = ListMemberDTO.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .password(passwordEncoder.encode(member.getPassword()))
                    .name(member.getName())
                    .nickname(member.getNickname())
                    .tel(member.getTel())
                    .birthDay(member.getBirthDay())
                    .joinDay(member.getJoinDay())
                    .gender(member.getGender())
                    .state(member.getState())
                    .role(member.getRole())
                    .bestGuide(guideStatus)
                    .kakaoConnect(kakaoConnectStatus)
                    .build();

            if(member.getProfile() != null){
                listMemberDTO.setProfile(domain + port + "/img/member/"+ member.getProfile());
            }

            return listMemberDTO;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("관리자 번호 이거나 해당 멤버가 없습니다.");
        }

    }


    /**
     * 회원목록
     */
   public List<ListMemberDTO> getListMembers(int adminId) {
        try {
            checkAdmin(adminId);

            List<Member> member = memberRepository.findByMembersId();
            List<ListMemberDTO> listMember = new ArrayList<>();

            for (Member members : member){

                Member bestGuide = memberRepository.findByMemberDetailInfoId(members.getId());
                Optional<KakaoInfo> kakaoInfo = kakaoInfoRepository.findByMemberId(members.getId());
                String kakaoConnectStatus = kakaoInfo.isPresent() ? "카카오 계정이랑 연동" : "연동안됨";



                if (members == null) {
                    throw new RuntimeException("해당 멤버를 찾을 수 없습니다.");
                }

                String guideStatus = "";
                if(members.getRole() == 2){
                    guideStatus = (bestGuide != null) ? "우수가이드" : "일반가이드";
                }else if(members.getRole() == 0){
                    guideStatus = "일반 회원";
                }else{
                    guideStatus = "미등록 가이드";
                }

                ListMemberDTO listMemberDTO = ListMemberDTO.builder()
                        .id(members.getId())
                        .email(members.getEmail())
                        .password(passwordEncoder.encode(members.getPassword()))
                        .name(members.getName())
                        .nickname(members.getNickname())
                        .tel(members.getTel())
                        .birthDay(members.getBirthDay())
                        .joinDay(members.getJoinDay())
                        .gender(members.getGender())
                        .role(members.getRole())
                        .bestGuide(guideStatus)
                        .kakaoConnect(kakaoConnectStatus)
                        .build();

                if(members.getProfile() != null){
                    listMemberDTO.setProfile(domain + port + "/img/member/"+ members.getProfile());
                }

                listMember.add(listMemberDTO);

            }

            return listMember;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }

    }

    /**
     *전화번호 업데이트
     */
    @Transactional
    public String updateTel(final int adminId, final int memberid, final String chgTel){

        checkAdmin(adminId);

        if(memberid < 1){
            log.warn("adminMemberService.updateTel() : Id 값이 이상해요");
            throw new RuntimeException("MemberService.updateTel() : Id 값이 이상해요");
        }
        if(chgTel == null || chgTel.equals("")){
            log.warn("adminMemberService.updateTel() : 전화번호 값을 집어넣으세요");
            throw new RuntimeException("MemberService.updateTel() : 전화번호 값을 집어넣으세요");
        }
        int count = memberRepository.findByTel(chgTel); // 바꾸려는 전화번호가 이미 있는지 확인
        if(count > 0){
            // 이미 같은 전화번호가 있으면
            log.warn("adminMemberService.updateTel() : 이미 같은 전화번호가 있어요");
            throw new RuntimeException("MemberService.updateTel() : 이미 같은 전화번호가 있어요");
        }

        // 같은 전화번호가 없으면 전화번호 수정
        try{
            final Member member = memberRepository.findByMemberId(memberid);
            member.setTel(chgTel);
            memberRepository.save(member); // 수정
            // 현재 저장되어 있는 값 가져오기
            final String tel = memberRepository.findTelByMemberId(memberid);
            return tel;

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("adminMemberService.updateContact() : 올바른 양식으로 입력해 주세요.");
        }

    }

    /**
     *닉네임 변경
     */
    @Transactional
    public String updateNickName(final int adminId, final int memberId, final String chgNickName){

        checkAdmin(adminId);

        if(memberId < 1){
            log.warn("adminMemberService.updateNickName() : Id 값이 이상해요");
            throw new RuntimeException("MemberService.updateNickName() : Id 값이 이상해요");
        }
        if(chgNickName == null || chgNickName.equals("")){
            log.warn("adminMemberService.updateNickName() : 닉네임 값을 집어넣으세요");
            throw new RuntimeException("MemberService.updateNickName() : 닉네임값을 집어넣으세요");
        }
        int count = memberRepository.findByNickname(chgNickName);
        if(count > 0){
            // 이미 같은 닉네임이 있으면
            log.warn("adminMemberService.updateNickName() : 이미 같은 닉네임이 있어요");
            throw new RuntimeException("MemberService.updateNickName() : 이미 같은 닉네임이 있어요");
        }

        try{
            final Member member = memberRepository.findByMemberId(memberId);
            member.setNickname(chgNickName);
            memberRepository.save(member); // 수정
            // 현재 저장되어 있는 값 가져오기
            final String NickName = memberRepository.findNickNameByMemberId(memberId);
            return NickName;

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("adminMemberService.updateNickName() : 올바른 양식으로 입력해 주세요.");
        }

    }

    /**
     *비밀번호 변경(관리자라 기존 비밀번호 입력해서 수정권한 획득할 필요 없음)
     */
    @Transactional
    public String updatePw(final int adminId,final int memberId,final String chgPw, PasswordEncoder passwordEncoder){
        checkAdmin(adminId);

        //패스워드 초기화
        final Member members = memberRepository.findByMemberId(memberId);
        members.changePassword(passwordEncoder.encode(chgPw));
        memberRepository.save(members);

        // 현재 저장되어 있는 값 가져오기
        final String password = memberRepository.findPasswordByMemberId(memberId);
        return password;
    }

    /**
     *프로필 이미지 수정
     */
    public ListMemberDTO updateProfileImg(final int adminId, final int memberId, ListMemberDTO.UpdateProfile memberDTO) {
        checkAdmin(adminId);

        try {
            // 이미지가 없는 경우
            if (memberDTO == null || !memberDTO.checkProfileImgRequestNull()) {
                log.warn("AdminMemberService.updateProfileImg() : 사진이 없습니다.");
                throw new RuntimeException("AdminMemberService.updateProfileImg() : 사진이 없습니다.");
            }

            Member member = memberRepository.findByMemberId(memberId);

            // Member가 null인 경우
            if (member == null) {
                log.warn("AdminMemberService.updateProfileImg() : 회원 정보를 찾을 수 없습니다.");
                throw new RuntimeException("AdminMemberService.updateProfileImg() : 회원 정보를 찾을 수 없습니다.");
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
                log.warn("adminMemberService.updateProfileImg() : multipartFile이 null이거나 비어 있습니다");
                throw new RuntimeException("adminMemberService.updateProfileImg() : multipartFile이 null이거나 비어 있습니다");

            }         memberRepository.save(member);
            return ListMemberDTO.builder().profile(member.getProfile()).build();
        }   catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("adminMemberService.updateContact() : 에러 발생.");
        }
    }



    /**
     * 회원삭제
     */
    public void deleteMember(final int adminId, int memberId) {
        checkAdmin(adminId);
        try {
            Optional<Member> member = memberRepository.findById(memberId);
            if (!member.isPresent()) {
                throw new IllegalArgumentException("해당 회원이 삭제되었거나 없습니다.");
            } else if (member.get().getRole() == 3) {
                throw new IllegalArgumentException("해당 회원은 관리자입니다.");
            } else {
                memberRepository.deleteById(memberId);
            }

        } catch (Exception e) {
            throw new RuntimeException("BoardService.deleteBoard() : 에러 발생", e);
        }
    }


    /**
     * 회원 검색(이름 or 이메일 or 전화번호)
     */
    public ResponseEntity<AdminSearchDTO> adminSearch(int adminId, String memberName, String memberEmail, String memberTel,LocalDate birthDay,int page, int size){
        checkAdmin(adminId);

        //페이지 번호를 1부터 시작하도록 조정
        page = Math.max(page - 1, 0);

        AdminSearchDTO adminSearch = AdminSearchDTO.builder()
                .contentMember(new ArrayList<>())
                .build();

        List<Member> member = memberRepository.findByMemberSearchId(memberName,memberEmail,memberTel,birthDay,PageRequest.of(page,size));
        if(member.isEmpty()){
            throw new IllegalArgumentException("멤버가 없습니다.");
        }else if(member.get(0).getRole() == 3){
            throw new IllegalArgumentException("해당 회원은 관리자입니다.");
        }

        for(Member members : member){
            if(members.getRole() != 3){
                adminSearch.getContentMember().add(
                        AdminSearchDTO.ContentMember.builder()
                                .id(members.getId())
                                .memberName(members.getName())
                                .memberEmail(members.getEmail())
                                .memberTel(members.getTel())
                                .birthDay(members.getBirthDay())
                                .joinDay(members.getJoinDay())
                                .role(members.getRole())
                                .build()
                );
            }
        }
        adminSearch.setCount(member.size());
        return new ResponseEntity<>(adminSearch, HttpStatus.OK);
    }

    /**
     * 회원 추가
     */
    // 새 계정 생성 - 이메일 중복 검사
    public AdminAddMemberDTO adminAddMember(int adminId,AdminAddMemberDTO.AddMember addMemberDTO){
        checkAdmin(adminId);
        if(addMemberDTO == null || addMemberDTO.getEmail() == null){
            log.warn("MemberService.add() : memberEntity에 email이 없습니다.");
            throw new RuntimeException("MemberService.add() : member에 email이 없습니다.");
        }

        final String email = addMemberDTO.getEmail();
        if(memberRepository.existsByEmail(email)){
            log.warn("MemberService.add() : 해당 email이 이미 존재합니다.");
            throw new RuntimeException("MemberService.add() : 해당 email이 이미 존재합니다.");
        }
        // 닉네임 중복 체크
        boolean check = checkNickname(addMemberDTO.getNickName());
        if(!check){
            log.warn("MemberService.add() : 중복되는 닉네임입니다.");
            throw new RuntimeException("MemberService.add() : 중복되는 닉네임입니다.");
        }

        try {
            Member member = Member.builder()
                    .email(addMemberDTO.getEmail())
                    .password(passwordEncoder.encode(addMemberDTO.getPassword()))
                    .name(addMemberDTO.getName())
                    .tel(addMemberDTO.getTel())
                    .nickname(addMemberDTO.getNickName())
                    .birthDay(addMemberDTO.getBirthDay())
                    .joinDay(LocalDateTime.now()) // 현재 시간
                    .gender(addMemberDTO.getGender())
                    .role(addMemberDTO.getRole())
                    .state(1)
                    .build();

            int memberId = memberRepository.save(member).getId();

            // 이미지가 있는 경우
            if (addMemberDTO.checkProfileImgRequestNull() && !addMemberDTO.getProfileImgRequest().isEmpty()) {

                MultipartFile multipartFile = addMemberDTO.getProfileImgRequest().get(0);

                if (!multipartFile.isEmpty()) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    String current_date = now.format(dateTimeFormatter);


                    //String absolutePath = filePath +File.separator + "member";
                    String absolutePath = "C:" +File.separator + "smallTour" + File.separator + "member";
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
                                log.warn("MemberService.add() : 지원하지 않는 이미지 형식입니다.");
                                break;
                            }
                        }

                        String new_file_name = String.valueOf(memberId);
                        member.setProfile(new_file_name + originalFileExtension);
                        memberRepository.save(member);

                        file = new File(absolutePath + File.separator + new_file_name + originalFileExtension);
                        multipartFile.transferTo(file); //지정된 경로에 파일 저장

                        file.setWritable(true);
                        file.setReadable(true);
                        break;
                    }
                }
            }
            //entity -> DTO
            //MemberDTO responseMemberDTO = new MemberDTO(member);
            AdminAddMemberDTO addDTO = AdminAddMemberDTO.builder()
                    .id(memberId)
                    .email(member.getEmail())
                    .password(passwordEncoder.encode(member.getPassword()))
                    .name(member.getName())
                    .tel(member.getTel())
                    .nickName(member.getNickname())
                    .birthDay(member.getBirthDay())
                    .joinDay(LocalDateTime.now()) // 현재 시간
                    .gender(member.getGender())
                    .profile(member.getProfile())
                    .role(member.getRole())
                    .state(member.getState())
                    .build();
            return addDTO;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("MemberService.add() : 에러 발생.");
        }
    }

    /**
     년/월/이름 입력하면 해당 정보가져옴
     년 월 입력해서 difference를 알 수 있음
     controller에 month, year, name을 입력 안할시에는 날짜같은경우는 현재 시간 LocalDateTime.now()를 기준으로 검색되고, name이 null일경우는 모든 내역 출력되게함
     */
    public AdminFavoriteGuideCountUpdateDTO countFavoriteGuideStatus(int adminId, Integer month, Integer year, int page, int size, int sort, String name){
        checkAdmin(adminId);

        if(month > 12 || month < 1){
            throw new IllegalArgumentException("월 의 범위를 벗어났습니다.");
        }

        String yearStr = String.valueOf(year);
        if(!yearStr.matches("[0-9]{4}")) { // 정확히 4자리 숫자로만 구성되어 있는지 검사
            throw new IllegalArgumentException("년도는 4자리 숫자로 구성되어야 합니다.");
        }


        //페이지 번호를 1부터 시작하도록 조정
        page = Math.max(page - 1, 0);
        /**
         * 컬럼 별칭은 sort 불가능, natvieQuery로해서 안됨 , JPQL로 바꿔야 함(JPQL에서는 from절 서브쿼리 사용 안됨 )
         */

        Pageable pageable;
        switch(sort){
            case 1:
                // Pageable 객체 생성
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC,"name"));
                break;
            case 2:
                // Pageable 객체 생성
                pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC,"name"));
                break;
            default:
                throw new IllegalArgumentException("정렬 방법을 입력하세요");
        }


        // 이전 월과 년을 계산합니다.
        LocalDate date = LocalDate.of(year, month, 1);
        LocalDate previousMonth = date.minusMonths(1);
        int previousMonthValue = previousMonth.getMonthValue();
        int previousYear = previousMonth.getYear();


        //favoriteGuide테이블에 총 가이드 수
        Integer countGuide = favoriteGuideRepository.findByCountGuide();

        List<Object[]> favoriteGuide = favoriteGuideRepository.findLikesDifferenceAndTotalByMonthAndYear(month, year, previousMonthValue, previousYear,name, pageable);
        List<AdminFavoriteGuideCountUpdateDTO.FavoriteGuideCount> adminFavoriteGuideLog = new ArrayList<>();
        for(Object[] fg : favoriteGuide){
            String differenceMessage = "";
            int difference = Integer.parseInt(String.valueOf(fg[5]));

            if(Integer.parseInt(String.valueOf(fg[5])) > 0){
                differenceMessage = difference + "명의 좋아요 수가 저번달이랑 비교해서 증가 하였습니다.";
            }else if(Integer.parseInt(String.valueOf(fg[5])) < 0){
                differenceMessage = -difference + "명의 좋아요 수가 저번달이랑 비교해서 감소 하였습니다.";
            }else{
                differenceMessage = "저번달이랑 비교해서 변동이 없습니다.";
            }


            int totalCancel = Integer.parseInt(String.valueOf(fg[3]));
            int totalCount = Integer.parseInt(String.valueOf(fg[4]));

            // 피연산자 중 하나를 float로 캐스팅하여 실수 나눗셈을 수행하고 결과를 백분율로 변환해야함.
            float percentage = (totalCount > 0) ? (float) totalCancel / totalCount * 100 : 0;
            int intPercentage = (int) percentage;


            AdminFavoriteGuideCountUpdateDTO.FavoriteGuideCount fgc = AdminFavoriteGuideCountUpdateDTO.FavoriteGuideCount.builder()
                    .guideId(Integer.parseInt(String.valueOf(fg[0])))
                    .likeCount(Integer.parseInt(String.valueOf(fg[2])))
                    .guideName(String.valueOf(fg[1]))
                    .difference(differenceMessage)
                    .cancel(Integer.parseInt(String.valueOf(fg[3])))
                    .cancelPercentge(intPercentage + "%")   //취소율, 취소된 횟수 / 총 갯수(좋아요 + 취소)
                    .build();
            adminFavoriteGuideLog.add(fgc);
        }


        if(favoriteGuide.isEmpty()){
            throw new IllegalArgumentException("해당 날짜에 대한 기록이 없습니다.");
        }

        return AdminFavoriteGuideCountUpdateDTO.builder()
                .count(countGuide)
                .favoriteGuideCount(adminFavoriteGuideLog)
                .build();
    }


}