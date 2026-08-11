package com.fundsphere.erp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundsphere.erp.entity.Ledger;

@Repository
public interface LedgerRepository
        extends JpaRepository<Ledger, Long> {

    // =========================================================
    // ALL LEDGER RECORDS — NEWEST FIRST
    // =========================================================

    List<Ledger> findAllByOrderByTransactionDateDesc();


    // =========================================================
    // MEMBER-WISE LEDGER
    // =========================================================

    List<Ledger> findByMemberCodeOrderByTransactionDateDesc(
            String memberCode
    );


    // =========================================================
    // FUND-WISE LEDGER
    // =========================================================

    List<Ledger> findByFundTypeOrderByTransactionDateDesc(
            String fundType
    );


    // =========================================================
    // DATE-WISE LEDGER
    // =========================================================

    List<Ledger> findByTransactionDateOrderByTransactionDateDesc(
            LocalDate transactionDate
    );


    // =========================================================
    // DATE RANGE
    // =========================================================

    List<Ledger> findByTransactionDateBetweenOrderByTransactionDateDesc(
            LocalDate fromDate,
            LocalDate toDate
    );

}