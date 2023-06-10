package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAll();

    Item findById(int id);
 
}
