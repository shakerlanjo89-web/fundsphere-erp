package com.fundsphere.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundsphere.erp.service.MemberBusinessService.MemberBusinessSummary;

@Service
public class ProfitService {

    @Autowired
    private BusinessTransactionService businessTransactionService;

    @Autowired
    private MemberBusinessService memberBusinessService;


    // =====================================================
    // TOTAL SALES
    // =====================================================

    public double getTotalSales() {

        return businessTransactionService
                .getTotalSales();
    }


    // =====================================================
    // TOTAL PURCHASES
    // =====================================================

    public double getTotalPurchases() {

        return businessTransactionService
                .getTotalPurchases();
    }


    // =====================================================
    // COST OF GOODS SOLD
    //
    // Sales - Gross Profit = COGS
    // =====================================================

    public double getCostOfGoodsSold() {

        double sales =
                getTotalSales();

        double grossProfitLoss =
                getGrossProfitLoss();

        return sales - grossProfitLoss;
    }


    // =====================================================
    // GROSS PROFIT / LOSS
    // =====================================================

    public double getGrossProfitLoss() {

        return businessTransactionService
                .getGrossProfitLoss();
    }


    // =====================================================
    // BUSINESS EXPENSES
    // =====================================================

    public double getBusinessExpenses() {

        return businessTransactionService
                .getTotalBusinessExpenses();
    }


    // =====================================================
    // NET PROFIT / LOSS
    // =====================================================

    public double getNetProfitLoss() {

        return getGrossProfitLoss()
                - getBusinessExpenses();
    }


    // =====================================================
    // MEMBER-WISE PROFIT
    // =====================================================

    public List<MemberBusinessSummary>
    getMemberProfitSummary() {

        return memberBusinessService
                .getMemberBusinessSummary();
    }


    // =====================================================
    // MEMBER PROFIT
    // =====================================================

    public MemberBusinessSummary
    getMemberProfit(
            String memberCode) {

        return memberBusinessService
                .getMemberBusinessSummary(
                        memberCode
                );
    }


    // =====================================================
    // COMPLETE PROFIT SUMMARY
    // =====================================================

    public ProfitSummary
    getProfitSummary() {

        double totalSales =
                getTotalSales();

        double totalPurchases =
                getTotalPurchases();

        double costOfGoodsSold =
                getCostOfGoodsSold();

        double grossProfitLoss =
                getGrossProfitLoss();

        double businessExpenses =
                getBusinessExpenses();

        double netProfitLoss =
                getNetProfitLoss();


        return new ProfitSummary(

                totalSales,
                totalPurchases,
                costOfGoodsSold,
                grossProfitLoss,
                businessExpenses,
                netProfitLoss
        );
    }


    // =====================================================
    // SUMMARY CLASS
    // =====================================================

    public static class ProfitSummary {

        private double totalSales;

        private double totalPurchases;

        private double costOfGoodsSold;

        private double grossProfitLoss;

        private double businessExpenses;

        private double netProfitLoss;


        public ProfitSummary(
                double totalSales,
                double totalPurchases,
                double costOfGoodsSold,
                double grossProfitLoss,
                double businessExpenses,
                double netProfitLoss) {

            this.totalSales =
                    totalSales;

            this.totalPurchases =
                    totalPurchases;

            this.costOfGoodsSold =
                    costOfGoodsSold;

            this.grossProfitLoss =
                    grossProfitLoss;

            this.businessExpenses =
                    businessExpenses;

            this.netProfitLoss =
                    netProfitLoss;
        }


        public double getTotalSales() {

            return totalSales;
        }


        public double getTotalPurchases() {

            return totalPurchases;
        }


        public double getCostOfGoodsSold() {

            return costOfGoodsSold;
        }


        public double getGrossProfitLoss() {

            return grossProfitLoss;
        }


        public double getBusinessExpenses() {

            return businessExpenses;
        }


        public double getNetProfitLoss() {

            return netProfitLoss;
        }
    }
}