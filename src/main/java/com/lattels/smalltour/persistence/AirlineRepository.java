package com.lattels.smalltour.persistence;



 
import com.lattels.smalltour.model.Airline;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AirlineRepository extends JpaRepository<Airline, Integer> {

    Airline findById(int id);

}
