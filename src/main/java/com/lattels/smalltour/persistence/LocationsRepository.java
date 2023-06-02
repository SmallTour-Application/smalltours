package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Locations;

import com.lattels.smalltour.model.Tours;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationsRepository extends JpaRepository<Locations, Integer> {

    Locations findById(int id);

    Locations findByTours(Tours tours);
 
}
