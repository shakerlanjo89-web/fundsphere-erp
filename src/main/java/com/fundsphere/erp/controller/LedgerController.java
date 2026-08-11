package com.fundsphere.erp.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fundsphere.erp.entity.BusinessFund;
import com.fundsphere.erp.entity.Contribution;
import com.fundsphere.erp.entity.EmergencyFundTransaction;
import com.fundsphere.erp.entity.Expense;
import com.fundsphere.erp.repository.BusinessFundRepository;
import com.fundsphere.erp.repository.ContributionRepository;
import com.fundsphere.erp.repository.EmergencyFundTransactionRepository;
import com.fundsphere.erp.repository.ExpenseRepository;

@Controller
public class LedgerController {

    @Autowired
    private ContributionRepository contributionRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BusinessFundRepository businessFundRepository;

    @Autowired
    private EmergencyFundTransactionRepository
            emergencyFundTransactionRepository;


    // =========================================================
    // LEDGER
    // =========================================================

    @GetMapping("/ledger")
    public String ledger(

            @RequestParam(required = false)
            String memberCode,

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            String fundType,

            @RequestParam(required = false)
            String fromDate,

            @RequestParam(required = false)
            String toDate,

            Model model) {


        // =====================================================
        // GET ALL DATA
        // =====================================================

        List<Contribution> contributions =
                contributionRepository.findAll();

        List<Expense> expenses =
                expenseRepository.findAll();

        List<BusinessFund> businessFunds =
                businessFundRepository.findAll();

        List<EmergencyFundTransaction>
                emergencyTransactions =
                emergencyFundTransactionRepository.findAll();


        // =====================================================
        // MEMBER FILTER
        // =====================================================

        if (memberCode != null
                && !memberCode.trim().isEmpty()) {

            String selectedMember =
                    memberCode.trim();


            contributions =
                    contributions.stream()
                            .filter(c ->
                                    c.getMemberCode() != null
                                    && c.getMemberCode()
                                            .equalsIgnoreCase(
                                                    selectedMember
                                            ))
                            .collect(Collectors.toList());


            businessFunds =
                    businessFunds.stream()
                            .filter(b ->
                                    b.getMemberCode() != null
                                    && b.getMemberCode()
                                            .equalsIgnoreCase(
                                                    selectedMember
                                            ))
                            .collect(Collectors.toList());


            emergencyTransactions =
                    emergencyTransactions.stream()
                            .filter(e ->
                                    e.getMemberCode() != null
                                    && e.getMemberCode()
                                            .equalsIgnoreCase(
                                                    selectedMember
                                            ))
                            .collect(Collectors.toList());


            // General expenses do not have member code.
            expenses =
                    new ArrayList<>();
        }


        // =====================================================
        // DATE FILTER
        // =====================================================

        LocalDate from = null;
        LocalDate to = null;


        try {

            if (fromDate != null
                    && !fromDate.trim().isEmpty()) {

                from = LocalDate.parse(fromDate);
            }

        } catch (Exception ignored) {
        }


        try {

            if (toDate != null
                    && !toDate.trim().isEmpty()) {

                to = LocalDate.parse(toDate);
            }

        } catch (Exception ignored) {
        }


        final LocalDate finalFrom = from;
        final LocalDate finalTo = to;


        // =====================================================
        // CONTRIBUTION DATE FILTER
        // =====================================================

        contributions =
                contributions.stream()
                        .filter(c ->
                                isDateInRange(
                                        c.getContributionDate(),
                                        finalFrom,
                                        finalTo
                                ))
                        .collect(Collectors.toList());


        // =====================================================
        // EXPENSE DATE FILTER
        // =====================================================

        expenses =
                expenses.stream()
                        .filter(e ->
                                isDateInRange(
                                        e.getExpenseDate(),
                                        finalFrom,
                                        finalTo
                                ))
                        .collect(Collectors.toList());


        // =====================================================
        // BUSINESS DATE FILTER
        // =====================================================

        businessFunds =
                businessFunds.stream()
                        .filter(b ->
                                isDateInRange(
                                        b.getTransactionDate(),
                                        finalFrom,
                                        finalTo
                                ))
                        .collect(Collectors.toList());


        // =====================================================
        // EMERGENCY DATE FILTER
        // =====================================================

        emergencyTransactions =
                emergencyTransactions.stream()
                        .filter(e ->
                                isDateInRange(
                                        e.getTransactionDate(),
                                        finalFrom,
                                        finalTo
                                ))
                        .collect(Collectors.toList());


        // =====================================================
        // SEARCH FILTER
        // =====================================================

        if (search != null
                && !search.trim().isEmpty()) {

            String keyword =
                    search.trim().toLowerCase();


            // -------------------------------------------------
            // CONTRIBUTIONS
            // -------------------------------------------------

            contributions =
                    contributions.stream()
                            .filter(c ->

                                    contains(
                                            c.getMemberCode(),
                                            keyword
                                    )

                                    || contains(
                                            c.getMemberName(),
                                            keyword
                                    )

                                    || contains(
                                            c.getReceiptNo(),
                                            keyword
                                    )

                                    || contains(
                                            c.getRemarks(),
                                            keyword
                                    )

                            )
                            .collect(Collectors.toList());


            // -------------------------------------------------
            // EXPENSES
            // -------------------------------------------------

            expenses =
                    expenses.stream()
                            .filter(e ->

                                    contains(
                                            e.getExpenseNo(),
                                            keyword
                                    )

                                    || contains(
                                            e.getExpenseType(),
                                            keyword
                                    )

                                    || contains(
                                            e.getDescription(),
                                            keyword
                                    )

                                    || contains(
                                            e.getPaidTo(),
                                            keyword
                                    )

                                    || contains(
                                            e.getFundType(),
                                            keyword
                                    )

                                    || contains(
                                            e.getRemarks(),
                                            keyword
                                    )

                            )
                            .collect(Collectors.toList());


            // -------------------------------------------------
            // BUSINESS FUND
            // -------------------------------------------------

            businessFunds =
                    businessFunds.stream()
                            .filter(b ->

                                    contains(
                                            b.getTransactionNo(),
                                            keyword
                                    )

                                    || contains(
                                            b.getTransactionType(),
                                            keyword
                                    )

                                    || contains(
                                            b.getMemberCode(),
                                            keyword
                                    )

                                    || contains(
                                            b.getMemberName(),
                                            keyword
                                    )

                                    || contains(
                                            b.getDescription(),
                                            keyword
                                    )

                                    || contains(
                                            b.getRemarks(),
                                            keyword
                                    )

                            )
                            .collect(Collectors.toList());


            // -------------------------------------------------
            // EMERGENCY FUND
            // -------------------------------------------------

            emergencyTransactions =
                    emergencyTransactions.stream()
                            .filter(e ->

                                    contains(
                                            e.getTransactionNo(),
                                            keyword
                                    )

                                    || contains(
                                            e.getTransactionType(),
                                            keyword
                                    )

                                    || contains(
                                            e.getMemberCode(),
                                            keyword
                                    )

                                    || contains(
                                            e.getMemberName(),
                                            keyword
                                    )

                                    || contains(
                                            e.getDescription(),
                                            keyword
                                    )

                                    || contains(
                                            e.getPaidTo(),
                                            keyword
                                    )

                                    || contains(
                                            e.getRemarks(),
                                            keyword
                                    )

                            )
                            .collect(Collectors.toList());
        }


        // =====================================================
        // CREATE LEDGER ROWS
        // =====================================================

        List<LedgerRow> ledgerRows =
                new ArrayList<>();


        // =====================================================
        // CONTRIBUTION
        // MAIN CONTRIBUTION = CREDIT
        // =====================================================

        for (Contribution c : contributions) {

            double amount =
                    c.getAmount() != null
                            ? c.getAmount()
                            : 0.0;


            LedgerRow row =
                    new LedgerRow();


            row.setId(c.getId());

            row.setDate(
                    c.getContributionDate()
            );

            row.setReference(
                    c.getReceiptNo()
            );

            row.setType(
                    "Contribution"
            );

            row.setDescription(
                    "Member Contribution - "
                    + safe(c.getMemberName())
            );

            row.setMemberCode(
                    safe(c.getMemberCode())
            );

            row.setMemberName(
                    safe(c.getMemberName())
            );

            row.setFund(
                    "Contribution"
            );

            row.setCredit(amount);

            row.setDebit(0.0);

            row.setSource(
                    "CONTRIBUTION"
            );


            ledgerRows.add(row);
        }


        // =====================================================
        // EXPENSE
        // EXPENSE = DEBIT
        // =====================================================

        for (Expense e : expenses) {

            double amount =
                    e.getAmount() != null
                            ? e.getAmount()
                            : 0.0;


            LedgerRow row =
                    new LedgerRow();


            row.setId(e.getId());

            row.setDate(
                    e.getExpenseDate()
            );

            row.setReference(
                    e.getExpenseNo()
            );

            row.setType(
                    "Expense"
            );

            row.setDescription(
                    safe(e.getDescription())
            );

            row.setMemberCode("");

            row.setMemberName(
                    safe(e.getPaidTo())
            );

            row.setFund(
                    safe(e.getFundType())
            );

            row.setCredit(0.0);

            row.setDebit(amount);

            row.setSource(
                    "EXPENSE"
            );


            ledgerRows.add(row);
        }


        // =====================================================
        // BUSINESS FUND
        // =====================================================

        for (BusinessFund b : businessFunds) {

            double amount =
                    b.getAmount() != null
                            ? b.getAmount()
                            : 0.0;


            String type =
                    safe(b.getTransactionType());


            /*
             * Contribution allocation is already included
             * in the main Contribution transaction.
             *
             * Therefore it must NOT be counted again.
             */

            if ("Contribution".equalsIgnoreCase(type)) {
                continue;
            }


            LedgerRow row =
                    new LedgerRow();


            row.setId(b.getId());

            row.setDate(
                    b.getTransactionDate()
            );

            row.setReference(
                    b.getTransactionNo()
            );

            row.setType(
                    "Business Fund - " + type
            );

            row.setDescription(
                    safe(b.getDescription())
            );

            row.setMemberCode(
                    safe(b.getMemberCode())
            );

            row.setMemberName(
                    safe(b.getMemberName())
            );

            row.setFund(
                    "Business Fund"
            );


            // Business Income = Credit
            if ("Business Income"
                    .equalsIgnoreCase(type)) {

                row.setCredit(amount);
                row.setDebit(0.0);
            }


            // Profit = Credit
            else if ("Profit"
                    .equalsIgnoreCase(type)) {

                row.setCredit(amount);
                row.setDebit(0.0);
            }


            // Withdrawal = Debit
            else if ("Withdrawal"
                    .equalsIgnoreCase(type)) {

                row.setCredit(0.0);
                row.setDebit(amount);
            }


            // Other Business transaction = Credit
            else {

                row.setCredit(amount);
                row.setDebit(0.0);
            }


            row.setSource(
                    "BUSINESS_FUND"
            );


            ledgerRows.add(row);
        }


        // =====================================================
        // EMERGENCY FUND
        // =====================================================

        for (EmergencyFundTransaction e
                : emergencyTransactions) {

            double amount =
                    e.getAmount() != null
                            ? e.getAmount()
                            : 0.0;


            String type =
                    safe(e.getTransactionType());


            /*
             * Contribution allocation is already included
             * in the main Contribution transaction.
             *
             * Therefore it must NOT be counted again.
             */

            if ("Contribution".equalsIgnoreCase(type)) {
                continue;
            }


            LedgerRow row =
                    new LedgerRow();


            row.setId(e.getId());

            row.setDate(
                    e.getTransactionDate()
            );

            row.setReference(
                    e.getTransactionNo()
            );

            row.setType(
                    "Emergency Fund - " + type
            );

            row.setDescription(
                    safe(e.getDescription())
            );

            row.setMemberCode(
                    safe(e.getMemberCode())
            );

            row.setMemberName(
                    safe(e.getMemberName())
            );

            row.setFund(
                    "Emergency Fund"
            );


            // Withdrawal = Debit
            if ("Withdrawal"
                    .equalsIgnoreCase(type)) {

                row.setCredit(0.0);

                row.setDebit(amount);
            }


            // Income = Credit
            else {

                row.setCredit(amount);

                row.setDebit(0.0);
            }


            row.setSource(
                    "EMERGENCY_FUND"
            );


            ledgerRows.add(row);
        }


        // =====================================================
        // FUND FILTER
        // =====================================================

        if (fundType != null
                && !fundType.trim().isEmpty()) {

            String selectedFund =
                    fundType.trim();


            ledgerRows =
                    ledgerRows.stream()
                            .filter(row ->
                                    selectedFund.equalsIgnoreCase(
                                            safe(row.getFund())
                                    ))
                            .collect(Collectors.toList());
        }


        // =====================================================
        // SORT BY DATE
        // =====================================================

        ledgerRows.sort(
                Comparator.comparing(
                        LedgerRow::getDate,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()
                        )
                )
        );


        // =====================================================
        // RUNNING BALANCE
        // =====================================================

        double runningBalance = 0.0;

        double totalCredit = 0.0;

        double totalDebit = 0.0;


        for (LedgerRow row : ledgerRows) {

            double credit =
                    row.getCredit() != null
                            ? row.getCredit()
                            : 0.0;

            double debit =
                    row.getDebit() != null
                            ? row.getDebit()
                            : 0.0;


            totalCredit += credit;

            totalDebit += debit;


            runningBalance =
                    runningBalance
                    + credit
                    - debit;


            row.setBalance(
                    runningBalance
            );
        }


        // =====================================================
        // MEMBER LIST
        // =====================================================

        List<LedgerMember> members =
                contributionRepository
                        .findAll()
                        .stream()
                        .filter(c ->
                                c.getMemberCode() != null
                                && !c.getMemberCode()
                                        .trim()
                                        .isEmpty()
                        )
                        .map(c ->
                                new LedgerMember(
                                        c.getMemberCode(),
                                        c.getMemberName()
                                )
                        )
                        .collect(
                                Collectors.toMap(
                                        LedgerMember::getCode,
                                        m -> m,
                                        (a, b) -> a
                                )
                        )
                        .values()
                        .stream()
                        .sorted(
                                Comparator.comparing(
                                        LedgerMember::getCode
                                )
                        )
                        .collect(
                                Collectors.toList()
                        );


        // =====================================================
        // MODEL
        // =====================================================

        model.addAttribute(
                "ledgerRows",
                ledgerRows
        );

        model.addAttribute(
                "members",
                members
        );

        model.addAttribute(
                "selectedMember",
                memberCode
        );

        model.addAttribute(
                "search",
                search
        );

        model.addAttribute(
                "selectedFund",
                fundType
        );

        model.addAttribute(
                "fromDate",
                fromDate
        );

        model.addAttribute(
                "toDate",
                toDate
        );

        model.addAttribute(
                "totalCredit",
                totalCredit
        );

        model.addAttribute(
                "totalDebit",
                totalDebit
        );

        model.addAttribute(
                "closingBalance",
                runningBalance
        );


        return "ledger";
    }


    // =========================================================
    // DATE CHECK
    // =========================================================

    private static boolean isDateInRange(
            LocalDate date,
            LocalDate from,
            LocalDate to) {

        if (date == null) {
            return false;
        }

        if (from != null
                && date.isBefore(from)) {

            return false;
        }

        if (to != null
                && date.isAfter(to)) {

            return false;
        }

        return true;
    }


    // =========================================================
    // CONTAINS
    // =========================================================

    private static boolean contains(
            String value,
            String keyword) {

        return value != null
                && value.toLowerCase()
                        .contains(
                                keyword.toLowerCase()
                        );
    }


    // =========================================================
    // SAFE
    // =========================================================

    private static String safe(
            String value) {

        return value != null
                ? value
                : "";
    }


    // =========================================================
    // LEDGER ROW
    // =========================================================

    public static class LedgerRow {

        private Long id;

        private LocalDate date;

        private String reference;

        private String type;

        private String description;

        private String memberCode;

        private String memberName;

        private String fund;

        private Double debit;

        private Double credit;

        private Double balance;

        private String source;


        public LedgerRow() {
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }


        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }


        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }


        public String getMemberCode() {
            return memberCode;
        }

        public void setMemberCode(String memberCode) {
            this.memberCode = memberCode;
        }


        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }


        public String getFund() {
            return fund;
        }

        public void setFund(String fund) {
            this.fund = fund;
        }


        public Double getDebit() {
            return debit;
        }

        public void setDebit(Double debit) {
            this.debit = debit;
        }


        public Double getCredit() {
            return credit;
        }

        public void setCredit(Double credit) {
            this.credit = credit;
        }


        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }


        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }


    // =========================================================
    // LEDGER MEMBER
    // =========================================================

    public static class LedgerMember {

        private String code;

        private String name;


        public LedgerMember(
                String code,
                String name) {

            this.code = code;
            this.name = name;
        }


        public String getCode() {
            return code;
        }


        public String getName() {
            return name;
        }
    }

}