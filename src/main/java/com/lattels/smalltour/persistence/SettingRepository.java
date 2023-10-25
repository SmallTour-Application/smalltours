package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SettingRepository extends JpaRepository<Setting, Integer> {

    /*
    * 전체 불러오기
    * */
    @Query(value = "SELECT s FROM Setting s")
    List<Setting> findAll();

    Setting findById(int id);
}
