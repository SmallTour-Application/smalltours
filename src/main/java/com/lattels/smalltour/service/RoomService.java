package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.RoomDTO;
import com.lattels.smalltour.model.*;
import com.lattels.smalltour.persistence.*;
import com.lattels.smalltour.util.MultipartUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    private final HotelRepository hotelRepository;

    private final MemberRepository memberRepository;

    @Value("${file.path.tours.room}")
    private String roomFilePath;

    public File getRoomDirectoryPath() {
        File file = new File(roomFilePath);
        file.mkdirs();

        return file;
    }

    // 방 등록
    public void addRoom(int memberId, RoomDTO.AddRequestDTO addRequestDTO, List<MultipartFile> image) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 패키지 등록인이 해당 member인지 검사
        Hotel hotel = hotelRepository.findByIdAndMember(addRequestDTO.getHotelId(), member);
        Preconditions.checkNotNull(hotel, "해당 유저와 호텔 등록인이 다릅니다. (회원 ID : %s, 호텔 ID : %s)", memberId, addRequestDTO.getHotelId());

        Room room = Room.builder()
                .hotel(hotel)
                .name(addRequestDTO.getName())
                .price(addRequestDTO.getPrice())
                .minPeople(addRequestDTO.getMinPeople())
                .maxPeople(addRequestDTO.getMaxPeople())
                .description(addRequestDTO.getDescription())
                .addPrice(addRequestDTO.getAddPrice())
                .build();

        roomRepository.save(room);

        // 방 이미지가 있을 경우
        if (image != null && !image.isEmpty()) {

            // 이미지 한개만 저장
            MultipartFile multipartFile = image.get(0);

            // 이미지 저장
            saveRoomImage(room, multipartFile);

        }

    }

    // 방 수정
    public void updateRoom(int memberId, RoomDTO.UpdateRequestDTO updateRequestDTO, List<MultipartFile> image) {

        // 방 정보 가져오기
        Room room = roomRepository.findById(updateRequestDTO.getId());
        Preconditions.checkNotNull(room, "등록된 방이 없습니다. (방 ID : %s)", updateRequestDTO.getId());

        // 방 등록인이 맞는지 검사
        Preconditions.checkArgument(memberId == room.getHotel().getTours().getGuide().getId(), "해당 유저와 등록인이 일치하지 않습니다. (수정 요청 회원 ID : %s, 기존 등록 회원 ID : %s)", memberId, room.getHotel().getTours().getGuide().getId());

        Room newRoom = Room.builder()
                .id(updateRequestDTO.getId())
                .hotel(room.getHotel())
                .name(updateRequestDTO.getName())
                .price(updateRequestDTO.getPrice())
                .minPeople(updateRequestDTO.getMinPeople())
                .maxPeople(updateRequestDTO.getMaxPeople())
                .description(updateRequestDTO.getDescription())
                .addPrice(updateRequestDTO.getAddPrice())
                .image(room.getImage())
                .build();

        // 방 이미지가 있을 경우
        if (image != null && !image.isEmpty()) {

            // 이미지 한개만 저장
            MultipartFile multipartFile = image.get(0);

            // 이미지 저장
            saveRoomImage(newRoom, multipartFile);

        }

        roomRepository.save(newRoom);

    }

    // 방 삭제
    public void deleteRoom(int memberId, RoomDTO.IdRequestDTO idRequestDTO) {

        // 방 정보 가져오기
        Room room = roomRepository.findById(idRequestDTO.getId());
        Preconditions.checkNotNull(room, "등록된 방이 없습니다. (방 ID : %s)", idRequestDTO.getId());

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 정보 등록인이거나 관리자인지 검사
        Preconditions.checkArgument(memberId == room.getHotel().getTours().getGuide().getId() || member.getRole() == MemberDTO.MemberRole.ADMIN, "해당 방 정보 등록자가 아닙니다. (방 정보 ID : %s, 삭제 요청 회원 ID : %s)", idRequestDTO.getId(), memberId);

        // 방 이미지 삭제
        deleteRoomImage(room);

        roomRepository.delete(room);

    }

    // 방 이미지 저장
    private void saveRoomImage(Room room, MultipartFile multipartFile) {

        // 기존 이미지가 있다면 삭제
        if (room.getImage() != null) {
            deleteRoomImage(room);
        }

        // 방 이미지 경로 가져오기
        File directoryPath = getRoomDirectoryPath();

        // 이미지 저장 (파일명 : "투어 ID.확장자")
        File newFile = MultipartUtils.saveImage(multipartFile, directoryPath, String.valueOf(room.getId()));

        // 이미지 파일명 DB에 저장
        room.setImage(newFile.getName());
        roomRepository.save(room);

    }

    // 방 이미지 삭제
    private void deleteRoomImage(Room room) {

        // 투어 이미지 경로 가져오기
        File directoryPath = getRoomDirectoryPath();

        // 이미지 삭제
        String imagePath = room.getImage();
        if(imagePath != null) {
            File oldImageFile = new File(directoryPath, imagePath);
            oldImageFile.delete();
        }

    }

}
