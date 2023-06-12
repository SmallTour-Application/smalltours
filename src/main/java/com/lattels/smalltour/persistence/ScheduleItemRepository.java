package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Schedule;
import com.lattels.smalltour.model.ScheduleItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Integer> {

    ScheduleItem findById(int id);

    List<ScheduleItem> findAllByScheduleId(int scheduleId);

    /*
     * 해당 schedule의 item 갯수 가져오기
     */
    long countBySchedule(Schedule schedule);

    /*
    * Schedule과 기본값에 맞는 엔티티 가져오기
    */
    ScheduleItem findByScheduleAndDefaultItem(Schedule schedule, int defaultItem);
 
}
