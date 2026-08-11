package com.fundsphere.erp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fundsphere.erp.entity.EmergencyFundTransaction;
import com.fundsphere.erp.repository.EmergencyFundTransactionRepository;

@Service
public class EmergencyFundService {

    @Autowired
    private EmergencyFundTransactionRepository repository;


    // =========================================================
    // SAVE / UPDATE TRANSACTION
    // =========================================================

    @Transactional
    public EmergencyFundTransaction saveTransaction(
            EmergencyFundTransaction transaction) {


        // =====================================================
        // DATE
        // =====================================================

        if (transaction.getTransactionDate() == null) {

            transaction.setTransactionDate(
                    LocalDate.now()
            );
        }


        // =====================================================
        // AMOUNT
        // =====================================================

        if (transaction.getAmount() == null) {

            transaction.setAmount(0.0);
        }


        // =====================================================
        // AMOUNT VALIDATION
        // =====================================================

        if (transaction.getAmount() <= 0) {

            throw new IllegalArgumentException(
                    "Amount must be greater than zero."
            );
        }


        // =====================================================
        // TRANSACTION TYPE
        // =====================================================

        if (transaction.getTransactionType() == null
                || transaction.getTransactionType()
                        .trim()
                        .isEmpty()) {

            transaction.setTransactionType(
                    "Withdrawal"
            );
        }


        // =====================================================
        // TRANSACTION NUMBER
        // =====================================================

        if (transaction.getId() == null) {

            if (transaction.getTransactionNo() == null
                    || transaction.getTransactionNo()
                            .trim()
                            .isEmpty()) {

                transaction.setTransactionNo(
                        "EMG-" + System.currentTimeMillis()
                );
            }
        }


        // =====================================================
        // WITHDRAWAL BALANCE VALIDATION
        // =====================================================

        if ("Withdrawal".equalsIgnoreCase(
                transaction.getTransactionType()
        )) {

            double availableBalance =
                    getCurrentBalance(
                            transaction.getId()
                    );


            if (transaction.getAmount()
                    > availableBalance) {

                throw new IllegalArgumentException(
                        "Insufficient Emergency Fund balance. "
                        + "Available Balance: Rs. "
                        + availableBalance
                        + " | Withdrawal Amount: Rs. "
                        + transaction.getAmount()
                );
            }
        }


        // =====================================================
        // SAVE
        // =====================================================

        return repository.save(
                transaction
        );
    }


    // =========================================================
    // CURRENT BALANCE
    // =========================================================

    public double getCurrentBalance() {

        return getCurrentBalance(null);
    }


    // =========================================================
    // CURRENT BALANCE
    // EXCLUDING CURRENT RECORD DURING EDIT
    // =========================================================

    public double getCurrentBalance(
            Long excludeId) {


        List<EmergencyFundTransaction>
                transactions =
                repository.findAll();


        double totalIncome = 0.0;

        double totalWithdrawal = 0.0;


        // =====================================================
        // CALCULATE BALANCE
        // =====================================================

        for (EmergencyFundTransaction transaction :
                transactions) {


            // =================================================
            // EXCLUDE CURRENT RECORD DURING EDIT
            // =================================================

            if (excludeId != null
                    && excludeId.equals(
                            transaction.getId()
                    )) {

                continue;
            }


            double amount =
                    transaction.getAmount() != null
                            ? transaction.getAmount()
                            : 0.0;


            String type =
                    transaction.getTransactionType();


            // =================================================
            // INCOME
            // =================================================

            if ("Contribution".equalsIgnoreCase(type)
                    || "Income".equalsIgnoreCase(type)) {

                totalIncome += amount;
            }


            // =================================================
            // WITHDRAWAL
            // =================================================

            else if ("Withdrawal".equalsIgnoreCase(type)) {

                totalWithdrawal += amount;
            }
        }


        // =====================================================
        // CURRENT BALANCE
        // =====================================================

        return totalIncome - totalWithdrawal;
    }


    // =========================================================
    // DELETE
    // =========================================================

    @Transactional
    public void deleteTransaction(Long id) {

        if (repository.existsById(id)) {

            repository.deleteById(id);
        }
    }

}