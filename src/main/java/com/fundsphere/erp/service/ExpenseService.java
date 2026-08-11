package com.fundsphere.erp.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundsphere.erp.entity.Expense;
import com.fundsphere.erp.repository.ExpenseRepository;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;


    public Expense saveExpense(Expense expense) {

        // =========================
        // DATE
        // =========================

        if (expense.getExpenseDate() == null) {
            expense.setExpenseDate(LocalDate.now());
        }


        // =========================
        // AMOUNT
        // =========================

        if (expense.getAmount() == null) {
            expense.setAmount(0.0);
        }


        // =========================
        // EXPENSE NUMBER
        // =========================
        // New expense = generate number
        // Existing expense = keep old number

        if (expense.getId() == null) {

            if (expense.getExpenseNo() == null
                    || expense.getExpenseNo().trim().isEmpty()) {

                expense.setExpenseNo(
                        "EXP-" + System.currentTimeMillis()
                );
            }
        }


        // =========================
        // SAVE / UPDATE
        // =========================

        return expenseRepository.save(expense);
    }

}