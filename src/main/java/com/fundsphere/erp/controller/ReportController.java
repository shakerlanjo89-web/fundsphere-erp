package com.fundsphere.erp.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public class ReportController {

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
    // REPORTS MAIN PAGE
    // =========================================================

    @GetMapping("/reports")
    public String reports(Model model) {

        model.addAttribute(
                "pageTitle",
                "Financial Reports"
        );

        return "reports";
    }


    // =========================================================
    // FINANCIAL SUMMARY
    // =========================================================

    @GetMapping("/reports/financial")
    public String financialReport(

            @RequestParam(required = false)
            String type,

            @RequestParam(required = false)
            String fromDate,

            @RequestParam(required = false)
            String toDate,

            Model model) {

        List<Contribution> contributions =
                contributionRepository.findAll();

        List<Expense> expenses =
                expenseRepository.findAll();

        List<BusinessFund> businessFunds =
                businessFundRepository.findAll();

        List<EmergencyFundTransaction>
                emergencyTransactions =
                emergencyFundTransactionRepository.findAll();


        LocalDate from = parseDate(fromDate);
        LocalDate to = parseDate(toDate);


        // =====================================================
        // DATE FILTER
        // =====================================================

        contributions =
                contributions.stream()
                        .filter(c ->
                                inDateRange(
                                        c.getContributionDate(),
                                        from,
                                        to
                                ))
                        .toList();


        expenses =
                expenses.stream()
                        .filter(e ->
                                inDateRange(
                                        e.getExpenseDate(),
                                        from,
                                        to
                                ))
                        .toList();


        businessFunds =
                businessFunds.stream()
                        .filter(b ->
                                inDateRange(
                                        b.getTransactionDate(),
                                        from,
                                        to
                                ))
                        .toList();


        emergencyTransactions =
                emergencyTransactions.stream()
                        .filter(e ->
                                inDateRange(
                                        e.getTransactionDate(),
                                        from,
                                        to
                                ))
                        .toList();


        // =====================================================
        // CONTRIBUTIONS
        // =====================================================

        double totalContributions = 0.0;

        for (Contribution c : contributions) {

            if (c.getAmount() != null) {

                totalContributions +=
                        c.getAmount();
            }
        }


        // =====================================================
        // EXPENSES
        // =====================================================

        double totalExpenses = 0.0;

        for (Expense e : expenses) {

            if (e.getAmount() != null) {

                totalExpenses +=
                        e.getAmount();
            }
        }


        // =====================================================
        // BUSINESS FUND
        // =====================================================

        double businessIncome = 0.0;
        double businessProfit = 0.0;
        double businessWithdrawal = 0.0;

        for (BusinessFund b : businessFunds) {

            double amount =
                    b.getAmount() != null
                            ? b.getAmount()
                            : 0.0;

            String transactionType =
                    b.getTransactionType() != null
                            ? b.getTransactionType()
                            : "";


            if ("Business Income"
                    .equalsIgnoreCase(transactionType)) {

                businessIncome += amount;

            } else if ("Profit"
                    .equalsIgnoreCase(transactionType)) {

                businessProfit += amount;

            } else if ("Withdrawal"
                    .equalsIgnoreCase(transactionType)) {

                businessWithdrawal += amount;
            }
        }


        double businessBalance =
                businessIncome
                + businessProfit
                - businessWithdrawal;


        // =====================================================
        // EMERGENCY FUND
        // =====================================================

        double emergencyIncome = 0.0;
        double emergencyWithdrawal = 0.0;

        for (EmergencyFundTransaction e
                : emergencyTransactions) {

            double amount =
                    e.getAmount() != null
                            ? e.getAmount()
                            : 0.0;

            String transactionType =
                    e.getTransactionType() != null
                            ? e.getTransactionType()
                            : "";


            if ("Income"
                    .equalsIgnoreCase(transactionType)) {

                emergencyIncome += amount;

            } else if ("Withdrawal"
                    .equalsIgnoreCase(transactionType)) {

                emergencyWithdrawal += amount;
            }
        }


        double emergencyBalance =
                emergencyIncome
                - emergencyWithdrawal;


        // =====================================================
        // OVERALL
        // =====================================================

        double totalIncome =
                totalContributions
                + businessIncome
                + businessProfit
                + emergencyIncome;


        double totalDebit =
                totalExpenses
                + businessWithdrawal
                + emergencyWithdrawal;


        double closingBalance =
                totalIncome
                - totalDebit;


        // =====================================================
        // MODEL
        // =====================================================

        model.addAttribute(
                "contributions",
                contributions
        );

        model.addAttribute(
                "expenses",
                expenses
        );

        model.addAttribute(
                "businessFunds",
                businessFunds
        );

        model.addAttribute(
                "emergencyTransactions",
                emergencyTransactions
        );

        model.addAttribute(
                "totalContributions",
                totalContributions
        );

        model.addAttribute(
                "totalExpenses",
                totalExpenses
        );

        model.addAttribute(
                "businessIncome",
                businessIncome
        );

        model.addAttribute(
                "businessProfit",
                businessProfit
        );

        model.addAttribute(
                "businessWithdrawal",
                businessWithdrawal
        );

        model.addAttribute(
                "businessBalance",
                businessBalance
        );

        model.addAttribute(
                "emergencyIncome",
                emergencyIncome
        );

        model.addAttribute(
                "emergencyWithdrawal",
                emergencyWithdrawal
        );

        model.addAttribute(
                "emergencyBalance",
                emergencyBalance
        );

        model.addAttribute(
                "totalIncome",
                totalIncome
        );

        model.addAttribute(
                "totalDebit",
                totalDebit
        );

        model.addAttribute(
                "closingBalance",
                closingBalance
        );

        model.addAttribute(
                "reportType",
                type
        );

        model.addAttribute(
                "fromDate",
                fromDate
        );

        model.addAttribute(
                "toDate",
                toDate
        );


        return "financial-report";
    }


    // =========================================================
    // CONTRIBUTION REPORT
    // =========================================================

    @GetMapping("/reports/contributions")
    public String contributionReport(Model model) {

        List<Contribution> contributions =
                contributionRepository.findAll();

        model.addAttribute(
                "contributions",
                contributions
        );

        return "contribution-report";
    }


    // =========================================================
    // EXPENSE REPORT
    // =========================================================

    @GetMapping("/reports/expenses")
    public String expenseReport(Model model) {

        List<Expense> expenses =
                expenseRepository.findAll();

        model.addAttribute(
                "expenses",
                expenses
        );

        return "expense-report";
    }


    // =========================================================
    // BUSINESS FUND REPORT
    // =========================================================

    @GetMapping("/reports/business-fund")
    public String businessFundReport(Model model) {

        List<BusinessFund> businessFunds =
                businessFundRepository.findAll();

        model.addAttribute(
                "businessFunds",
                businessFunds
        );

        return "business-fund-report";
    }


    // =========================================================
    // EMERGENCY FUND REPORT
    // =========================================================

    @GetMapping("/reports/emergency-fund")
    public String emergencyFundReport(Model model) {

        List<EmergencyFundTransaction>
                transactions =
                emergencyFundTransactionRepository
                        .findAll();


        /*
         * Emergency contribution allocation comes
         * from Contribution.emergencyAmount.
         *
         * It is NOT treated as another income transaction.
         */

        double totalIncome = 0.0;

        List<Contribution> contributions =
                contributionRepository.findAll();


        for (Contribution c : contributions) {

            if (c.getEmergencyAmount() != null) {

                totalIncome +=
                        c.getEmergencyAmount();
            }
        }


        model.addAttribute(
                "transactions",
                transactions
        );

        model.addAttribute(
                "totalIncome",
                totalIncome
        );


        return "emergency-fund-report";
    }


    // =========================================================
    // MEMBER-WISE COMPLETE REPORT
    // =========================================================

    @GetMapping("/reports/member")
    public String memberReport(

            @RequestParam(required = false)
            String memberCode,

            Model model) {


        // =====================================================
        // ALL RECORDS
        // =====================================================

        List<Contribution> contributions =
                contributionRepository.findAll();

        List<BusinessFund> businessFunds =
                businessFundRepository.findAll();

        List<EmergencyFundTransaction>
                emergencyTransactions =
                emergencyFundTransactionRepository
                        .findAll();


        // =====================================================
        // MEMBER OPTIONS
        // =====================================================

        Map<String, String> memberOptions =
                new LinkedHashMap<>();


        for (Contribution c : contributions) {

            String code =
                    c.getMemberCode();


            if (code != null
                    && !code.trim().isEmpty()) {

                String name =
                        c.getMemberName() != null
                                ? c.getMemberName()
                                : "";


                memberOptions.put(
                        code,
                        name
                );
            }
        }


        // =====================================================
        // BUSINESS MEMBERS
        // =====================================================

        for (BusinessFund b : businessFunds) {

            String code =
                    b.getMemberCode();


            if (code != null
                    && !code.trim().isEmpty()
                    && !memberOptions.containsKey(code)) {

                String name =
                        b.getMemberName() != null
                                ? b.getMemberName()
                                : "";


                memberOptions.put(
                        code,
                        name
                );
            }
        }


        // =====================================================
        // EMERGENCY MEMBERS
        // =====================================================

        for (EmergencyFundTransaction e
                : emergencyTransactions) {

            String code =
                    e.getMemberCode();


            if (code != null
                    && !code.trim().isEmpty()
                    && !memberOptions.containsKey(code)) {

                String name =
                        e.getMemberName() != null
                                ? e.getMemberName()
                                : "";


                memberOptions.put(
                        code,
                        name
                );
            }
        }


        // =====================================================
        // SELECTED MEMBER
        // =====================================================

        String selectedCode =
                memberCode != null
                        ? memberCode.trim()
                        : "";


        if (!selectedCode.isEmpty()) {

            String code =
                    selectedCode;


            contributions =
                    contributions.stream()
                            .filter(c ->
                                    c.getMemberCode() != null
                                    && c.getMemberCode()
                                            .equalsIgnoreCase(code)
                            )
                            .toList();


            businessFunds =
                    businessFunds.stream()
                            .filter(b ->
                                    b.getMemberCode() != null
                                    && b.getMemberCode()
                                            .equalsIgnoreCase(code)
                            )
                            .toList();


            emergencyTransactions =
                    emergencyTransactions.stream()
                            .filter(e ->
                                    e.getMemberCode() != null
                                    && e.getMemberCode()
                                            .equalsIgnoreCase(code)
                            )
                            .toList();
        }


        // =====================================================
        // MEMBER NAME
        // =====================================================

        String selectedMemberName = "";


        if (!selectedCode.isEmpty()
                && memberOptions.containsKey(
                        selectedCode)) {

            selectedMemberName =
                    memberOptions.get(
                            selectedCode
                    );
        }


        // =====================================================
        // MODEL
        // =====================================================

        model.addAttribute(
                "contributions",
                contributions
        );

        model.addAttribute(
                "businessFunds",
                businessFunds
        );

        model.addAttribute(
                "emergencyTransactions",
                emergencyTransactions
        );

        model.addAttribute(
                "memberOptions",
                memberOptions
        );

        model.addAttribute(
                "selectedMemberCode",
                selectedCode
        );

        model.addAttribute(
                "selectedMemberName",
                selectedMemberName
        );


        return "member-report";
    }


    // =========================================================
    // DAILY REPORT
    // =========================================================

    @GetMapping("/reports/daily")
    public String dailyReport(

            @RequestParam(required = false)
            String date,

            Model model) {


        LocalDate selectedDate =
                parseDate(date);


        // =====================================================
        // CONTRIBUTIONS
        // =====================================================

        List<Contribution> contributions =
                contributionRepository.findAll()
                        .stream()
                        .filter(c ->
                                selectedDate == null
                                || selectedDate.equals(
                                        c.getContributionDate()
                                ))
                        .toList();


        // =====================================================
        // EXPENSES
        // =====================================================

        List<Expense> expenses =
                expenseRepository.findAll()
                        .stream()
                        .filter(e ->
                                selectedDate == null
                                || selectedDate.equals(
                                        e.getExpenseDate()
                                ))
                        .toList();


        // =====================================================
        // BUSINESS
        // =====================================================

        List<BusinessFund> businessFunds =
                businessFundRepository.findAll()
                        .stream()
                        .filter(b ->
                                selectedDate == null
                                || selectedDate.equals(
                                        b.getTransactionDate()
                                ))
                        .toList();


        // =====================================================
        // EMERGENCY
        // =====================================================

        List<EmergencyFundTransaction>
                emergencyTransactions =
                emergencyFundTransactionRepository
                        .findAll()
                        .stream()
                        .filter(e ->
                                selectedDate == null
                                || selectedDate.equals(
                                        e.getTransactionDate()
                                ))
                        .toList();


        // =====================================================
        // MODEL
        // =====================================================

        model.addAttribute(
                "selectedDate",
                date
        );

        model.addAttribute(
                "contributions",
                contributions
        );

        model.addAttribute(
                "expenses",
                expenses
        );

        model.addAttribute(
                "businessFunds",
                businessFunds
        );

        model.addAttribute(
                "emergencyTransactions",
                emergencyTransactions
        );


        return "daily-report";
    }


    // =========================================================
    // MONTHLY REPORT
    // =========================================================

    @GetMapping("/reports/monthly")
    public String monthlyReport(

            @RequestParam(required = false)
            Integer year,

            @RequestParam(required = false)
            Integer month,

            Model model) {


        // =====================================================
        // SELECTED MONTH
        // =====================================================

        LocalDate now =
                LocalDate.now();


        int selectedYear =
                year != null
                        ? year
                        : now.getYear();


        int selectedMonth =
                month != null
                        ? month
                        : now.getMonthValue();


        // =====================================================
        // ALL RECORDS
        // =====================================================

        List<Contribution> allContributions =
                contributionRepository.findAll();


        List<Expense> allExpenses =
                expenseRepository.findAll();


        List<BusinessFund> allBusinessFunds =
                businessFundRepository.findAll();


        List<EmergencyFundTransaction>
                allEmergencyTransactions =
                emergencyFundTransactionRepository
                        .findAll();


        // =====================================================
        // MONTH FILTER
        // =====================================================

        List<Contribution> contributions =
                allContributions.stream()
                        .filter(c ->
                                c.getContributionDate() != null
                                && c.getContributionDate()
                                        .getYear()
                                        == selectedYear
                                && c.getContributionDate()
                                        .getMonthValue()
                                        == selectedMonth
                        )
                        .toList();


        List<Expense> expenses =
                allExpenses.stream()
                        .filter(e ->
                                e.getExpenseDate() != null
                                && e.getExpenseDate()
                                        .getYear()
                                        == selectedYear
                                && e.getExpenseDate()
                                        .getMonthValue()
                                        == selectedMonth
                        )
                        .toList();


        List<BusinessFund> businessFunds =
                allBusinessFunds.stream()
                        .filter(b ->
                                b.getTransactionDate() != null
                                && b.getTransactionDate()
                                        .getYear()
                                        == selectedYear
                                && b.getTransactionDate()
                                        .getMonthValue()
                                        == selectedMonth
                        )
                        .toList();


        List<EmergencyFundTransaction>
                emergencyTransactions =
                allEmergencyTransactions.stream()
                        .filter(e ->
                                e.getTransactionDate() != null
                                && e.getTransactionDate()
                                        .getYear()
                                        == selectedYear
                                && e.getTransactionDate()
                                        .getMonthValue()
                                        == selectedMonth
                        )
                        .toList();


        // =====================================================
        // TOTAL CONTRIBUTIONS
        // =====================================================

        double totalContributions = 0.0;


        for (Contribution c : contributions) {

            if (c.getAmount() != null) {

                totalContributions +=
                        c.getAmount();
            }
        }


        // =====================================================
        // TOTAL EXPENSES
        // =====================================================

        double totalExpenses = 0.0;


        for (Expense e : expenses) {

            if (e.getAmount() != null) {

                totalExpenses +=
                        e.getAmount();
            }
        }


        // =====================================================
        // CONTRIBUTION FUND ALLOCATIONS
        // =====================================================

        double businessContribution = 0.0;

        double emergencyContribution = 0.0;

        double educationContribution = 0.0;

        double welfareContribution = 0.0;

        double administrationContribution = 0.0;


        for (Contribution c : contributions) {

            if (c.getBusinessAmount() != null) {

                businessContribution +=
                        c.getBusinessAmount();
            }


            if (c.getEmergencyAmount() != null) {

                emergencyContribution +=
                        c.getEmergencyAmount();
            }


            if (c.getEducationAmount() != null) {

                educationContribution +=
                        c.getEducationAmount();
            }


            if (c.getWelfareAmount() != null) {

                welfareContribution +=
                        c.getWelfareAmount();
            }


            if (c.getAdministrationAmount() != null) {

                administrationContribution +=
                        c.getAdministrationAmount();
            }
        }


        // =====================================================
        // BUSINESS TRANSACTIONS
        // =====================================================

        double businessIncome = 0.0;

        double businessProfit = 0.0;

        double businessWithdrawal = 0.0;


        for (BusinessFund b : businessFunds) {

            double amount =
                    b.getAmount() != null
                            ? b.getAmount()
                            : 0.0;


            String transactionType =
                    b.getTransactionType() != null
                            ? b.getTransactionType()
                            : "";


            if ("Business Income"
                    .equalsIgnoreCase(transactionType)) {

                businessIncome += amount;

            } else if ("Profit"
                    .equalsIgnoreCase(transactionType)) {

                businessProfit += amount;

            } else if ("Withdrawal"
                    .equalsIgnoreCase(transactionType)) {

                businessWithdrawal += amount;
            }
        }


        // =====================================================
        // BUSINESS BALANCE
        //
        // Contribution Allocation
        // + Business Income
        // + Profit
        // - Withdrawal
        // =====================================================

        double businessBalance =
                businessContribution
                + businessIncome
                + businessProfit
                - businessWithdrawal;


        // =====================================================
        // EMERGENCY TRANSACTIONS
        // =====================================================

        double emergencyIncome = 0.0;

        double emergencyWithdrawal = 0.0;


        for (EmergencyFundTransaction e
                : emergencyTransactions) {

            double amount =
                    e.getAmount() != null
                            ? e.getAmount()
                            : 0.0;


            String transactionType =
                    e.getTransactionType() != null
                            ? e.getTransactionType()
                            : "";


            if ("Income"
                    .equalsIgnoreCase(transactionType)) {

                emergencyIncome += amount;

            } else if ("Withdrawal"
                    .equalsIgnoreCase(transactionType)) {

                emergencyWithdrawal += amount;
            }
        }


        // =====================================================
        // EMERGENCY BALANCE
        //
        // Contribution Allocation
        // + Emergency Income
        // - Withdrawal
        // =====================================================

        double emergencyBalance =
                emergencyContribution
                + emergencyIncome
                - emergencyWithdrawal;


        // =====================================================
        // TOTAL FUND ALLOCATION
        // =====================================================

        double totalFundAllocation =
                businessContribution
                + emergencyContribution
                + educationContribution
                + welfareContribution
                + administrationContribution;


        // =====================================================
        // TOTAL REAL INCOME
        //
        // Contribution itself is income.
        // Fund allocations are NOT additional income.
        // =====================================================

        double totalIncome =
                totalContributions
                + businessIncome
                + businessProfit
                + emergencyIncome;


        // =====================================================
        // TOTAL DEBIT
        // =====================================================

        double totalDebit =
                totalExpenses
                + businessWithdrawal
                + emergencyWithdrawal;


        // =====================================================
        // MONTHLY CLOSING BALANCE
        // =====================================================

        double closingBalance =
                totalIncome
                - totalDebit;


        // =====================================================
        // TOTAL CURRENT FUND POSITION
        // =====================================================

        double totalFundPosition =
                businessBalance
                + emergencyBalance
                + educationContribution
                + welfareContribution
                + administrationContribution;


        // =====================================================
        // MONTH START / END
        // =====================================================

        LocalDate firstDay =
                LocalDate.of(
                        selectedYear,
                        selectedMonth,
                        1
                );


        LocalDate lastDay =
                firstDay.withDayOfMonth(
                        firstDay.lengthOfMonth()
                );


        // =====================================================
        // BASIC MODEL
        // =====================================================

        model.addAttribute(
                "year",
                selectedYear
        );

        model.addAttribute(
                "month",
                selectedMonth
        );


        model.addAttribute(
                "firstDay",
                firstDay
        );

        model.addAttribute(
                "lastDay",
                lastDay
        );


        // =====================================================
        // RECORDS
        // =====================================================

        model.addAttribute(
                "contributions",
                contributions
        );

        model.addAttribute(
                "expenses",
                expenses
        );

        model.addAttribute(
                "businessFunds",
                businessFunds
        );

        model.addAttribute(
                "emergencyTransactions",
                emergencyTransactions
        );


        // =====================================================
        // TOTALS
        // =====================================================

        model.addAttribute(
                "totalContributions",
                totalContributions
        );

        model.addAttribute(
                "totalExpenses",
                totalExpenses
        );


        // =====================================================
        // FUND ALLOCATIONS
        // =====================================================

        model.addAttribute(
                "businessContribution",
                businessContribution
        );

        model.addAttribute(
                "emergencyContribution",
                emergencyContribution
        );

        model.addAttribute(
                "educationContribution",
                educationContribution
        );

        model.addAttribute(
                "welfareContribution",
                welfareContribution
        );

        model.addAttribute(
                "administrationContribution",
                administrationContribution
        );


        model.addAttribute(
                "totalFundAllocation",
                totalFundAllocation
        );


        // =====================================================
        // BUSINESS
        // =====================================================

        model.addAttribute(
                "businessIncome",
                businessIncome
        );

        model.addAttribute(
                "businessProfit",
                businessProfit
        );

        model.addAttribute(
                "businessWithdrawal",
                businessWithdrawal
        );

        model.addAttribute(
                "businessBalance",
                businessBalance
        );


        // =====================================================
        // EMERGENCY
        // =====================================================

        model.addAttribute(
                "emergencyIncome",
                emergencyIncome
        );

        model.addAttribute(
                "emergencyWithdrawal",
                emergencyWithdrawal
        );

        model.addAttribute(
                "emergencyBalance",
                emergencyBalance
        );


        // =====================================================
        // OVERALL
        // =====================================================

        model.addAttribute(
                "totalIncome",
                totalIncome
        );

        model.addAttribute(
                "totalDebit",
                totalDebit
        );

        model.addAttribute(
                "closingBalance",
                closingBalance
        );

        model.addAttribute(
                "totalFundPosition",
                totalFundPosition
        );


        return "monthly-report";
    }


    // =========================================================
    // DATE PARSER
    // =========================================================

    private LocalDate parseDate(String value) {

        try {

            if (value == null
                    || value.trim().isEmpty()) {

                return null;
            }


            return LocalDate.parse(
                    value
            );

        } catch (Exception e) {

            return null;
        }
    }


    // =========================================================
    // DATE RANGE
    // =========================================================

    private boolean inDateRange(

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

}