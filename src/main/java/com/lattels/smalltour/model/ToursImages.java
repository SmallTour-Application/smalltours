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
@Table(name = "tour_images")
public class ToursImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동증가
    @Column(name = "id")
    private int id; // 사용자에게 고유하게 부여되는 값

    @Column(name = "tour_id")
    private int tourId;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "description")
    private String description;

    @Column(name = "created_day")
    private LocalDate createdDay;


}
