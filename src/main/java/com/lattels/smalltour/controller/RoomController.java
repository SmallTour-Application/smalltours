package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.FlightDTO;
import com.lattels.smalltour.dto.RoomDTO;
import com.lattels.smalltour.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/package/hotel/room")
@Api(tags = "Room Controller", description = "호텔 방 컨트롤러")
public class RoomController {

    private final RoomService roomService;

    // 호텔 방 등록
    @PostMapping(value = "/add")
    @ApiOperation(value = "호텔 방 등록")
    public ResponseEntity<Object> addRoom(@ApiIgnore Authentication authentication,
                                          @Valid RoomDTO.AddRequestDTO addRequestDTO,
                                          @RequestPart(value = "image", required = false) List<MultipartFile> image) {

        roomService.addRoom(Integer.parseInt(authentication.getPrincipal().toString()), addRequestDTO, image);
        return ResponseEntity.ok().build();

    }

    // 호텔 방 수정
    @PostMapping(value = "/update")
    @ApiOperation(value = "호텔 방 수정")
    public ResponseEntity<Object> updateRoom(@ApiIgnore Authentication authentication,
                                             @Valid @RequestBody RoomDTO.UpdateRequestDTO updateRequestDTO,
                                             @RequestPart(value = "image", required = false) List<MultipartFile> image) {

        roomService.updateRoom(Integer.parseInt(authentication.getPrincipal().toString()), updateRequestDTO, image);
        return ResponseEntity.ok().build();

    }

    // 호텔 방 등록
    @PostMapping(value = "/delete")
    @ApiOperation(value = "호텔 방 삭제")
    public ResponseEntity<Object> deleteRoom(@ApiIgnore Authentication authentication, @Valid @RequestBody RoomDTO.IdRequestDTO idRequestDTO) {

        roomService.deleteRoom(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().build();

    }

}
