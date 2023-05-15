package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.model.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    Schedule findById(int id);

    @Query(value = "SELECT s FROM Schedule s JOIN s.tours t WHERE s.id = :id AND t.guide = :member")
    Schedule findByIdAndMember(@Param("id") int id, @Param("member") Member member);

}
