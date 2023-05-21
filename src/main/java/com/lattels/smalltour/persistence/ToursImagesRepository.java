package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Tours;
import com.lattels.smalltour.model.ToursImages;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ToursImagesRepository extends JpaRepository<ToursImages, Integer> {

    boolean existsByTours(Tours tours);

    List<ToursImages> findByTours(Tours tours);
}
