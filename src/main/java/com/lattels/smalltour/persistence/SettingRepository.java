package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Integer> {


    Setting findById(int id);

}
