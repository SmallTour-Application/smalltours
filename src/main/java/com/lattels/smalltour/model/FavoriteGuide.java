package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(FavoriteGuide.FavoriteGuideID.class)
@Table(name = "favorite_guide")
public class FavoriteGuide {

    @Id
    @Column(name = "member_id")
    private int memberId;

    @Id
    @Column(name = "guide_id")
    private int guideId;


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoriteGuideID implements Serializable {

        private Integer memberId;

        private Integer guideId;

    }

}