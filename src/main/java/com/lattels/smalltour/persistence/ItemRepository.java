package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Integer> {
  
 
}
