package com.fundsphere.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fundsphere.erp.entity.BusinessFund;

public interface BusinessFundRepository
        extends JpaRepository<BusinessFund, Long> {

    Optional<BusinessFund> findByTransactionNo(String transactionNo);

}