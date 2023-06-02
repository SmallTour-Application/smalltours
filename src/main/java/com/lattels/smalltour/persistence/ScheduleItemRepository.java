package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.ScheduleItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Integer> {

    ScheduleItem findById(int id);

    List<ScheduleItem> findAllByScheduleId(int scheduleId);
 
}
