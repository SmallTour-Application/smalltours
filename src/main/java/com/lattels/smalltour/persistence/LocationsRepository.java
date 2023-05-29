package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Locations;

import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationsRepository extends JpaRepository<Locations, Integer> {

    Locations findById(int id);
 
}
