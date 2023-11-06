package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    @Column(name = "date_pressed")
    private LocalDateTime datePressed;

    @Column(name = "date_cancel")
    private LocalDateTime dateCancel;

    @Column(name = "state")
    private int state;


    /*

    복합 키는 두 개 이상의 컬럼으로 이루어진 키를 말하며,
    이 경우 각 컬럼은 간단한 값(주로 숫자나 문자열)을 가지게 됩니다.
    복합 키를 구성하는 각 컬럼은 자체적으로는 엔티티를 유일하게 식별할 수 없지만,
    다른 컬럼들과 결합하여 엔티티를 유일하게 식별할 수 있습니다.

    이 때문에 복합 키를 구성하는 필드는 간단한 자료형을 사용해야 합니다.
     Integer나 String과 같은 기본 자료형이나 이들의 래퍼 클래스를 사용해야 합니다.
     복합 키 필드에 객체를 사용하는 것은 지양해야 합니다.
    ->이런 이유로 private Member member -> private Integer member로 변경함
    */

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoriteGuideID implements Serializable {

        private int member;  //private Member member -> private int member로변경

        private int guide;

    }

}