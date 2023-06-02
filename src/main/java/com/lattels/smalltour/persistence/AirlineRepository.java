package com.lattels.smalltour.persistence;



 
import com.lattels.smalltour.model.Airline;

import com.lattels.smalltour.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AirlineRepository extends JpaRepository<Airline, Integer> {

    Airline findById(int id);

    @Query(value = "SELECT a FROM  Airline a JOIN  a.tours t WHERE a.id = :id AND t.guide = :guide")
    Airline findByIdAndMember(@Param("id") int id, @Param("guide") Member guideId);

    List<Airline> findAllByToursId(int tourId);

}
