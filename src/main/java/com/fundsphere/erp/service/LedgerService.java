package com.fundsphere.erp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundsphere.erp.entity.BusinessFund;
import com.fundsphere.erp.entity.Contribution;
import com.fundsphere.erp.entity.EmergencyFundTransaction;
import com.fundsphere.erp.entity.Expense;
import com.fundsphere.erp.repository.BusinessFundRepository;
import com.fundsphere.erp.repository.ContributionRepository;
import com.fundsphere.erp.repository.EmergencyFundTransactionRepository;
import com.fundsphere.erp.repository.ExpenseRepository;

@Service
public class LedgerService {

    @Autowired
    private ContributionRepository contributionRepository;

    @Autowired
    private BusinessFundRepository businessFundRepository;

    @Autowired
    private EmergencyFundTransactionRepository
            emergencyFundTransactionRepository;

    @Autowired
    private ExpenseRepository expenseRepository;


    // =====================================================
    // GET ALL LEDGER ENTRIES
    // =====================================================

    public List<LedgerEntry> getLedgerEntries() {

        List<LedgerEntry> entries =
                new ArrayList<>();


        // =================================================
        // CONTRIBUTIONS
        // =================================================

        List<Contribution> contributions =
                contributionRepository.findAll();


        for (Contribution c : contributions) {

            double amount =
                    c.getAmount() != null
                            ? c.getAmount()
                            : 0.0;


            if (amount <= 0) {
                continue;
            }


            entries.add(
                    new LedgerEntry(

                            c.getContributionDate(),

                            c.getReceiptNo(),

                            "CONTRIBUTION",

                            "Member Contribution",

                            safe(c.getMemberCode()),

                            safe(c.getMemberName()),

                            safe(c.getPaymentMethod()),

                            amount,

                            0.0,

                            safe(c.getRemarks())
                    )
            );
        }


        // =================================================
        // BUSINESS FUND
        // =================================================

        List<BusinessFund> businessFunds =
                businessFundRepository.findAll();


        for (BusinessFund b : businessFunds) {

            double amount =
                    b.getAmount() != null
                            ? b.getAmount()
                            : 0.0;


            if (amount <= 0) {
                continue;
            }


            String type =
                    safe(b.getTransactionType());


            // ---------------------------------------------
            // CONTRIBUTION
            //
            // Already recorded through Contribution
            // ---------------------------------------------

            if ("CONTRIBUTION".equalsIgnoreCase(type)) {

                continue;
            }


            // ---------------------------------------------
            // INCOME / PROFIT = CREDIT
            // ---------------------------------------------

            if ("BUSINESS INCOME".equalsIgnoreCase(type)
                    || "INCOME".equalsIgnoreCase(type)
                    || "PROFIT".equalsIgnoreCase(type)
                    || "DEPOSIT".equalsIgnoreCase(type)) {

                entries.add(
                        new LedgerEntry(

                                b.getTransactionDate(),

                                b.getTransactionNo(),

                                "BUSINESS",

                                type,

                                safe(b.getMemberCode()),

                                safe(b.getMemberName()),

                                safe(b.getPaymentMethod()),

                                amount,

                                0.0,

                                safe(b.getDescription())
                        )
                );
            }


            // ---------------------------------------------
            // WITHDRAWAL = DEBIT
            // ---------------------------------------------

            else if ("WITHDRAWAL".equalsIgnoreCase(type)
                    || "WITHDRAW".equalsIgnoreCase(type)) {

                entries.add(
                        new LedgerEntry(

                                b.getTransactionDate(),

                                b.getTransactionNo(),

                                "BUSINESS",

                                type,

                                safe(b.getMemberCode()),

                                safe(b.getMemberName()),

                                safe(b.getPaymentMethod()),

                                0.0,

                                amount,

                                safe(b.getDescription())
                        )
                );
            }
        }


        // =================================================
        // EMERGENCY FUND
        // =================================================

        List<EmergencyFundTransaction>
                emergencyTransactions =
                emergencyFundTransactionRepository.findAll();


        for (EmergencyFundTransaction e :
                emergencyTransactions) {

            double amount =
                    e.getAmount() != null
                            ? e.getAmount()
                            : 0.0;


            if (amount <= 0) {
                continue;
            }


            String type =
                    safe(e.getTransactionType());


            // ---------------------------------------------
            // CONTRIBUTION
            //
            // Already recorded through Contribution
            // ---------------------------------------------

            if ("CONTRIBUTION".equalsIgnoreCase(type)) {

                continue;
            }


            // ---------------------------------------------
            // INCOME / DEPOSIT = CREDIT
            // ---------------------------------------------

            if ("INCOME".equalsIgnoreCase(type)
                    || "DEPOSIT".equalsIgnoreCase(type)) {

                entries.add(
                        new LedgerEntry(

                                e.getTransactionDate(),

                                e.getTransactionNo(),

                                "EMERGENCY",

                                type,

                                safe(e.getMemberCode()),

                                safe(e.getMemberName()),

                                safe(e.getPaymentMethod()),

                                amount,

                                0.0,

                                safe(e.getDescription())
                        )
                );
            }


            // ---------------------------------------------
            // WITHDRAWAL = DEBIT
            // ---------------------------------------------

            else if ("WITHDRAWAL".equalsIgnoreCase(type)) {

                entries.add(
                        new LedgerEntry(

                                e.getTransactionDate(),

                                e.getTransactionNo(),

                                "EMERGENCY",

                                type,

                                safe(e.getMemberCode()),

                                safe(e.getMemberName()),

                                safe(e.getPaymentMethod()),

                                0.0,

                                amount,

                                safe(e.getDescription())
                        )
                );
            }
        }


        // =================================================
        // EXPENSES
        // =================================================

        List<Expense> expenses =
                expenseRepository.findAll();


        for (Expense e : expenses) {

            double amount =
                    e.getAmount() != null
                            ? e.getAmount()
                            : 0.0;


            if (amount <= 0) {
                continue;
            }


            String description =
                    safe(e.getDescription());


            String expenseType =
                    safe(e.getExpenseType());


            if (!expenseType.isBlank()) {

                description =
                        expenseType
                        + " - "
                        + description;
            }


            entries.add(
                    new LedgerEntry(

                            e.getExpenseDate(),

                            e.getExpenseNo(),

                            "EXPENSE",

                            "EXPENSE",

                            "",

                            "",

                            safe(e.getPaymentMethod()),

                            0.0,

                            amount,

                            description
                    )
            );
        }


        // =================================================
        // SORT BY DATE
        // NEWEST FIRST
        // =================================================

        entries.sort(
                Comparator.comparing(
                        LedgerEntry::getDate,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )
                )
        );


        // =================================================
        // RUNNING BALANCE
        // =================================================

        double balance = 0.0;


        /*
         * Since entries are newest first, calculate
         * balance from oldest to newest.
         */

        List<LedgerEntry> chronological =
                new ArrayList<>(entries);


        chronological.sort(
                Comparator.comparing(
                        LedgerEntry::getDate,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()
                        )
                )
        );


        for (LedgerEntry entry :
                chronological) {

            balance +=
                    entry.getCredit()
                    - entry.getDebit();


            entry.setBalance(balance);
        }


        return entries;
    }


    // =====================================================
    // MEMBER FILTER
    // =====================================================

    public List<LedgerEntry> getMemberLedger(
            String memberCode) {

        if (memberCode == null
                || memberCode.trim().isEmpty()) {

            return getLedgerEntries();
        }


        String code =
                memberCode.trim();


        List<LedgerEntry> all =
                getLedgerEntries();


        List<LedgerEntry> result =
                new ArrayList<>();


        for (LedgerEntry entry : all) {

            if (entry.getMemberCode() != null
                    && entry.getMemberCode()
                        .equalsIgnoreCase(code)) {

                result.add(entry);
            }
        }


        // -----------------------------------------------
        // Recalculate member running balance
        // -----------------------------------------------

        List<LedgerEntry> chronological =
                new ArrayList<>(result);


        chronological.sort(
                Comparator.comparing(
                        LedgerEntry::getDate,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()
                        )
                )
        );


        double balance = 0.0;


        for (LedgerEntry entry :
                chronological) {

            balance +=
                    entry.getCredit()
                    - entry.getDebit();

            entry.setBalance(balance);
        }


        result.sort(
                Comparator.comparing(
                        LedgerEntry::getDate,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )
                )
        );


        return result;
    }


    // =====================================================
    // TOTAL CREDIT
    // =====================================================

    public double getTotalCredit(
            List<LedgerEntry> entries) {

        double total = 0.0;


        for (LedgerEntry entry : entries) {

            total += entry.getCredit();
        }


        return total;
    }


    // =====================================================
    // TOTAL DEBIT
    // =====================================================

    public double getTotalDebit(
            List<LedgerEntry> entries) {

        double total = 0.0;


        for (LedgerEntry entry : entries) {

            total += entry.getDebit();
        }


        return total;
    }


    // =====================================================
    // BALANCE
    // =====================================================

    public double getBalance(
            List<LedgerEntry> entries) {

        return
                getTotalCredit(entries)
                - getTotalDebit(entries);
    }


    // =====================================================
    // SAFE STRING
    // =====================================================

    private static String safe(
            String value) {

        return value != null
                ? value.trim()
                : "";
    }


    // =====================================================
    // LEDGER ENTRY CLASS
    // =====================================================

    public static class LedgerEntry {

        private LocalDate date;

        private String transactionNo;

        private String source;

        private String type;

        private String memberCode;

        private String memberName;

        private String paymentMethod;

        private double credit;

        private double debit;

        private double balance;

        private String description;


        public LedgerEntry(
                LocalDate date,
                String transactionNo,
                String source,
                String type,
                String memberCode,
                String memberName,
                String paymentMethod,
                double credit,
                double debit,
                String description) {

            this.date = date;

            this.transactionNo =
                    transactionNo;

            this.source =
                    source;

            this.type =
                    type;

            this.memberCode =
                    memberCode;

            this.memberName =
                    memberName;

            this.paymentMethod =
                    paymentMethod;

            this.credit =
                    credit;

            this.debit =
                    debit;

            this.description =
                    description;
        }


        public LocalDate getDate() {
            return date;
        }


        public String getTransactionNo() {
            return transactionNo;
        }


        public String getSource() {
            return source;
        }


        public String getType() {
            return type;
        }


        public String getMemberCode() {
            return memberCode;
        }


        public String getMemberName() {
            return memberName;
        }


        public String getPaymentMethod() {
            return paymentMethod;
        }


        public double getCredit() {
            return credit;
        }


        public double getDebit() {
            return debit;
        }


        public double getBalance() {
            return balance;
        }


        public void setBalance(
                double balance) {

            this.balance =
                    balance;
        }


        public String getDescription() {
            return description;
        }
    }
}