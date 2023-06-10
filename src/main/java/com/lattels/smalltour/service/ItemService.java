package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.ItemDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.UpperPaymentDTO;
import com.lattels.smalltour.model.*;
import com.lattels.smalltour.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public File getItemDirectoryPath() {
        File file = new File(itemFilePath);
        file.mkdirs();

        return file;
    }

    // 가이드 결제 상품 리스트 불러오기
    public List<ItemDTO.ListResponseDTO> getItemList(int memberId) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument((member.getRole() == MemberDTO.MemberRole.GUIDE) || (member.getRole() == MemberDTO.MemberRole.ADMIN), "가이드나 관리자 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 상품 리스트 가져오기
        List<Item> itemList = itemRepository.findAll();
        // 반환할 dto에 저장
        List<ItemDTO.ListResponseDTO> listResponseDTOList = itemList.stream()
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
        viewResponseDTO.setImagePath("http://localhost:8081/img/item/" + viewResponseDTO.getImagePath());

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

}
