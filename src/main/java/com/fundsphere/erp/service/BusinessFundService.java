package com.fundsphere.erp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundsphere.erp.entity.BusinessFund;
import com.fundsphere.erp.repository.BusinessFundRepository;

@Service
public class BusinessFundService {


    @Autowired
    private BusinessFundRepository businessFundRepository;


    // =========================================================
    // SAVE / UPDATE
    // =========================================================

    public BusinessFund saveBusinessFund(
            BusinessFund businessFund) {


        // =====================================================
        // DATE
        // =====================================================

        if (businessFund.getTransactionDate() == null) {

            businessFund.setTransactionDate(
                    LocalDate.now()
            );
        }


        // =====================================================
        // AMOUNT
        // =====================================================

        if (businessFund.getAmount() == null) {

            businessFund.setAmount(0.0);
        }


        // =====================================================
        // TRANSACTION TYPE
        // =====================================================

        if (businessFund.getTransactionType() == null
                || businessFund.getTransactionType()
                        .trim()
                        .isEmpty()) {

            throw new IllegalArgumentException(
                    "Transaction Type is required."
            );
        }


        // =====================================================
        // AMOUNT VALIDATION
        // =====================================================

        if (businessFund.getAmount() <= 0) {

            throw new IllegalArgumentException(
                    "Amount must be greater than zero."
            );
        }


        // =====================================================
        // TRANSACTION NUMBER
        // =====================================================

        if (businessFund.getId() == null) {

            if (businessFund.getTransactionNo() == null
                    || businessFund.getTransactionNo()
                            .trim()
                            .isEmpty()) {

                businessFund.setTransactionNo(
                        "BF-" + System.currentTimeMillis()
                );
            }
        }


        // =====================================================
        // WITHDRAWAL BALANCE VALIDATION
        // =====================================================

        if ("Withdrawal".equalsIgnoreCase(
                businessFund.getTransactionType()
        )) {


            double availableBalance =
                    getCurrentBalance(
                            businessFund.getId()
                    );


            if (businessFund.getAmount()
                    > availableBalance) {

                throw new IllegalArgumentException(
                        "Insufficient Business Fund balance. "
                        + "Available Balance: Rs. "
                        + availableBalance
                        + " | Withdrawal Amount: Rs. "
                        + businessFund.getAmount()
                );
            }
        }


        // =====================================================
        // SAVE
        // =====================================================

        return businessFundRepository.save(
                businessFund
        );
    }


    // =========================================================
    // CURRENT BALANCE
    // =========================================================

    public double getCurrentBalance() {

        return getCurrentBalance(null);
    }


    // =========================================================
    // CURRENT BALANCE EXCLUDING ONE RECORD
    // =========================================================

    public double getCurrentBalance(Long excludeId) {


        List<BusinessFund> records =
                businessFundRepository.findAll();


        double totalIncome = 0.0;

        double totalProfit = 0.0;

        double totalWithdrawal = 0.0;


        for (BusinessFund record :
                records) {


            // =================================================
            // EXCLUDE CURRENT RECORD DURING EDIT
            // =================================================

            if (excludeId != null
                    && excludeId.equals(
                            record.getId()
                    )) {

                continue;
            }


            double amount =
                    record.getAmount() != null
                            ? record.getAmount()
                            : 0.0;


            String type =
                    record.getTransactionType();


            // =================================================
            // CONTRIBUTION + BUSINESS INCOME
            // =================================================

            if ("Contribution".equalsIgnoreCase(type)
                    || "Business Income".equalsIgnoreCase(type)) {

                totalIncome += amount;
            }


            // =================================================
            // PROFIT
            // =================================================

            else if ("Profit".equalsIgnoreCase(type)) {

                totalProfit += amount;
            }


            // =================================================
            // WITHDRAWAL
            // =================================================

            else if ("Withdrawal".equalsIgnoreCase(type)) {

                totalWithdrawal += amount;
            }
        }


        return
                totalIncome
                + totalProfit
                - totalWithdrawal;
    }

}