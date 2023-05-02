package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.ScheduleItem;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Integer> {

 
}
