package com.fundsphere.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fundsphere.erp.entity.Contribution;

public interface ContributionRepository extends JpaRepository<Contribution, Long> {

    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM Contribution c")
    Double getTotalContributions();

    @Query("SELECT COALESCE(SUM(c.businessAmount), 0) FROM Contribution c")
    Double getTotalBusinessFund();

    @Query("SELECT COALESCE(SUM(c.emergencyAmount), 0) FROM Contribution c")
    Double getTotalEmergencyFund();

    @Query("SELECT COALESCE(SUM(c.educationAmount), 0) FROM Contribution c")
    Double getTotalEducationFund();

    @Query("SELECT COALESCE(SUM(c.welfareAmount), 0) FROM Contribution c")
    Double getTotalWelfareFund();

    @Query("SELECT COALESCE(SUM(c.administrationAmount), 0) FROM Contribution c")
    Double getTotalAdministrationFund();
}