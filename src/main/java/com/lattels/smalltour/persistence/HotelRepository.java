package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.Hotel;

import org.springframework.data.jpa.repository.JpaRepository;


public interface HotelRepository extends JpaRepository<Hotel, Integer> {
  
 
}
