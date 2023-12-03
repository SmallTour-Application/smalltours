package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.ItemDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.UpperPaymentDTO;
import com.lattels.smalltour.exception.ErrorCode;
import com.lattels.smalltour.exception.ResponseMessageException;
import com.lattels.smalltour.model.*;
import com.lattels.smalltour.persistence.*;
import com.lattels.smalltour.util.MultipartUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;

    private final GuideReviewRepository guideReviewRepository;

    private final ToursRepository toursRepository;

    private final UpperPaymentRepository upperPaymentRepository;

    @Value("${file.path.item}")
    private String itemFilePath;

    @Value("${server.domain}")
    private String domain;

    @Value("${server.port}")
    private String port;

    public File getItemDirectoryPath() {
        File file = new File(itemFilePath);
        file.mkdirs();

        return file;
    }

    // 가이드 결제 상품 리스트 불러오기
    public List<ItemDTO.ListResponseDTO> getItemList(Authentication authentication, Pageable pageable) {

        // 등록된 회원인지 검사
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument((member.getRole() == MemberDTO.MemberRole.GUIDE) || (member.getRole() == MemberDTO.MemberRole.ADMIN), "가이드나 관리자 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 상품 리스트 가져오기
        Page<Item> pages = itemRepository.findAll(pageable);
        List<Item> items = pages.getContent();
        // 반환할 dto에 저장
        List<ItemDTO.ListResponseDTO> listResponseDTOList = items.stream()
                .map(item -> new ItemDTO.ListResponseDTO(item))
                .collect(Collectors.toList());

        return listResponseDTOList;

    }

    // 가이드 결제 상품 정보 불러오기
    public ItemDTO.ViewResponseDTO getItemInfo(int memberId, ItemDTO.IdRequestDTO idRequestDTO) {

        // Item 가져오기
        Item item = itemRepository.findById(idRequestDTO.getId());
        Preconditions.checkNotNull(item, "등록된 아이템이 아닙니다. (item ID : %s)", idRequestDTO.getId());

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument((member.getRole() == MemberDTO.MemberRole.GUIDE) || (member.getRole() == MemberDTO.MemberRole.ADMIN), "가이드나 관리자 회원이 아닙니다. (회원 ID : %s)", memberId);

        ItemDTO.ViewResponseDTO viewResponseDTO = new ItemDTO.ViewResponseDTO(item);
        viewResponseDTO.setImagePath(domain + port + "/img/item/" + viewResponseDTO.getImagePath());

        return viewResponseDTO;

    }

    // 가이드 결제 상품 결제 화면
    public ItemDTO.PaymentViewResponseDTO getPaymentView(int memberId, ItemDTO.IdRequestDTO idRequestDTO) {

        // Item 가져오기
        Item item = itemRepository.findById(idRequestDTO.getId());
        Preconditions.checkNotNull(item, "등록된 아이템이 아닙니다. (item ID : %s)", idRequestDTO.getId());

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument((member.getRole() == MemberDTO.MemberRole.GUIDE) || (member.getRole() == MemberDTO.MemberRole.ADMIN), "가이드나 관리자 회원이 아닙니다. (회원 ID : %s)", memberId);

        ItemDTO.PaymentViewResponseDTO paymentViewResponseDTO = new ItemDTO.PaymentViewResponseDTO(item);

        return paymentViewResponseDTO;

    }

    // 상품 결제
    public void paymentItem(int memberId, UpperPaymentDTO.AddRequestDTO addRequestDTO) {

        // Item 가져오기
        Item item = itemRepository.findById(addRequestDTO.getItemId());
        Preconditions.checkNotNull(item, "등록된 아이템이 아닙니다. (item ID : %s)", addRequestDTO.getItemId());

        // Tours 가져오기
        Tours tours = toursRepository.findByToursId(addRequestDTO.getToursId());
        Preconditions.checkNotNull(item, "등록된 투어가 아닙니다. (Tours ID : %s)", addRequestDTO.getToursId());

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument((member.getRole() == MemberDTO.MemberRole.GUIDE) || (member.getRole() == MemberDTO.MemberRole.ADMIN), "가이드나 관리자 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원이라면
        if (member.getRole() == MemberDTO.MemberRole.GUIDE) {
            // 가이드 평점 + 투어 평점의 평점이 4가 넘는지 검사
            double rating = guideReviewRepository.getGuideAndToursRating(member.getId());
            Preconditions.checkArgument(rating > 4, "평점이 4가 넘지 않습니다.");

        }

        UpperPayment upperPayment = UpperPayment.builder()
                .item(item)
                .guide(member)
                .tours(tours)
                .payDay(LocalDateTime.now())
                .build();
        upperPaymentRepository.save(upperPayment);

    }

    // 강사가 결제한 리스트
    public List<ItemDTO.PaymentListResponseDTO> getPaymentList(int memberId, Pageable pageable) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument((member.getRole() == MemberDTO.MemberRole.GUIDE) || (member.getRole() == MemberDTO.MemberRole.ADMIN), "가이드나 관리자 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 결제한 상위노출 리스트 가져오기
        Page<UpperPayment> upperPaymentPage = upperPaymentRepository.findAllByGuideOrderByPayDay(member, pageable);
        List<UpperPayment> upperPaymentList = upperPaymentPage.getContent();

        List<ItemDTO.PaymentListResponseDTO> paymentListResponseDTOList = new ArrayList<>();
        for (UpperPayment upperpayment: upperPaymentList) {

            // item 가져오기
            Item item = itemRepository.findById(upperpayment.getItem().getId());
            // tours 가져오기
            Tours tours = toursRepository.findByToursId(upperpayment.getTours().getId());
            // 만료일 저장
            LocalDateTime expirationDate = upperpayment.getPayDay().plusMonths(item.getPeriod());

            // dto에 값 넣기
            ItemDTO.PaymentListResponseDTO paymentListResponseDTO = new ItemDTO.PaymentListResponseDTO(upperpayment);
            paymentListResponseDTO.setItemTitle(item.getTitle());
            paymentListResponseDTO.setItemPrice(item.getPrice());
            paymentListResponseDTO.setToursId(tours.getId());
            paymentListResponseDTO.setToursTitle(tours.getTitle());
            paymentListResponseDTO.setExpirationDate(expirationDate);
            // 만료일이 현재보다 뒤라면 1 (만료) 할당
            if (expirationDate.isBefore(LocalDateTime.now())) {
                paymentListResponseDTO.setState(1);
            }
            else {
                paymentListResponseDTO.setState(0);
            }
            paymentListResponseDTOList.add(paymentListResponseDTO);
        }

        return paymentListResponseDTOList;
    }

    /**
     * 상품 추가하기
     * @param authentication 로그인 정보
     * @param addRequestDTO 상품 추가 요청 DTO
     * @param image 이미지
     */
    public void addItem(Authentication authentication, ItemDTO.AddRequestDTO addRequestDTO, List<MultipartFile> image) {

        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member admin = memberRepository.findByMemberId(memberId);
        // 등록된 회원인지 검사
        if (admin == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (admin.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }

        Item item = Item.builder()
                .title(addRequestDTO.getTitle())
                .price(addRequestDTO.getPrice())
                .period(addRequestDTO.getPeriod())
                .type(addRequestDTO.getType())
                .state(addRequestDTO.getState())
                .build();
        itemRepository.save(item);

        // 이미지가 있을 경우
        if (image != null && !image.isEmpty()) {

            // 이미지 한개만 저장
            MultipartFile multipartFile = image.get(0);

            // 이미지 저장
            saveItemImage(item, multipartFile);

        }

    }

    /**
     * 상품 수정
     * @param authentication 로그인 정보
     * @param updateRequestDTO 상품 수정 DTO
     * @param image 이미지
     */
    public void updateItem(Authentication authentication, ItemDTO.UpdateRequestDTO updateRequestDTO, List<MultipartFile> image) {

        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member admin = memberRepository.findByMemberId(memberId);
        // 등록된 회원인지 검사
        if (admin == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (admin.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }
        Item item = itemRepository.findById(updateRequestDTO.getId());
        // 유효한 상품인지 확인
        if (item == null) {
            throw new ResponseMessageException(ErrorCode.ITEM_NOT_FOUND);
        }

        Item newItem = Item.builder()
                .id(updateRequestDTO.getId())
                .title(updateRequestDTO.getTitle())
                .price(updateRequestDTO.getPrice())
                .period(updateRequestDTO.getPeriod())
                .type(updateRequestDTO.getType())
                .state(updateRequestDTO.getState())
                .imagePath(item.getImagePath())
                .build();

        // 이미지가 있을 경우
        if (image != null && !image.isEmpty()) {

            // 이미지 한개만 저장
            MultipartFile multipartFile = image.get(0);

            // 이미지 저장
            saveItemImage(item, multipartFile);

        }

        itemRepository.save(newItem);

    }

    /**
     * 상품 삭제
     * @param authentication 로그인 정보
     * @param idRequestDTO 상품 아이디 DTO
     */
    public void deleteItem(Authentication authentication, ItemDTO.IdRequestDTO idRequestDTO) {

        int memberId = Integer.parseInt(authentication.getPrincipal().toString());
        Member admin = memberRepository.findByMemberId(memberId);
        // 등록된 회원인지 검사
        if (admin == null) {
            throw new ResponseMessageException(ErrorCode.USER_UNREGISTERED);
        }
        // 관리자 회원인지 검사
        if (admin.getRole() != MemberDTO.MemberRole.ADMIN) {
            throw new ResponseMessageException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }
        Item item = itemRepository.findById(idRequestDTO.getId());
        // 유효한 상품인지 확인
        if (item == null) {
            throw new ResponseMessageException(ErrorCode.ITEM_NOT_FOUND);
        }

        item.setState(ItemDTO.ItemState.DELETE);
        itemRepository.save(item);

    }

    /**
     * 이미지 저장
     * @param item Item 엔티티
     * @param multipartFile 이미지
     */
    private void saveItemImage(Item item, MultipartFile multipartFile) {

        // 기존 이미지가 있다면 삭제
        if (item.getImagePath() != null) {
            deleteItemImage(item);
        }

        // 방 이미지 경로 가져오기
        File directoryPath = getItemDirectoryPath();

        // 이미지 저장 (파일명 : "투어 ID.확장자")
        File newFile = MultipartUtils.saveImage(multipartFile, directoryPath, String.valueOf(item.getId()));

        // 이미지 파일명 DB에 저장
        item.setImagePath(newFile.getName());
        itemRepository.save(item);

    }

    /**
     * @param item Item 엔티티
     */
    private void deleteItemImage(Item item) {

        // 투어 이미지 경로 가져오기
        File directoryPath = getItemDirectoryPath();

        // 이미지 삭제
        String imagePath = item.getImagePath();
        if(imagePath != null) {
            File oldImageFile = new File(directoryPath, imagePath);
            oldImageFile.delete();
        }

    }

}
