package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.dto.favoriteGuideDTO;
import com.lattels.smalltour.dto.favoriteTourDTO;
import com.lattels.smalltour.model.FavoriteGuide;
import com.lattels.smalltour.model.FavoriteTour;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.FavoriteGuideRepository;
import com.lattels.smalltour.persistence.FavoriteTourRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
//가이드에 대해서 좋아요 누르기 
//가이드에 대해서 좋아요 누른거 취소하기
public class MemberFavoriteStatusService {

    private final FavoriteGuideRepository favoriteGuideRepository;
    private final MemberRepository memberRepository;

    @Value("${file.path}")
    private String filePath;

    // 좋아요 누르기
    @Transactional
    public void addFavoriteGuide(int memberId, favoriteGuideDTO.favoriteDTO favoriteDTO) {
        // 해당 가이드 조회 (role이 2인 Member)
        Member guide = memberRepository.findByIdAndRole(favoriteDTO.getGuideId(), 2)
                .orElseThrow(() -> new IllegalArgumentException("가이드를 찾을 수 없습니다."));

        // 해당 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id:" + memberId));

        // 회원인지 확인
        if (member.getRole() != 0) {
            throw new IllegalArgumentException("가이드는 좋아요를 누를 수 없습니다.");
        }

        // 이미 좋아요를 눌렀는지 확인
        if(favoriteGuideRepository.existsByMemberAndGuide(member, guide)){
            throw new IllegalArgumentException("이미 좋아요를 누른 가이드입니다.");
        }

        FavoriteGuide favoriteGuide = FavoriteGuide.builder()
                .member(member)
                .guide(guide)
                .build();

        // 좋아요 누르기
        favoriteGuideRepository.save(favoriteGuide);
    }

    // 좋아요 취소하기
    @Transactional
    public void cancelFavoriteGuide(int memberId, favoriteGuideDTO.favoriteDTO favoriteDTO) {
        // 해당 가이드 조회 (role이 2인 Member)
        Member guide = memberRepository.findByIdAndRole(favoriteDTO.getGuideId(), 2)
                .orElseThrow(() -> new IllegalArgumentException("가이드를 찾을 수 없습니다."));

        // 해당 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id:" + memberId));

        // 좋아요를 누른 상태인지 확인
        if (!favoriteGuideRepository.existsByMemberAndGuide(member, guide)) {
            throw new IllegalArgumentException("아직 좋아요를 누르지 않은 가이드입니다.");
        }

        FavoriteGuide favoriteGuide = FavoriteGuide.builder()
                .member(member)
                .guide(guide)
                .build();

        // 좋아요 취소하기
        favoriteGuideRepository.delete(favoriteGuide);
    }
}