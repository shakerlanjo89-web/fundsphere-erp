package com.fundsphere.erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fundsphere.erp.entity.BusinessTransaction;

public interface BusinessTransactionRepository
        extends JpaRepository<BusinessTransaction, Long> {

    List<BusinessTransaction> findByTransactionType(
            String transactionType
    );

    List<BusinessTransaction> findByMemberCode(
            String memberCode
    );

}