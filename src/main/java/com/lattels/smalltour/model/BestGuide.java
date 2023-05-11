package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "best_guide")
//우수가이드 테이블
public class BestGuide {

    @Id
    @Column(name = "guide_id")
    private int guideId;

    @Column(name = "set_day")
    private LocalDate setDay; //선정일시

}
