package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.LogMember;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LogMemberRepository extends JpaRepository<LogMember, Integer> {

}
