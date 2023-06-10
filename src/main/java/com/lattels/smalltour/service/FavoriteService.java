package com.lattels.smalltour.service;

import com.google.common.base.Preconditions;
import com.lattels.smalltour.dto.ToursDTO;
import com.lattels.smalltour.model.FavoriteTour;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.persistence.FavoriteTourRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.persistence.ToursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final MemberRepository memberRepository;

    private final ToursRepository toursRepository;

    private final FavoriteTourRepository favoriteTourRepository;

    /*
    * 투어 좋아요 추가
    */
    public void add(int memberId, ToursDTO.IdRequestDTO idRequestDTO) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 등록된 투어인지 검사
        Tours tours = toursRepository.findByToursId(idRequestDTO.getId());
        Preconditions.checkNotNull(tours, "등록된 투어가 아닙니다. (투어 ID : %s)", idRequestDTO.getId());

        // 투어 업로더인지 검사
        Preconditions.checkArgument(memberId != tours.getGuide().getId(), "자신이 등록한 투어에 좋아요를 누를 수 없습니다.");

        favoriteTourRepository.save(
                FavoriteTour.builder()
                        .memberId(memberId)
                        .tourId(tours.getId())
                        .build()
        );

    }


    /*
    * 투어 좋아요 삭제
    */
    public void delete(int memberId, ToursDTO.IdRequestDTO idRequestDTO) {

        // 등록된 회원인지 검사
        Member member = memberRepository.findByMemberId(memberId);
        Preconditions.checkNotNull(member, "등록된 회원이 아닙니다. (회원 ID : %s)", memberId);

        // 등록된 투어인지 검사
        Tours tours = toursRepository.findByToursId(idRequestDTO.getId());
        Preconditions.checkNotNull(tours, "등록된 투어가 아닙니다. (투어 ID : %s)", idRequestDTO.getId());

        // 등록된 좋아요인지 검사
        FavoriteTour favoriteTour = favoriteTourRepository.findByMemberIdAndTourId(memberId, tours.getId());
        Preconditions.checkNotNull(favoriteTour, "해당 투어에 좋아요를 누르지 않았습니다. (투어 ID : %s)", idRequestDTO.getId());

        favoriteTourRepository.delete(favoriteTour);

    }
}
