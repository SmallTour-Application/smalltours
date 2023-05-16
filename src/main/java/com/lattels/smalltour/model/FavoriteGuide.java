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
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Member guide;


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoriteGuideID implements Serializable {

        private Member member;

        private Member guide;

    }

}