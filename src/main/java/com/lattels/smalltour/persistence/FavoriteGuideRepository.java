package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.FavoriteGuide;

import org.springframework.data.jpa.repository.JpaRepository;


public interface FavoriteGuideRepository extends JpaRepository<FavoriteGuide, FavoriteGuide.FavoriteGuideID> {

 
}
