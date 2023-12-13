package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.payment.*;
import com.lattels.smalltour.model.*;
import com.lattels.smalltour.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final MemberRepository memberRepository;
    private final ToursRepository toursRepository;
    private final ScheduleItemRepository scheduleItemRepository;
    private final ReviewsRepository reviewsRepository;
    private final GuideReviewRepository guideReviewRepository;
    private final GuideLockRepository guideLockRepository;



    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;

    @Value("${file.path.tours.images}")
    private String filePathToursImages;

    public File getTourDirectoryPath() {
        File file = new File(filePathToursImages);
        file.mkdirs();

        return file;
    }



    /**
     * 패키지를 결제합니다.
     * @param authentication 로그인 정보
     * @param paymentOkRequestDTO 패키지 결제 요청 DTO
     */
    public void paymentOk(Authentication authentication, PaymentOkRequestDTO paymentOkRequestDTO) {
        int packageId = paymentOkRequestDTO.getPackageId();
        int peopleCount = paymentOkRequestDTO.getPeople();

        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원 존재 여부 체크
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 패키지 존재 여부 체크
        Tours tours = toursRepository.findById(packageId).orElse(null);
        Preconditions.checkNotNull(tours, "패키지를 찾을 수 없습니다. (패키지 ID: %s)", packageId);

        // 패키지 일정 아이템 존재 여부 체크
        List<ScheduleItem> scheduleItems = scheduleItemRepository.findAllById(paymentOkRequestDTO.getItems());
        List<Integer> scheduleItemIds = scheduleItems.stream()
                .map(scheduleItem -> scheduleItem.getId())
                .collect(Collectors.toList());

        paymentOkRequestDTO.getItems().forEach(itemId -> Preconditions.checkArgument(scheduleItemIds.contains(itemId), "패키지 일정 아이템을 찾을 수 없습니다. (패키지 일정 아이템 ID: %s)", itemId));

        // 일정 아이템이 해당 패키지의 일정 아이템인지 체크
        scheduleItems.stream().forEach(scheduleItem -> Preconditions.checkArgument(scheduleItem.getSchedule().getTours().getId() == tours.getId(),
                "패키지 일정이 해당 패키지의 일정이 아닙니다. (결제할 패키지 ID: %s, 실제 패키지 ID: %s, 패키지 일정 아이템 ID: %s)", tours.getId(), scheduleItem.getSchedule().getTours().getId(), scheduleItem.getId()));

        // 인원 수 체크
        Preconditions.checkArgument(tours.getMinGroupSize() <= peopleCount, "패키지 인원 조건에 맞지 않습니다. (최소 인원: %s, 최대 인원: %s, 현재 인원: %s)", tours.getMinGroupSize(), tours.getMaxGroupSize(), peopleCount);

        // 가이드락 체크
        int guideId = tours.getGuide().getId();
        LocalDate startDay = paymentOkRequestDTO.getDepartureDay();
        LocalDate endDay = paymentOkRequestDTO.getDepartureDay().plusDays(tours.getDuration());
        List<GuideLock> guideLocks = guideLockRepository.findByGuideIdAndStartDayBetweenOrEndDayBetween(guideId, startDay, endDay);
        Preconditions.checkArgument(guideLocks.isEmpty(), "가이드락에 걸려있는 가이드입니다. (가이드 ID: %s, 결제한 여행 시작일: %s, 결제한 여행 종료일: %s, 가이드락 시작일: %s, 가이드락 종료일: %s)", guideId, startDay, endDay, guideLocks.isEmpty() ? null : guideLocks.get(0).getStartDay(), guideLocks.isEmpty() ? null : guideLocks.get(0).getEndDay());

        // 결제 가격 계산
        int price = calculatePrice(tours, scheduleItems);

        // 결제 내역 저장
        Payment payment = Payment.builder()
                .member(member)
                .tours(tours)
                .price(price)
                .paymentDay(LocalDateTime.now())
                .state(PaymentState.COMPLETE) // PG사에 연결하지 않았으므로 바로 결제 처리
                .departureDay(paymentOkRequestDTO.getDepartureDay())
                .people(peopleCount)
                .build();

        paymentRepository.save(payment);

        // 생성된 결제 ID 불러오기
        int paymentId = payment.getId();

        // 일정 결제 내역 저장
        scheduleItems.stream()
                .map(scheduleItem -> PaymentSchedule.builder()
                        .paymentId(paymentId)
                        .scheduleItemId(scheduleItem.getId())
                        .price(scheduleItem.getPrice())
                        .build())
                .forEach(paymentSchedule -> paymentScheduleRepository.save(paymentSchedule));

        // 가이드락 저장
        GuideLock guideLock = GuideLock.builder()
                .guide(tours.getGuide())
                .startDay(startDay)
                .endDay(endDay)
                .build();

        guideLockRepository.save(guideLock);
    }

    /**
     * 결제를 취소합니다.
     * @param authentication 로그인 정보
     * @param paymentId 결제 ID
     * @return 결제 취소 결과 DTO
     */
    @Transactional
    public PaymentCancelDTO paymentCancel(Authentication authentication, int paymentId) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원 존재 여부 체크
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 결제 존재 여부 체크
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        Preconditions.checkNotNull(payment, "결제 내역을 찾을 수 없습니다. (결제 ID: %s)", paymentId);

        // 취소할 수 있는 기간인지 체크
        // 기간 기준이 없어 미구현
        /*if () {
            return PaymentCancelDTO.builder()
                    .message(PaymentCancelDTO.CancelType.TIME_OVER)
                    .build();
        }*/

        // 이미 결제 완료한 건인지 체크
        if (payment.getState() == PaymentState.COMPLETE) {
            return PaymentCancelDTO.builder()
                    .message(PaymentCancelDTO.CancelType.ALREADY_PAID)
                    .build();
        }

        // 여행 출발일 전인지 체크
        if (LocalDate.now().isAfter(payment.getDepartureDay())) {
            return PaymentCancelDTO.builder()
                    .message(PaymentCancelDTO.CancelType.ARRIVED)
                    .build();
        }

        // 결제된 패키지 일정 정보 삭제
        paymentScheduleRepository.deleteAllByPaymentId(paymentId);

        // 결제 정보 삭제
        paymentRepository.delete(payment);

        // 가이드락 삭제
        int guideId = payment.getTours().getGuide().getId();
        LocalDate startDay = payment.getDepartureDay();
        LocalDate endDay = startDay.plusDays(payment.getTours().getDuration());
        guideLockRepository.deleteByGuideIdAndStartDayAndEndDay(guideId, startDay, endDay);

        // 결제 삭제 완료 DTO 반환
        return PaymentCancelDTO.builder()
                .message(PaymentCancelDTO.CancelType.SUCCESS)
                .build();
    }

    /**
     * 내 결제 내역을 확인합니다.
     * @param authentication 로그인 정보
     * @param page 페이지
     * @param countPerPage 페이지당 결제 내역 수
     * @return 내 결제 내역 목록 DTO
     */
    public PaymentListDTO getMyPayment(Authentication authentication, int page, int countPerPage) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원 존재 여부 체크
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 결제 전체 개수 불러오기
        int paymentCount = Long.valueOf(paymentRepository.countAllByMemberId(memberId)).intValue();

        // 페이지 계산
        Pageable pageable = PageRequest.of(page, countPerPage);

        // 페이지에 맞는 내 결제 내역 불러오기
        List<Payment> payments = paymentRepository.findAllByMemberIdOrderByPaymentDayDesc(memberId, pageable);
        List<PaymentDTO> paymentDTOS = payments.stream()
                .map(payment -> {
                    String thumbUrl = domain+port+"/img/payment/tourThumb/" + payment.getTours().getThumb();
                    return new PaymentDTO(payment, thumbUrl, canReview(payment), canGuideReview(payment));
                })
                .collect(Collectors.toList());

        // DTO 반환
        return PaymentListDTO.builder()
                .count(paymentCount)
                .content(paymentDTOS)
                .build();
    }

    /*paymentId입력시 해당 결제 사람정보 ,6월13일- 추가*/
    public PaymentMemberInfoDTO getPaymentMemberInfo(Authentication authentication, int paymentId) {
        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원 존재 여부 체크
        Member searcher = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. (회원 ID: " + memberId + ")"));

        // 검색을 하는 사람이 가이드나 admin인지 확인
        if (searcher.getRole() == 0) {
            throw new IllegalArgumentException("가이드만이 이 정보를 검색할 수 있습니다. (회원 ID: " + memberId + ")");
        }

        // paymentId로 Payment 찾기
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 결제 ID입니다. (결제 ID: " + paymentId + ")"));

        // 결제한 사람이 회원인지 확인
        Member member = payment.getMember();
        if (member.getRole() != 0) {
            throw new IllegalArgumentException("결제한 사람이 회원이 아닙니다. (결제 ID: " + paymentId + ")");
        }


        // 출발일 가져오기
        LocalDate departureDay = payment.getDepartureDay();
        int people = payment.getPeople();

        // PaymentMemberInfoDTO 객체 생성하여 반환
        return PaymentMemberInfoDTO.builder()
                .memberId(memberId)
                .memberName(member.getName())
                .tel(member.getTel())
                .people(people)
                .departureDay(departureDay)
                .build();
    }


    /**
     * 패키지 기본 가격과 일정 가격의 합을 반환합니다.
     * @param paymentCheckRequestDTO 패키지 가격 확인 요청 DTO
     * @return 패키지 기본 가격 + 일정들 가격 합
     */
    public int checkPrice(PaymentCheckRequestDTO paymentCheckRequestDTO) {
        int packageId = paymentCheckRequestDTO.getPackageId();

        // 패키지 존재 여부 체크
        Tours tours = toursRepository.findById(packageId).orElse(null);
        Preconditions.checkNotNull(tours, "패키지를 찾을 수 없습니다. (패키지 ID: %s)", packageId);

        // 패키지 일정 아이템 존재 여부 체크
        List<ScheduleItem> scheduleItems = scheduleItemRepository.findAllById(paymentCheckRequestDTO.getItems());
        List<Integer> scheduleItemIds = scheduleItems.stream()
                .map(scheduleItem -> scheduleItem.getId())
                .collect(Collectors.toList());

        paymentCheckRequestDTO.getItems().forEach(itemId -> Preconditions.checkArgument(scheduleItemIds.contains(itemId), "패키지 일정 아이템을 찾을 수 없습니다. (패키지 일정 아이템 ID: %s)", itemId));

        // 일정 아이템이 해당 패키지의 일정 아이템인지 체크
        scheduleItems.stream().forEach(scheduleItem -> Preconditions.checkArgument(scheduleItem.getSchedule().getTours().getId() == tours.getId(),
                "패키지 일정이 해당 패키지의 일정이 아닙니다. (결제할 패키지 ID: %s, 실제 패키지 ID: %s, 패키지 일정 아이템 ID: %s)", tours.getId(), scheduleItem.getSchedule().getTours().getId(), scheduleItem.getId()));

        // 결제 가격 계산
        int price = calculatePrice(tours, scheduleItems);

        return price;
    }

    /**
     * 내 결제 내역을 검색합니다.
     * @param paymentSearchRequestDTO 결제 내역 검색 DTO
     * @return 결제 내역 목록 DTO
     */
    public PaymentListDTO searchMyPayment(Authentication authentication, PaymentSearchRequestDTO paymentSearchRequestDTO, int countPerPage) {
        String keyword = paymentSearchRequestDTO.getKeyword();

        // 날짜 검색을 위해 시작 시간은 0시로, 끝 시간은 23시 59분 59초로 설정
        LocalDateTime startDay = LocalDateTime.of(paymentSearchRequestDTO.getStartDay(), LocalTime.MIN);
        LocalDateTime endDay = LocalDateTime.of(paymentSearchRequestDTO.getEndDay(), LocalTime.MAX);

        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원 존재 여부 체크
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 결제 전체 개수 불러오기
        int paymentCount = 0;
        // 패키지명으로 검색
        if (paymentSearchRequestDTO.getType() == PaymentSearchRequestDTO.SearchType.BY_PACKAGE_NAME) {
            paymentCount = Long.valueOf(paymentRepository.countAllByMemberIdAndToursTitleContainsAndPaymentDayBetween(memberId, keyword, startDay, endDay)).intValue();
        }
        // 가이드명으로 검색
        else if (paymentSearchRequestDTO.getType() == PaymentSearchRequestDTO.SearchType.BY_GUIDE_NAME) {
            paymentCount = Long.valueOf(paymentRepository.countAllByMemberIdAndToursGuideNameContainsAndPaymentDayBetween(memberId, keyword, startDay, endDay)).intValue();
        }

        // 페이지 계산
        Pageable pageable = pageable = PageRequest.of(paymentSearchRequestDTO.getPage(), countPerPage);

        // 페이지에 맞는 내 결제 내역 불러오기
        List<Payment> payments = null;
        // 패키지명으로 검색
        if (paymentSearchRequestDTO.getType() == PaymentSearchRequestDTO.SearchType.BY_PACKAGE_NAME) {
            payments = paymentRepository.findAllByMemberIdAndToursTitleContainsAndPaymentDayBetweenOrderByPaymentDayDesc(memberId, keyword, startDay, endDay, pageable);
        }
        // 가이드명으로 검색
        else if (paymentSearchRequestDTO.getType() == PaymentSearchRequestDTO.SearchType.BY_GUIDE_NAME) {
            payments = paymentRepository.findAllByMemberIdAndToursGuideNameContainsAndPaymentDayBetweenOrderByPaymentDayDesc(memberId, keyword, startDay, endDay, pageable);
        }

        List<PaymentDTO> paymentDTOS = payments.stream()
                .map(payment -> {
                    //String thumbUrl = domain + port + "/img/payment/tourThumb/" + payment.getTours().getThumb();
                    String thumbUrl = domain + port + "/img/tours/" + payment.getTours().getThumb();
                    return new PaymentDTO(payment, thumbUrl, canReview(payment), canGuideReview(payment));
                })
                .collect(Collectors.toList());
        // DTO 반환
        return PaymentListDTO.builder()
                .count(paymentCount)
                .content(paymentDTOS)
                .build();
    }

    /**
     * 내 패키지 판매 내역을 검색합니다.
     * @param paymentSearchRequestDTO 결제 내역 검색 DTO
     * @return 결제 내역 목록 DTO
     */
    public PaymentListDTO searchGuidePaymentSell(Authentication authentication, PaymentSearchRequestDTO paymentSearchRequestDTO, int countPerPage) {
        String keyword = paymentSearchRequestDTO.getKeyword();

        // 날짜 검색을 위해 시작 시간은 0시로, 끝 시간은 23시 59분 59초로 설정
        LocalDateTime startDay = LocalDateTime.of(paymentSearchRequestDTO.getStartDay(), LocalTime.MIN);
        LocalDateTime endDay = LocalDateTime.of(paymentSearchRequestDTO.getEndDay(), LocalTime.MAX);

        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원 존재 여부 체크
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "회원을 찾을 수 없습니다. (회원 ID: %s)", memberId);

        // 결제 전체 개수 불러오기
        int paymentCount = 0;
        // 패키지명으로 검색
        if (paymentSearchRequestDTO.getType() == PaymentSearchRequestDTO.SearchType.BY_PACKAGE_NAME) {
            paymentCount = Long.valueOf(paymentRepository.countAllByToursGuideIdAndToursTitleContainsAndPaymentDayBetween(memberId, keyword, startDay, endDay)).intValue();
        }
        // 가이드명으로 검색
        else if (paymentSearchRequestDTO.getType() == PaymentSearchRequestDTO.SearchType.BY_GUIDE_NAME) {
            paymentCount = Long.valueOf(paymentRepository.countAllByToursGuideIdAndToursGuideNameContainsAndPaymentDayBetween(memberId, keyword, startDay, endDay)).intValue();
        }

        // 페이지 계산
        Pageable pageable = PageRequest.of(paymentSearchRequestDTO.getPage(), countPerPage);

        // 페이지에 맞는 내 결제 내역 불러오기
        List<Payment> payments = null;
        // 패키지명으로 검색
        if (paymentSearchRequestDTO.getType() == PaymentSearchRequestDTO.SearchType.BY_PACKAGE_NAME) {
            payments = paymentRepository.findAllByToursGuideIdAndToursTitleContainsAndPaymentDayBetweenOrderByPaymentDayDesc(memberId, keyword, startDay, endDay, pageable);
        }
        // 가이드명으로 검색
        else if (paymentSearchRequestDTO.getType() == PaymentSearchRequestDTO.SearchType.BY_GUIDE_NAME) {
            payments = paymentRepository.findAllByToursGuideIdAndToursGuideNameContainsAndPaymentDayBetweenOrderByPaymentDayDesc(memberId, keyword, startDay, endDay, pageable);
        }

        // DTO로 변환
        List<PaymentDTO> paymentDTOS = payments.stream()
                .map(payment -> {
                    String thumbUrl = domain + port + "/img/payment/tourThumb/" + payment.getTours().getThumb();
                    return new PaymentDTO(payment, thumbUrl, canReview(payment), canGuideReview(payment));
                })
                .collect(Collectors.toList());

        // DTO 반환
        return PaymentListDTO.builder()
                .count(paymentCount)
                .content(paymentDTOS)
                .build();
    }

    /**
     * 패키지 기본 가격과 일정 가격의 합을 계산하여 반환합니다.
     * @param tours 패키지
     * @param scheduleItems 패키지 일정 아이템 목록
     * @return 패키지 기본 가격 + 일정들 가격 합
     */
    public int calculatePrice(Tours tours, List<ScheduleItem> scheduleItems) {
        int defaultPrice = tours.getDefaultPrice(); // 기본 가격
        int scheduleItemsPrice = scheduleItems.stream() // 일정 아이템들 가격
                .mapToInt(scheduleItem -> scheduleItem.getPrice())
                .sum();
        int totalPrice = defaultPrice + scheduleItemsPrice; // 총 가격

        return totalPrice;
    }

    /**
     * 해당 결제에 대한 리뷰 작성 가능 여부를 반환합니다.
     * @param payment 결제
     * @return 리뷰 작성 가능 여부
     */
    public boolean canReview(Payment payment) {
        List<Reviews> reviews = reviewsRepository.findByMemberIdAndPaymentId(payment.getMember().getId(), payment.getId());
        return reviews.isEmpty();
    }

    /**
     * 해당 결제에 대한 가이드 리뷰 작성 가능 여부를 반환합니다.
     * @param payment 결제
     * @return 가이드 리뷰 작성 가능 여부
     */
    public boolean canGuideReview(Payment payment) {
        List<GuideReview> guideReviews = guideReviewRepository.findByReviewerIdAndPaymentId(payment.getMember().getId(), payment.getId());
        return guideReviews.isEmpty();
    }

}
