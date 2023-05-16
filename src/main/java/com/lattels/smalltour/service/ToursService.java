package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.ToursDTO;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.model.ToursImages;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ToursImagesRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ToursService {

    private final ToursRepository toursRepository;

    private final MemberRepository memberRepository;

    private final ToursImagesRepository toursImagesRepository;

    public void addTours(int memberId, ToursDTO.AddRequestDTO addRequestDTO) {

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
                .build();

        int toursId = toursRepository.save(tours).getId();
/*
        List<ToursImages> toursImagesList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(addRequestDTO.getTourImages())) {

            String absolutePath = "C:" + File.separator + "eum" + File.separator + "tours" + File.separator;
            File file = new File(absolutePath);

            if (!file.exists()) {
                boolean wasSuccessful = file.mkdirs();
                if (!wasSuccessful) {
                    log.warn("file : was not successful");
                }
            }
            int cnt = 1;
            for (MultipartFile toursImg : addRequestDTO.getTourImages()) {
                String originalFileExtension;
                String contentType = toursImg.getContentType();

                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                } else {
                    if (contentType.contains("image/jpeg")) {
                        originalFileExtension = ".jpg";
                    } else if (contentType.contains("image/png")) {
                        originalFileExtension = ".png";
                    } else {
                        break;
                    }
                }

                String new_file_name = String.valueOf(toursId) + "_" + String.valueOf(cnt);

                ToursImages toursImages = ToursImages.builder()
                        .tourId(toursId)
                        .imagePath(new_file_name + originalFileExtension)
                        .build();

                toursImagesList.add(toursImages);

                file = new File(absolutePath + File.separator + new_file_name + originalFileExtension);
                try {
                    toursImg.transferTo(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                savedImagePath = absolutePath + new_file_name + originalFileExtension;
//                member.setProfile(savedImagePath);
//                memberRepository.save(member);
//                file = new File(savedImagePath);
//                multipartFile.transferTo(file);



                file.setWritable(true);
                file.setReadable(true);

                toursImagesRepository.save(toursImages);
                cnt++;
            }
        }*/
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

        toursRepository.delete(tours);

    }

}

