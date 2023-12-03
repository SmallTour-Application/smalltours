package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Integer> {

    Page<Item> findAll(Pageable pageable);

    Item findById(int id);
 
}
