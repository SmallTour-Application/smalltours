package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.FavoriteTour;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FavoriteTourRepository extends JpaRepository<FavoriteTour, FavoriteTour.FavoriteTourID> {
  
 
}
