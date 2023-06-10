package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.ItemDTO;
import com.lattels.smalltour.dto.ToursDTO;
import com.lattels.smalltour.dto.UpperPaymentDTO;
import com.lattels.smalltour.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
@Api(tags = "Item Controller", description = "가이드 결제 상품 컨트롤러")
public class ItemController {

    private final ItemService itemService;

    // 가이드 결제 상품 리스트 불러오기
    @PostMapping(value = "/list")
    @ApiOperation(value = "가이드 결제 상품 리스트 불러오기")
    public ResponseEntity<List<ItemDTO.ListResponseDTO>> getItemList(@ApiIgnore Authentication authentication) {

        List<ItemDTO.ListResponseDTO> listResponseDTOList = itemService.getItemList(Integer.parseInt(authentication.getPrincipal().toString()));
        return ResponseEntity.ok().body(listResponseDTOList);

    }

    // 가이드 결제 상품 정보 불러오기
    @PostMapping(value = "/view")
    @ApiOperation(value = "가이드 결제 상품 정보 불러오기")
    public ResponseEntity<ItemDTO.ViewResponseDTO> getItemInfo(@ApiIgnore Authentication authentication, @Valid @RequestBody ItemDTO.IdRequestDTO idRequestDTO) {

        ItemDTO.ViewResponseDTO viewResponseDTO = itemService.getItemInfo(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().body(viewResponseDTO);

    }

    // 가이드 결제 상품 결제 화면
    @PostMapping(value = "/payment_view")
    @ApiOperation(value = "가이드 결제 상품 결제 화면")
    public ResponseEntity<ItemDTO.PaymentViewResponseDTO> getPaymentView(@ApiIgnore Authentication authentication, @Valid @RequestBody ItemDTO.IdRequestDTO idRequestDTO) {

        ItemDTO.PaymentViewResponseDTO paymentViewResponseDTO = itemService.getPaymentView(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().body(paymentViewResponseDTO);

    }

    // 가이드 결제 상품 결제 화면
    @PostMapping(value = "/payment")
    @ApiOperation(value = "가이드 결제 상품 결제")
    public ResponseEntity<Object> paymentItem(@ApiIgnore Authentication authentication, @Valid @RequestBody UpperPaymentDTO.AddRequestDTO addRequestDTO) {

        itemService.paymentItem(Integer.parseInt(authentication.getPrincipal().toString()), addRequestDTO);
        return ResponseEntity.ok().build();

    }

    // 가이드 결제 상품 리스트 불러오기
    @PostMapping(value = "/payment/list")
    @ApiOperation(value = "가이드가 결제한 리스트 불러오기")
    public ResponseEntity<List<ItemDTO.PaymentListResponseDTO>> getPaymentList(@ApiIgnore Authentication authentication, @PageableDefault(size = 10) Pageable pageable) {

        List<ItemDTO.PaymentListResponseDTO> paymentListResponseDTOList = itemService.getPaymentList(Integer.parseInt(authentication.getPrincipal().toString()), pageable);
        return ResponseEntity.ok().body(paymentListResponseDTOList);

    }

}
