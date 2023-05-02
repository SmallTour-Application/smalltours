package com.lattels.smalltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Tours_Categories")
//우수가이드 테이블
public class ToursCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @Column(name = "tour_id")
    private int tourId;

    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "approval_status")
    private int approvalStatus;
    
}
