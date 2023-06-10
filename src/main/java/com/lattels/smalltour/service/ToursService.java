package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.*;
import com.lattels.smalltour.dto.tour.MyPackageDTO;
import com.lattels.smalltour.dto.tour.MyPackageListDTO;
import com.lattels.smalltour.dto.tour.MyPackageListRequestDTO;
import com.lattels.smalltour.model.*;
import com.lattels.smalltour.persistence.*;
import com.lattels.smalltour.util.MultipartUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class ToursService {

    private final ToursRepository toursRepository;

    private final MemberRepository memberRepository;

    private final ToursImagesRepository toursImagesRepository;

    private final GuideLockRepository guideLockRepository;

    private final LocationsRepository locationsRepository;

    @Value("${file.path.tours}")
    private String toursFilePath;

    @Value("${file.path.tours.images}")
    private String toursImagesFilePath;

    public File getToursDirectoryPath() {
        File file = new File(toursFilePath);
        file.mkdirs();

        return file;
    }

    public File getToursImagesDirectoryPath() {
        File file = new File(toursImagesFilePath);
        file.mkdirs();

        return file;
    }

    // 투어 등록
    public void addTours(int memberId, ToursDTO.AddRequestDTO addRequestDTO, List<MultipartFile> tourImages, List<MultipartFile> thumb) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        Tours tours = Tours.builder()
                .guide(member)
                .title(addRequestDTO.getTitle())
                .subTitle(addRequestDTO.getSubTitle())
                .description(addRequestDTO.getDescription())
                .meetingPoint(addRequestDTO.getMeetingPoint())
                .duration(addRequestDTO.getDuration())
                .price(addRequestDTO.getPrice())
                .maxGroupSize(addRequestDTO.getMaxGroupSize())
                .minGroupSize(addRequestDTO.getMinGroupSize())
                .createdDay(LocalDateTime.now())
                .defaultPrice(addRequestDTO.getDefaultPrice())
                .approvals(ToursDTO.ToursApprovals.APPROVAL)
                .build();

        toursRepository.save(tours);

        // 썸네일 이미지가 있을 경우
        if (thumb != null && !thumb.isEmpty()) {

            // 이미지 한개만 저장
            MultipartFile multipartFile = thumb.get(0);

            // 이미지 저장
            saveToursImage(tours, multipartFile);

        }

        // 투어 이미지가 있을 경우
        if (tourImages != null && !tourImages.isEmpty()) {

            // 이미지 저장
            saveToursImageList(tours, tourImages);

        }
    }

    // 투어 수정
    public void updateTours(int memberId, ToursDTO.UpdateRequestDTO updateRequestDTO, List<MultipartFile> tourImages, List<MultipartFile> thumb) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 항공사 정보 가져오기
        Tours oldTours = toursRepository.findByToursId(updateRequestDTO.getId());
        Preconditions.checkNotNull(oldTours, "등록된 투어가 없습니다. (투어 ID : %s)", updateRequestDTO.getId());

        // 항공사 등록인이 맞는지 검사
        Preconditions.checkArgument(memberId == oldTours.getGuide().getId(), "해당 유저와 등록인이 일치하지 않습니다. (수정 요청 회원 ID : %s, 기존 등록 회원 ID : %s)", memberId, oldTours.getGuide().getId());

        Tours tours = Tours.builder()
                .id(updateRequestDTO.getId())
                .guide(member)
                .title(updateRequestDTO.getTitle())
                .subTitle(updateRequestDTO.getSubTitle())
                .description(updateRequestDTO.getDescription())
                .meetingPoint(updateRequestDTO.getMeetingPoint())
                .duration(updateRequestDTO.getDuration())
                .price(updateRequestDTO.getPrice())
                .maxGroupSize(updateRequestDTO.getMaxGroupSize())
                .minGroupSize(updateRequestDTO.getMinGroupSize())
                .createdDay(oldTours.getCreatedDay())
                .updateDay(LocalDateTime.now())
                .defaultPrice(updateRequestDTO.getDefaultPrice())
                .thumb(oldTours.getThumb())
                .build();

        // 썸네일 이미지가 있을 경우
        if (thumb != null && !thumb.isEmpty()) {

            // 이미지 한개만 저장
            MultipartFile multipartFile = thumb.get(0);

            // 이미지 저장
            saveToursImage(tours, multipartFile);

        }

        // 투어 이미지가 있을 경우
        if (tourImages != null && !tourImages.isEmpty()) {

            // 이미지 저장
            saveToursImageList(tours, tourImages);

        }
        toursRepository.save(tours);
    }



    // 투어 삭제
    public void deleteTours(int memberId, ToursDTO.IdRequestDTO idRequestDTO) {

        // 투어 정보 가져오기
        Tours tours = toursRepository.findByToursId(idRequestDTO.getId());
        Preconditions.checkNotNull(tours, "등록된 투어가 없습니다. (투어 ID : %s)", idRequestDTO.getId());

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 정보 등록인이거나 관리자인지 검사
        Preconditions.checkArgument(memberId == tours.getGuide().getId() || member.getRole() == MemberDTO.MemberRole.ADMIN, "해당 투어 등록자가 아닙니다. (투어 ID : %s, 삭제 요청 회원 ID : %s)", idRequestDTO.getId(), memberId);

        //투어 삭제
        tours.setApprovals(ToursDTO.ToursApprovals.DELETE);
        toursRepository.save(tours);

    }

    // 투어 이미지 저장
    private void saveToursImage(Tours tours, MultipartFile multipartFile) {

        // 기존 이미지가 있다면 삭제
        if (tours.getThumb() != null) {
            deleteToursImage(tours);
        }

        // 투어 이미지 경로 가져오기
        File directoryPath = getToursDirectoryPath();

        // 이미지 저장 (파일명 : "투어 ID.확장자")
        File newFile = MultipartUtils.saveImage(multipartFile, directoryPath, String.valueOf(tours.getId()));

        // 이미지 파일명 DB에 저장
        tours.setThumb(newFile.getName());
        toursRepository.save(tours);

    }

    // 투어 이미지 리스트 저장
    private void saveToursImageList(Tours tours, List<MultipartFile> tourImages) {

        // 사진 갯수
        int count = 1;
        for (MultipartFile tourImg : tourImages) {

            // 기존 이미지가 있다면 삭제
            if(toursImagesRepository.existsByTours(tours)) {
                deleteToursImageList(tours);
            }

            // 투어 이미지 리스트 경로 가져오기
            File directoryPath = getToursImagesDirectoryPath();

            // 이미지 저장 (파일명 : "투어 ID_1..2...")
            File newFile = MultipartUtils.saveImage(tourImg, directoryPath, tours.getId() + "_" + count);

            // 투어 이미지 저장
            ToursImages toursImagesEntity = ToursImages.builder()
                    .tours(tours)
                    .imagePath(newFile.getName())
                    .build();
            toursImagesRepository.save(toursImagesEntity);
            count++;

        }

    }

    // 투어 이미지 삭제
    private void deleteToursImage(Tours tours) {

        // 투어 이미지 경로 가져오기
        File directoryPath = getToursDirectoryPath();

        // 이미지 삭제
        String imagePath = tours.getThumb();
        if(imagePath != null) {
            File oldImageFile = new File(directoryPath, imagePath);
            oldImageFile.delete();
        }

    }

    // 투어 이미지 리스트 삭제
    private void deleteToursImageList(Tours tours) {

        // 이미지 리스트 가져오기
        List<ToursImages> toursImagesList = toursImagesRepository.findByTours(tours);
        // 투어 이미지 경로 가져오기
        File directoryPath = getToursImagesDirectoryPath();

        for (ToursImages toursImages : toursImagesList) {

            // 이미지 삭제
            String imagePath = toursImages.getImagePath();
            if(imagePath != null) {
                File oldImageFile = new File(directoryPath, imagePath);
                oldImageFile.delete();
            }

        }

    }

    /**
     * 내 패키지 목록을 불러옵니다.
     * @param authentication 로그인 정보
     * @param myPackageListRequestDto 내 패키지 목록 요청 DTO
     * @param countPerPage 페이지당 패키지 개수
     * @return 내 패키지 목록
     */
    public MyPackageListDTO getMyTourList(Authentication authentication, MyPackageListRequestDTO myPackageListRequestDto, int countPerPage) {
        // 페이지 계산
        Pageable pageable = PageRequest.of(myPackageListRequestDto.getPage() - 1, countPerPage);

        // 회원 ID 불러오기
        int memberId = Integer.parseInt(authentication.getPrincipal().toString());

        // 회원의 패키지 개수 가져오기
        int packageCount = Long.valueOf(toursRepository.countAllByGuideId(memberId)).intValue();

        // 회원의 패키지 목록 불러오기
        Page<Tours> tours = null;
        int sort = myPackageListRequestDto.getSort();

        // 정렬 타입 체크
        Preconditions.checkArgument(sort == MyPackageListRequestDTO.Sort.LATEST || sort == MyPackageListRequestDTO.Sort.STATE,
                "잘못된 정렬 형식입니다. (입력한 정렬 ID: %s)", sort);

        // 최신순
        if (sort == MyPackageListRequestDTO.Sort.LATEST) {
            tours = toursRepository.findAllByGuideIdOrderByCreatedDayDesc(memberId, pageable);
        }
        // 등록여부순 (상태순)
        else if (sort == MyPackageListRequestDTO.Sort.STATE) {
            tours = toursRepository.findAllByGuideIdOrderByApprovalsDesc(memberId, pageable);
        }

        // 패키지를 DTO로 변환
        List<MyPackageDTO> myPackageDTOS = tours.stream()
                .map(tour -> new MyPackageDTO(tour))
                .collect(Collectors.toList());

        // DTO 반환
        return MyPackageListDTO.builder()
                .count(packageCount)
                .content(myPackageDTOS)
                .build();
    }

    // 투어 불러오기
    public ToursDTO.ViewResponseDTO viewTours(ToursDTO.IdRequestDTO idRequestDTO) {

        // 해당 ID에 맞는 투어가 있는지 검사
        Tours tours = toursRepository.findByToursId(idRequestDTO.getId());
        Preconditions.checkNotNull(tours, "등록된 투어가 없습니다. (투어 ID : %s)", idRequestDTO.getId());

        // 반환 DTO에 추가
        ToursDTO.ViewResponseDTO viewResponseDTO = new ToursDTO.ViewResponseDTO(tours);
        // 투어 썸네일 추가
        viewResponseDTO.setThumb("http://localhost:8081/img/tours/" + tours.getThumb());

        // 투어 소유 가이드 정보 가져오기
        Member member = memberRepository.findByMemberId(tours.getGuide().getId());
        MemberDTO.ProfileResponseDTO profileResponseDTO = new MemberDTO.ProfileResponseDTO(member);
        // 반환 DTO에 추가
        viewResponseDTO.setProfileResponseDTO(profileResponseDTO);

        // 특정 기간 투어 잠금 정보 가져오기
        List<GuideLock> guideLockList = guideLockRepository.findAllByGuideOrderByStartDay(member);
        List<GuideLockDTO.ViewResponseDTO> guideLockDTOList = new ArrayList<>();
        // 반복해서 GuideLockDTOList에 저장
        for (GuideLock guideLock : guideLockList) {
            GuideLockDTO.ViewResponseDTO guideLockDTO = new GuideLockDTO.ViewResponseDTO(guideLock);
            guideLockDTOList.add(guideLockDTO);
        }
        // 반환 DTO에 추가
        viewResponseDTO.setGuideLockDTOList(guideLockDTOList);

        // 투어 위치 정보 가져오기
        Locations locations = locationsRepository.findByTours(tours);
        LocationsDTO.ViewResponseDTO locationsDTO = (locations == null) ? null : new LocationsDTO.ViewResponseDTO(locations);
        // 반환 DTO에 추가
        viewResponseDTO.setLocationsDTO(locationsDTO);

        // 투어 이미지 리스트 가져오기
        List<ToursImages> toursImagesList = toursImagesRepository.findByTours(tours);
        List<ToursImagesDTO.ViewResponseDTO> toursImagesDTOList = new ArrayList<>();
        // 반복해서 ToursImagesDTOList에 추가
        for (ToursImages toursImages : toursImagesList) {
            ToursImagesDTO.ViewResponseDTO toursImagesDTO = new ToursImagesDTO.ViewResponseDTO();
            toursImagesDTO.setImagePath("http://localhost:8081/img/tours/images/" + toursImages.getImagePath());
            toursImagesDTOList.add(toursImagesDTO);
        }
        //반환 DTO에 추가
        viewResponseDTO.setToursImagesDTOList(toursImagesDTOList);

        //반환
        return viewResponseDTO;

    }

    /*
    * 가이드 자신이 올린 투어 리스트 가져오기
    */
    public List<ToursDTO.GuideListResponseDTO> getToursList(int memberId, Pageable pageable) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 가이드 회원인지 검사
        Preconditions.checkArgument(member.getRole() == MemberDTO.MemberRole.GUIDE, "가이드 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 투어 리스트 가져오기
        Page<Tours> toursPage = toursRepository.findAllByGuideOrderByCreatedDayDesc(member, pageable);
        Preconditions.checkNotNull(toursPage, "등록된 투어가 없습니다. (회원 ID : %s)", memberId);

        // 반환할 dto 리스트에 저장
        List<Tours> toursList = toursPage.getContent();
        List<ToursDTO.GuideListResponseDTO> responseDTOList = toursList.stream()
                .map(tours -> new ToursDTO.GuideListResponseDTO(tours))
                .collect(Collectors.toList());

        return responseDTOList;

    }
}