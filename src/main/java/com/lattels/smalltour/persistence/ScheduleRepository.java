package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

 
}
