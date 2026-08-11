package com.fundsphere.erp.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundsphere.erp.entity.Contribution;
import com.fundsphere.erp.repository.ContributionRepository;

@Service
public class MemberBusinessService {

    @Autowired
    private ContributionRepository contributionRepository;

    @Autowired
    private BusinessTransactionService
            businessTransactionService;


    // =====================================================
    // MEMBER BUSINESS SUMMARY
    // =====================================================

    public List<MemberBusinessSummary>
    getMemberBusinessSummary() {


        List<Contribution> contributions =
                contributionRepository.findAll();


        /*
         * Member-wise investment
         *
         * Investment comes ONLY from
         * Contribution.businessAmount
         */

        Map<String, MemberBusinessSummary>
                memberMap =
                new LinkedHashMap<>();


        // =================================================
        // BUILD MEMBER INVESTMENT
        // =================================================

        for (Contribution c :
                contributions) {


            String memberCode =
                    c.getMemberCode();


            if (memberCode == null
                    || memberCode.trim().isEmpty()) {

                continue;
            }


            memberCode =
                    memberCode.trim();


            String memberName =
                    c.getMemberName() != null
                            ? c.getMemberName()
                            : "";


            double businessAmount =
                    c.getBusinessAmount() != null
                            ? c.getBusinessAmount()
                            : 0.0;


            MemberBusinessSummary summary =
                    memberMap.get(memberCode);


            if (summary == null) {

                summary =
                        new MemberBusinessSummary();

                summary.setMemberCode(
                        memberCode
                );

                summary.setMemberName(
                        memberName
                );

                summary.setInvestment(
                        0.0
                );

                memberMap.put(
                        memberCode,
                        summary
                );
            }


            /*
             * IMPORTANT:
             *
             * Only businessAmount is added.
             *
             * Full contribution is NOT
             * treated as business investment.
             */

            summary.setInvestment(
                    summary.getInvestment()
                    + businessAmount
            );
        }


        // =================================================
        // TOTAL BUSINESS INVESTMENT
        // =================================================

        double totalInvestment = 0.0;


        for (MemberBusinessSummary summary :
                memberMap.values()) {

            totalInvestment +=
                    summary.getInvestment();
        }


        // =================================================
        // BUSINESS RESULTS
        // =================================================

        double totalProfitLoss =
                businessTransactionService
                        .getGrossProfitLoss();


        double totalBusinessExpenses =
                businessTransactionService
                        .getTotalBusinessExpenses();


        /*
         * Net result after business expenses.
         */

        double totalNetProfitLoss =
                totalProfitLoss
                - totalBusinessExpenses;


        // =================================================
        // DISTRIBUTE MEMBER-WISE
        // =================================================

        for (MemberBusinessSummary summary :
                memberMap.values()) {


            double investment =
                    summary.getInvestment();


            double investmentPercent = 0.0;


            if (totalInvestment > 0) {

                investmentPercent =
                        (
                            investment
                            / totalInvestment
                        ) * 100.0;
            }


            // ---------------------------------------------
            // PROFIT / LOSS SHARE
            // ---------------------------------------------

            double profitLossShare = 0.0;


            if (totalInvestment > 0) {

                profitLossShare =
                        (
                            investment
                            / totalInvestment
                        )
                        * totalProfitLoss;
            }


            // ---------------------------------------------
            // BUSINESS EXPENSE SHARE
            // ---------------------------------------------

            double expenseShare = 0.0;


            if (totalInvestment > 0) {

                expenseShare =
                        (
                            investment
                            / totalInvestment
                        )
                        * totalBusinessExpenses;
            }


            // ---------------------------------------------
            // NET MEMBER RESULT
            //
            // Gross Profit/Loss
            // minus Expense Share
            // ---------------------------------------------

            double netProfitLoss =
                    profitLossShare
                    - expenseShare;


            summary.setInvestmentPercent(
                    investmentPercent
            );

            summary.setProfitLossShare(
                    profitLossShare
            );

            summary.setExpenseShare(
                    expenseShare
            );

            summary.setNetProfitLoss(
                    netProfitLoss
            );
        }


        // =================================================
        // SORT BY MEMBER CODE
        // =================================================

        List<MemberBusinessSummary>
                result =
                new ArrayList<>(
                        memberMap.values()
                );


        result.sort(
                Comparator.comparing(
                        MemberBusinessSummary
                                ::getMemberCode
                )
        );


        return result;
    }


    // =====================================================
    // FIND ONE MEMBER
    // =====================================================

    public MemberBusinessSummary
    getMemberBusinessSummary(
            String memberCode) {


        if (memberCode == null
                || memberCode.trim().isEmpty()) {

            return null;
        }


        String selectedCode =
                memberCode.trim();


        List<MemberBusinessSummary>
                summaries =
                getMemberBusinessSummary();


        for (MemberBusinessSummary summary :
                summaries) {


            if (summary.getMemberCode()
                    != null
                    && summary.getMemberCode()
                        .equalsIgnoreCase(
                                selectedCode
                        )) {

                return summary;
            }
        }


        return null;
    }


    // =====================================================
    // MEMBER SUMMARY CLASS
    // =====================================================

    public static class MemberBusinessSummary {

        private String memberCode;

        private String memberName;

        private double investment;

        private double investmentPercent;

        private double profitLossShare;

        private double expenseShare;

        private double netProfitLoss;


        // =============================================
        // MEMBER CODE
        // =============================================

        public String getMemberCode() {

            return memberCode;
        }

        public void setMemberCode(
                String memberCode) {

            this.memberCode =
                    memberCode;
        }


        // =============================================
        // MEMBER NAME
        // =============================================

        public String getMemberName() {

            return memberName;
        }

        public void setMemberName(
                String memberName) {

            this.memberName =
                    memberName;
        }


        // =============================================
        // INVESTMENT
        // =============================================

        public double getInvestment() {

            return investment;
        }

        public void setInvestment(
                double investment) {

            this.investment =
                    investment;
        }


        // =============================================
        // INVESTMENT %
        // =============================================

        public double getInvestmentPercent() {

            return investmentPercent;
        }

        public void setInvestmentPercent(
                double investmentPercent) {

            this.investmentPercent =
                    investmentPercent;
        }


        // =============================================
        // PROFIT / LOSS SHARE
        // =============================================

        public double getProfitLossShare() {

            return profitLossShare;
        }

        public void setProfitLossShare(
                double profitLossShare) {

            this.profitLossShare =
                    profitLossShare;
        }


        // =============================================
        // EXPENSE SHARE
        // =============================================

        public double getExpenseShare() {

            return expenseShare;
        }

        public void setExpenseShare(
                double expenseShare) {

            this.expenseShare =
                    expenseShare;
        }


        // =============================================
        // NET PROFIT / LOSS
        // =============================================

        public double getNetProfitLoss() {

            return netProfitLoss;
        }

        public void setNetProfitLoss(
                double netProfitLoss) {

            this.netProfitLoss =
                    netProfitLoss;
        }
    }

}