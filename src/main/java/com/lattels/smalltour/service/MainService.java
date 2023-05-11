package com.lattels.smalltour.service;


import com.lattels.smalltour.dto.GuideSearchDTO;
import com.lattels.smalltour.dto.MemberDTO;
import com.lattels.smalltour.model.GuideReview;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.GuideReviewRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {

    private final MemberRepository memberRepository;

    private final GuideReviewRepository guideReviewRepository;

    //가이드 이름넣고 검색하기, 가이드인 사람들 role이 1임
    public GuideSearchDTO.GuideSearchResult searchGuideByName(String guideName){
        List<Member> guides = memberRepository.findByNameAndRole(guideName, 1);
        List<GuideSearchDTO.ReviewInfo> content = new ArrayList<>();

        for(Member guide : guides){
            List<GuideReview> reviews = guideReviewRepository.findByGuideId(guide.getId());

            for(GuideReview review : reviews){
                GuideSearchDTO.ReviewInfo reviewInfo = new GuideSearchDTO.ReviewInfo();
                reviewInfo.setProfileImg(guide.getProfile());
                reviewInfo.setGuideName(guide.getName());
                reviewInfo.setRating(review.getRating());
                reviewInfo.setReviewerName(review.getReviewer().getName());
                reviewInfo.setContent(review.getContent());

                content.add(reviewInfo);
            }
        }
        GuideSearchDTO.GuideSearchResult result = new GuideSearchDTO.GuideSearchResult();
        result.setCount(content.size());
        result.setContent(content);

        return result;
    }

}