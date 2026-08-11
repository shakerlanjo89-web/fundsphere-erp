package com.fundsphere.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundsphere.erp.entity.Setting;

@Repository
public interface SettingRepository
        extends JpaRepository<Setting, Long> {

}