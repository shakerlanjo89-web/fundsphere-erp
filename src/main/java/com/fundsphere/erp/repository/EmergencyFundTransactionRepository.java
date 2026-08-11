package com.fundsphere.erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundsphere.erp.entity.EmergencyFundTransaction;

@Repository
public interface EmergencyFundTransactionRepository
        extends JpaRepository<EmergencyFundTransaction, Long> {

    List<EmergencyFundTransaction> findAllByOrderByTransactionDateDesc();

    List<EmergencyFundTransaction> findByTransactionType(
            String transactionType
    );

}