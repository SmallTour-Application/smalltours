/*
package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(GuideEducation.GuideEducationID.class)
@Table(name = "guide_education")
public class GuideEducation {


    @Id
    @Column(name = "guide_id")
    private int guideId;

    @Column(name = "last_view")
    private LocalTime lastView; //DB에선 타입이 TIME

    @Column(name = "is_finish")
    private int isFinish;


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GuideEducationID implements Serializable {

        private Integer educationId;

        private Integer guideId;

    }

}*/
