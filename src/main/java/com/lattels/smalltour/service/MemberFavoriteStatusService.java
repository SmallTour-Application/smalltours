package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.favoriteGuideDTO;
import com.lattels.smalltour.model.FavoriteGuide;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.FavoriteGuideRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public void addFavoriteGuide(int memberId, int guideId) {
        // 해당 가이드 조회 (role이 2인 Member)
        Member guide = memberRepository.findByIdAndRole(guideId, 2)
                .orElseThrow(() -> new IllegalArgumentException("가이드를 찾을 수 없습니다."));

        // 해당 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다.:" + memberId));


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
                .datePressed(LocalDateTime.now())
                .dateCancel(null)
                .state(1)
                .build();

        // 좋아요 누르기
        favoriteGuideRepository.save(favoriteGuide);
    }

    // 좋아요 취소하기
    @Transactional
    public void cancelFavoriteGuide(int memberId, int guideId) {
        // 해당 가이드 조회 (role이 2인 Member)
        Member guide = memberRepository.findByIdAndRole(guideId, 2)
                .orElseThrow(() -> new IllegalArgumentException("가이드를 찾을 수 없습니다."));

        // 해당 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다." + memberId));


        // 좋아요한 가이드 조회
        FavoriteGuide existingFavoriteGuide = favoriteGuideRepository.findByMemberAndGuide(member,guide);


        if (!favoriteGuideRepository.existsByMemberAndGuide(member, guide)) {
            throw new IllegalArgumentException("아직 좋아요를 누르지 않은 가이드입니다.");
        }


        FavoriteGuide favoriteGuide = FavoriteGuide.builder()
                .member(member)
                .guide(guide)
                .datePressed(existingFavoriteGuide.getDatePressed())
                .dateCancel(LocalDateTime.now())
                .state(0)
                .build();

        // 좋아요 취소하기
        favoriteGuideRepository.save(favoriteGuide);
    }

    /**
     *FavoriteGuide에는 id라는 컬럼이 pk로 지정이 안되어있어서, .save를 할경우 insert가아닌 update로 레코드가 됨.
     * 관리자 페이지에서 해당 가이드의 좋아요 수 변동을 보기위함이니 , DB에 회원이 좋아요를 눌렀거나 혹은 취소하였을경우,
     * insert가아닌 update로 저장함.
     */
}