package com.fundsphere.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fundsphere.erp.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e")
    Double getTotalExpenses();

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.fundType = 'Business Fund'
    """)
    Double getTotalBusinessExpenses();

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.fundType = 'Emergency Fund'
    """)
    Double getTotalEmergencyExpenses();

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.fundType = 'Education Fund'
    """)
    Double getTotalEducationExpenses();

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.fundType = 'Welfare Fund'
    """)
    Double getTotalWelfareExpenses();

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.fundType = 'Administration Fund'
    """)
    Double getTotalAdministrationExpenses();
}