package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(EducationLog.EducationLogID.class)
@Table(name = "education_log")
public class EducationLog {

    @Id
    @Column(name = "education_id")
    private int educationId;

    @Id
    @Column(name = "guide_id")
    private int guideId;

    @Column(name = "last_view")
    private LocalTime lastView; //DB에선 타입이 TIME

    @Column(name = "state")
    private int state;

    @Column(name = "completed_date")
    private LocalDate completedDate;


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducationLogID implements Serializable {

        private Integer educationId;

        private Integer guideId;

    }

}
