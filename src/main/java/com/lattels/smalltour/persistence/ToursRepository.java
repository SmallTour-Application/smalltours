package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Tours;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ToursRepository extends JpaRepository<Tours, Integer> {


}
