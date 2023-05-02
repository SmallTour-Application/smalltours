package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<Room, Integer> {
 
 
}
