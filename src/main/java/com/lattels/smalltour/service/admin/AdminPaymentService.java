package com.lattels.smalltour.service.admin;


import com.lattels.smalltour.dto.admin.member.ListMemberDTO;
import com.lattels.smalltour.dto.payment.PaymentMemberInfoDTO;
import com.lattels.smalltour.dto.payment.PaymentMemberListDTO;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Payment;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public List<PaymentMemberListDTO> common(List<Payment> payment){
        List<PaymentMemberListDTO> pl = new ArrayList<>();
        int counter = 1;
        // 결제한 사람이 회원인지 확인
        for (Payment payments : payment) {
            Member member = payments.getMember();
            // 출발일 가져오기
            LocalDate departureDay = payments.getDepartureDay();
            int people = payments.getPeople();

            PaymentMemberListDTO paymentMemberListDTO = PaymentMemberListDTO.builder()
                    .id(counter)
                    .tourName(payments.getTours().getTitle())
                    //.memberId(member.getId())
                    .email(member.getEmail())
                    .price(payments.getTours().getPrice())
                    .memberName(member.getName())
                    .people(people)
                    .departureDay(departureDay)
                    .state(payments.getState())
                    .build();
            // PaymentMemberInfoDTO 객체 생성하여 반환
            pl.add(paymentMemberListDTO);
            counter++;

        }
        return pl;
    }

    /*사이트내 회원들 결제정보 (role = 0)
     *  결제완료
     *  */
    public List<PaymentMemberListDTO> getPaymentMemberList(Authentication authentication, int page, int countPerPage) {
        checkAdmin(authentication);
        // 페이지 계산
        Pageable pageable = PageRequest.of(page, countPerPage);
        List<Payment> payment = paymentRepository.findByPaymentMemberId(pageable);
        return common(payment);
    }

    /*사이트내 회원들 결제취소 (role = 0)
     *  결제취소
     *  */
    public List<PaymentMemberListDTO> getPaymentCancelMemberList(Authentication authentication, int page, int countPerPage) {
        checkAdmin(authentication);
        // 페이지 계산
        Pageable pageable = PageRequest.of(page, countPerPage);
        List<Payment> payment = paymentRepository.findByPaymentCancelMemberId(pageable);
        return common(payment);
    }

    /*사이트내 회원들 결제환불 (role = 0)
     *  결제취소
     *  */
    public List<PaymentMemberListDTO> getPaymentrefundMemberList(Authentication authentication, int page, int countPerPage) {
        checkAdmin(authentication);
        // 페이지 계산
        Pageable pageable = PageRequest.of(page, countPerPage);
        List<Payment> payment = paymentRepository.findByPaymentRefundMemberId(pageable);
        return common(payment);
    }

    /*사이트내 회원들 여행완료 (role = 0)
     *  아직 기준이 없음
     *  */
 

}