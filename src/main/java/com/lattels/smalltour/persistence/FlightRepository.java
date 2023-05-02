package com.lattels.smalltour.persistence;



import com.lattels.smalltour.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FlightRepository extends JpaRepository<Flight, Integer> {
  
 
}
