package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.FavoriteTour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FavoriteTourRepository extends JpaRepository<FavoriteTour, FavoriteTour.FavoriteTourID> {


}
