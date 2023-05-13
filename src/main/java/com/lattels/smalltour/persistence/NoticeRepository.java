package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    Notice findById(int id);

    Notice findByIdAndMemberId(int id, int memberId);

    @Query(value = "SELECT id, member_id, title, created_day, updated_day, view FROM notice ORDER BY created_day", nativeQuery = true)
    Page<Object[]> getList(Pageable pageable);
}
