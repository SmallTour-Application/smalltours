package com.lattels.smalltour.dto.admin.Traffic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminFavoriteGuideCountUpdateDTO {
    private int count; // 좋아요 테이블에 있는 가이드 총 명수
    private List<FavoriteGuideCount> favoriteGuideCount;
    //private List<FavoriteCancelGuideCount> favoriteCancelGuideCount;



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoriteGuideCount {
        private int guideId;
        private int likeCount; // 해당 가이드 총 좋아요 수
        private String guideName;
        private String difference;// 좋아요 수 변동(저번달과 비교)
        private int cancel; //취소 수
        private String cancelPercentge;  //취소율, 취소된 횟수 / 총 갯수(좋아요 + 취소)
    }








}