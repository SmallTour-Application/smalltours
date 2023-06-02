package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.Hotel;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Tours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    Hotel findById(int id);

    @Query(value = "SELECT h FROM  Hotel h JOIN  h.tours t WHERE h.id = :id AND t.guide = :guide")
    Hotel findByIdAndMember(@Param("id") int id, @Param("guide") Member guideId);

    List<Hotel> findAllByToursId(int toursId);
}
