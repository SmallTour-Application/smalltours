
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
@IdClass(FavoriteTour.FavoriteTourID.class)
@Table(name = "favorite_tour")
public class FavoriteTour {

    @Id
    @Column(name = "member_id")
    private int memberId;

    @Id
    @Column(name = "tour_id")
    private int tourId;


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoriteTourID implements Serializable {

        private Integer memberId;

        private Integer tourId;

    }


}