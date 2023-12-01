package com.lattels.smalltour.service.admin;


import com.lattels.smalltour.dto.admin.member.ListMemberDTO;
import com.lattels.smalltour.dto.admin.payment.AdminInterfacePaymentTourList;
import com.lattels.smalltour.dto.admin.payment.AdminPaymentListDTO;
import com.lattels.smalltour.dto.admin.payment.AdminPaymentTourListDTO;
import com.lattels.smalltour.dto.admin.payment.AdminPaymentUnDetailListDTO;
import com.lattels.smalltour.dto.payment.PaymentMemberInfoDTO;
import com.lattels.smalltour.dto.payment.PaymentMemberListDTO;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Payment;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
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

@Slf4j
@Service
@RequiredArgsConstructor
//unauth/search/package , guide 관련 Service
public class AdminPaymentService {
    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;


    private final PasswordEncoder passwordEncoder;

    @Value("${file.path}")
    private String filePath;

    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;

    @Value("${file.path.tours.images}")
    private String filePathToursImages;

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

    //관리자 체크
    private void checkAdmin(Authentication authentication){
        // 관리자 ID 불러오기
        int adminId = Integer.parseInt(authentication.getPrincipal().toString());
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자를 찾을수없습니다."));
        if(admin.getRole() != 3){
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }
    }

    //공통메서드. 사이트 결제 목록가져오기(상태에 따라 다르게 가져오는건 밑에 메서드에서 구현)
    public List<AdminPaymentUnDetailListDTO> common(List<Payment> payment){
        List<AdminPaymentUnDetailListDTO> pl = new ArrayList<>();
        int counter = 1;
        // 결제한 사람이 회원인지 확인
        for (Payment payments : payment) {
            // 출발일 가져오기
            LocalDate departureDay = payments.getDepartureDay();
            int people = payments.getPeople();

            AdminPaymentUnDetailListDTO adminPaymentUnDetailListDTO = AdminPaymentUnDetailListDTO.builder()
                    .id(counter)
                    .tourId(payments.getTours().getId()) // 패키지 아이디
                    .tourName(payments.getTours().getTitle()) //패키지(투어) 이름
                    .email(payments.getMember().getEmail())
                    .price(payments.getTours().getPrice())
                    .memberId(payments.getMember().getId()) //결제한 사람 id
                    .memberName(payments.getMember().getName())
                    .people(people)
                    .departureDay(departureDay)
                    .state(payments.getState())
                    .build();
            // PaymentMemberInfoDTO 객체 생성하여 반환
            pl.add(adminPaymentUnDetailListDTO);
            counter++;

        }
        return pl;
    }

    /*사이트내 회원들 결제정보 (role = 0)
     *  결제완료
     *  */
    public List<AdminPaymentUnDetailListDTO> getPaymentMemberList(Authentication authentication,Integer memberId, int page, int countPerPage) {
        checkAdmin(authentication);
        // 페이지 계산
        Pageable pageable = PageRequest.of(page, countPerPage);
        List<Payment> payment = paymentRepository.findByPaymentMemberId(memberId,pageable);
        return common(payment);
    }

    /*사이트내 회원들 결제취소 (role = 0)
     *  결제취소
     *  */
    public List<AdminPaymentUnDetailListDTO> getPaymentCancelMemberList(Authentication authentication,Integer memberId, int page, int countPerPage) {
        checkAdmin(authentication);
        // 페이지 계산
        Pageable pageable = PageRequest.of(page, countPerPage);
        List<Payment> payment = paymentRepository.findByPaymentCancelMemberId(memberId,pageable);
        return common(payment);
    }

    /*사이트내 회원들 결제환불 (role = 0)
     *  결제취소
     *  */
    public List<AdminPaymentUnDetailListDTO> getPaymentrefundMemberList(Authentication authentication,Integer memberId, int page, int countPerPage) {
        checkAdmin(authentication);
        // 페이지 계산
        Pageable pageable = PageRequest.of(page, countPerPage);
        List<Payment> payment = paymentRepository.findByPaymentRefundMemberId(memberId,pageable);
        return common(payment);
    }

    /*사이트내 회원들 여행완료 (role = 0)
     *  아직 기준이 없음
     *  */

    // 특정 멤버의 결제정보 가져오기
    public AdminPaymentListDTO.MemberPaymentListDTO getPaymentMemberInfo(Authentication authentication, int memberId, Pageable pageable, int state) {
        checkAdmin(authentication);
        // 멤버 정보 가져오기
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        // 멤버의 id와 state로 결제정보 가져오기
        List<Payment> payment = paymentRepository.findByMemberIdAndState(memberId, state, pageable);
        // 총 몇개인지 가져오기
        int totalCnt = paymentRepository.countByMemberIdAndState(memberId, state);
        // paymentmemberinfolistdto 객체 생성
        AdminPaymentListDTO.MemberPaymentListDTO memberPaymentListDTO = AdminPaymentListDTO.MemberPaymentListDTO.
                builder()
                // 전체갯수
                .totalCnt(totalCnt).build();

        // paymentMemberInfoDTO list 생성
        List<AdminPaymentListDTO.MemberPaymentDTO> paymentMemberInfoDTOList = new ArrayList<>();
        int counter = 1;
        // 결제한 사람이 회원인지 확인
        for (Payment payments : payment) {
            // 출발일 가져오기
            LocalDate departureDay = payments.getDepartureDay();
            int people = payments.getPeople();
            // 상품명 가져오기
            String tourName = payments.getTours().getTitle();
            // 상품 아이디 가져오기
            int tourId = payments.getTours().getId();

            AdminPaymentListDTO.MemberPaymentDTO paymentMemberInfoDTO =  AdminPaymentListDTO.MemberPaymentDTO.builder()
                    // 결제금액
                    .price(payment.get(0).getPrice())
                    // 결제인원
                    .people(payment.get(0).getPeople())
                    .departureDay(payment.get(0).getDepartureDay())
                    // 결제일
                    .paymentDay(payment.get(0).getPaymentDay())
                    //결과(state)
                    .state(payment.get(0).getState())
                    .memberId(member.getId())
                    .email(member.getEmail())
                    .name(member.getName())
                    //상품명
                    .toursTitle(tourName)
                    //상품아이디
                    .toursId(tourId)
                    .build();
            // PaymentMemberInfoDTO 객체 생성하여 반환
            paymentMemberInfoDTOList.add(paymentMemberInfoDTO);
            counter++;
        }

        // memberPaymentListDTO에 paymentMemberInfoDTOList 추가
        memberPaymentListDTO.setPaymentList(paymentMemberInfoDTOList);

        //리턴
        return memberPaymentListDTO;
    }


    /**
     * 특정 패키지에 대한 결제 목록
     * Object[] 대신 interface써서 repository에서 사용
     */
    public List<AdminPaymentTourListDTO> getPaymentTourList(Authentication authentication, int tourId, int page) {
        checkAdmin(authentication);

        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<AdminInterfacePaymentTourList> paymentTourLists = paymentRepository.findPaymentTourList(tourId, pageable);
        List<AdminPaymentTourListDTO> paymentTourListDTOs = new ArrayList<>();
        for (AdminInterfacePaymentTourList ap : paymentTourLists) {
            paymentTourListDTOs.add(new AdminPaymentTourListDTO(
                    ap.getPaymentId(),
                    ap.getTourId(),
                    ap.getTitle(),
                    ap.getMemberId(),
                    ap.getMemberName(),
                    ap.getGuideId(),
                    ap.getGuideName(),
                    ap.getPrice(),
                    ap.getState(),
                    ap.getDepartureDay(),
                    ap.getPeople()
            ));
        }
        return paymentTourListDTOs;
    }
}

