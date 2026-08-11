package com.fundsphere.erp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundsphere.erp.entity.BusinessTransaction;
import com.fundsphere.erp.entity.Contribution;
import com.fundsphere.erp.repository.BusinessTransactionRepository;
import com.fundsphere.erp.repository.ContributionRepository;

@Service
public class BusinessTransactionService {

    @Autowired
    private BusinessTransactionRepository repository;

    @Autowired
    private ContributionRepository contributionRepository;


    // =====================================================
    // FIND ALL
    // =====================================================

    public List<BusinessTransaction> findAll() {

        List<BusinessTransaction> records =
                repository.findAll();

        records.sort(
                Comparator.comparing(
                        BusinessTransaction::getTransactionDate,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()
                        )
                )
        );

        return records;
    }


    // =====================================================
    // FIND BY ID
    // =====================================================

    public BusinessTransaction findById(Long id) {

        return repository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Business transaction not found."
                        )
                );
    }


    // =====================================================
    // SAVE
    // =====================================================

    public BusinessTransaction save(
            BusinessTransaction transaction) {

        // -------------------------------------------------
        // DATE
        // -------------------------------------------------

        if (transaction.getTransactionDate() == null) {

            transaction.setTransactionDate(
                    LocalDate.now()
            );
        }


        // -------------------------------------------------
        // TYPE
        // -------------------------------------------------

        if (transaction.getTransactionType() == null
                || transaction.getTransactionType()
                        .trim()
                        .isEmpty()) {

            throw new IllegalArgumentException(
                    "Transaction Type is required."
            );
        }


        String type =
                transaction.getTransactionType()
                        .trim()
                        .toUpperCase();

        transaction.setTransactionType(type);


        // -------------------------------------------------
        // ITEM
        // -------------------------------------------------

        if (transaction.getItemName() == null
                || transaction.getItemName()
                        .trim()
                        .isEmpty()) {

            throw new IllegalArgumentException(
                    "Item name is required."
            );
        }


        // -------------------------------------------------
        // QUANTITY
        // -------------------------------------------------

        if (transaction.getQuantity() == null
                || transaction.getQuantity() <= 0) {

            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }


        // -------------------------------------------------
        // UNIT PRICE
        // -------------------------------------------------

        if (transaction.getUnitPrice() == null
                || transaction.getUnitPrice() < 0) {

            throw new IllegalArgumentException(
                    "Unit price cannot be negative."
            );
        }


        // -------------------------------------------------
        // TRANSACTION NUMBER
        // -------------------------------------------------

        if (transaction.getId() == null
                && (
                    transaction.getTransactionNo() == null
                    || transaction.getTransactionNo()
                            .trim()
                            .isEmpty()
                )) {

            transaction.setTransactionNo(
                    "BT-" + System.currentTimeMillis()
            );
        }


        // -------------------------------------------------
        // NORMALIZE MEMBER DATA
        // -------------------------------------------------

        if (transaction.getMemberCode() != null) {

            transaction.setMemberCode(
                    transaction.getMemberCode().trim()
            );
        }

        if (transaction.getMemberName() != null) {

            transaction.setMemberName(
                    transaction.getMemberName().trim()
            );
        }


        // =================================================
        // SALE STOCK VALIDATION
        // =================================================

        if ("SALE".equalsIgnoreCase(type)) {

            double availableStock =
                    getAvailableStock(
                            transaction.getItemName(),
                            transaction.getId()
                    );


            if (transaction.getQuantity()
                    > availableStock) {

                throw new IllegalArgumentException(
                        "Insufficient stock for item: "
                        + transaction.getItemName()
                        + ". Available stock: "
                        + availableStock
                        + " | Sale quantity: "
                        + transaction.getQuantity()
                );
            }
        }


        return repository.save(transaction);
    }


    // =====================================================
    // DELETE
    // =====================================================

    public void delete(Long id) {

        if (repository.existsById(id)) {

            repository.deleteById(id);
        }
    }


    // =====================================================
    // TOTAL PURCHASES
    // =====================================================

    public double getTotalPurchases() {

        double total = 0.0;

        for (BusinessTransaction t : findAll()) {

            if ("PURCHASE".equalsIgnoreCase(
                    t.getTransactionType()
            )) {

                total += getTotalAmount(t);
            }
        }

        return total;
    }


    // =====================================================
    // TOTAL SALES
    // =====================================================

    public double getTotalSales() {

        double total = 0.0;

        for (BusinessTransaction t : findAll()) {

            if ("SALE".equalsIgnoreCase(
                    t.getTransactionType()
            )) {

                total += getTotalAmount(t);
            }
        }

        return total;
    }


    // =====================================================
    // BUSINESS EXPENSES
    // =====================================================

    public double getTotalBusinessExpenses() {

        double total = 0.0;

        for (BusinessTransaction t : findAll()) {

            if ("EXPENSE".equalsIgnoreCase(
                    t.getTransactionType()
            )) {

                total += getTotalAmount(t);
            }
        }

        return total;
    }


    // =====================================================
    // GROSS PROFIT / LOSS
    //
    // SALES - ACTUAL COST OF GOODS SOLD
    // =====================================================

    public double getGrossProfitLoss() {

        List<BusinessTransaction> records =
                findAll();

        double grossProfit = 0.0;

        List<PurchaseStock> stock =
                new ArrayList<>();


        for (BusinessTransaction t : records) {

            String type =
                    t.getTransactionType();


            // ---------------------------------------------
            // PURCHASE
            // ---------------------------------------------

            if ("PURCHASE".equalsIgnoreCase(type)) {

                stock.add(
                        new PurchaseStock(
                                t.getItemName(),
                                t.getQuantity(),
                                t.getUnitPrice()
                        )
                );
            }


            // ---------------------------------------------
            // SALE
            // ---------------------------------------------

            else if ("SALE".equalsIgnoreCase(type)) {

                double saleAmount =
                        getTotalAmount(t);


                double cost =
                        calculateSaleCost(
                                stock,
                                t.getItemName(),
                                t.getQuantity()
                        );


                grossProfit +=
                        saleAmount - cost;
            }
        }


        return grossProfit;
    }


    // =====================================================
    // NET PROFIT / LOSS
    //
    // GROSS PROFIT - BUSINESS EXPENSES
    // =====================================================

    public double getNetProfitLoss() {

        return getGrossProfitLoss()
                - getTotalBusinessExpenses();
    }


    // =====================================================
    // TOTAL BUSINESS INVESTMENT
    //
    // IMPORTANT:
    // Investment comes from Contribution.businessAmount
    // =====================================================

    public double getTotalBusinessInvestment() {

        double total = 0.0;

        List<Contribution> contributions =
                contributionRepository.findAll();


        for (Contribution c : contributions) {

            if (c.getBusinessAmount() != null) {

                total += c.getBusinessAmount();
            }
        }


        return total;
    }


    // =====================================================
    // MEMBER BUSINESS INVESTMENT
    // =====================================================

    public double getMemberBusinessInvestment(
            String memberCode) {

        if (memberCode == null
                || memberCode.trim().isEmpty()) {

            return 0.0;
        }


        String code =
                memberCode.trim();


        double total = 0.0;


        List<Contribution> contributions =
                contributionRepository.findAll();


        for (Contribution c : contributions) {

            if (c.getMemberCode() == null) {
                continue;
            }


            if (!c.getMemberCode()
                    .trim()
                    .equalsIgnoreCase(code)) {

                continue;
            }


            if (c.getBusinessAmount() != null) {

                total += c.getBusinessAmount();
            }
        }


        return total;
    }


    // =====================================================
    // MEMBER INVESTMENT PERCENTAGE
    // =====================================================

    public double getMemberInvestmentPercent(
            String memberCode) {

        double totalInvestment =
                getTotalBusinessInvestment();


        if (totalInvestment <= 0) {

            return 0.0;
        }


        double memberInvestment =
                getMemberBusinessInvestment(
                        memberCode
                );


        return (
                memberInvestment
                / totalInvestment
        ) * 100.0;
    }


    // =====================================================
    // MEMBER GROSS PROFIT / LOSS
    //
    // Overall Gross Profit/Loss is distributed
    // according to business investment percentage.
    // =====================================================

    public double getMemberGrossProfitLoss(
            String memberCode) {

        double grossProfitLoss =
                getGrossProfitLoss();


        double investmentPercent =
                getMemberInvestmentPercent(
                        memberCode
                );


        return
                grossProfitLoss
                * investmentPercent
                / 100.0;
    }


    // =====================================================
    // MEMBER BUSINESS EXPENSE SHARE
    //
    // Business expenses are also distributed according
    // to member business investment percentage.
    // =====================================================

    public double getMemberBusinessExpenseShare(
            String memberCode) {

        double totalExpenses =
                getTotalBusinessExpenses();


        double investmentPercent =
                getMemberInvestmentPercent(
                        memberCode
                );


        return
                totalExpenses
                * investmentPercent
                / 100.0;
    }


    // =====================================================
    // MEMBER NET PROFIT / LOSS
    //
    // MEMBER GROSS PROFIT/LOSS
    // MINUS
    // MEMBER BUSINESS EXPENSE SHARE
    // =====================================================

    public double getMemberNetProfitLoss(
            String memberCode) {

        double memberGrossProfitLoss =
                getMemberGrossProfitLoss(
                        memberCode
                );


        double memberExpenseShare =
                getMemberBusinessExpenseShare(
                        memberCode
                );


        return
                memberGrossProfitLoss
                - memberExpenseShare;
    }


    // =====================================================
    // MEMBER BUSINESS SUMMARY
    // =====================================================

    public MemberBusinessSummary
            getMemberBusinessSummary(
                    String memberCode) {

        double investment =
                getMemberBusinessInvestment(
                        memberCode
                );


        double investmentPercent =
                getMemberInvestmentPercent(
                        memberCode
                );


        double grossProfitLoss =
                getMemberGrossProfitLoss(
                        memberCode
                );


        double expenseShare =
                getMemberBusinessExpenseShare(
                        memberCode
                );


        double netProfitLoss =
                getMemberNetProfitLoss(
                        memberCode
                );


        return new MemberBusinessSummary(
                investment,
                investmentPercent,
                grossProfitLoss,
                expenseShare,
                netProfitLoss
        );
    }


    // =====================================================
    // AVAILABLE STOCK
    // =====================================================

    public double getAvailableStock(
            String itemName,
            Long excludeId) {

        double stock = 0.0;


        for (BusinessTransaction t :
                findAll()) {


            if (excludeId != null
                    && excludeId.equals(
                            t.getId()
                    )) {

                continue;
            }


            if (t.getItemName() == null
                    || itemName == null) {

                continue;
            }


            if (!t.getItemName()
                    .trim()
                    .equalsIgnoreCase(
                            itemName.trim()
                    )) {

                continue;
            }


            double quantity =
                    t.getQuantity() != null
                            ? t.getQuantity()
                            : 0.0;


            if ("PURCHASE".equalsIgnoreCase(
                    t.getTransactionType()
            )) {

                stock += quantity;
            }


            else if ("SALE".equalsIgnoreCase(
                    t.getTransactionType()
            )) {

                stock -= quantity;
            }
        }


        return stock;
    }


    // =====================================================
    // TOTAL AMOUNT
    // =====================================================

    private double getTotalAmount(
            BusinessTransaction t) {

        double quantity =
                t.getQuantity() != null
                        ? t.getQuantity()
                        : 0.0;


        double unitPrice =
                t.getUnitPrice() != null
                        ? t.getUnitPrice()
                        : 0.0;


        return quantity * unitPrice;
    }


    // =====================================================
    // SALE COST
    //
    // FIFO PURCHASE COST
    // =====================================================

    private double calculateSaleCost(
            List<PurchaseStock> stock,
            String itemName,
            double saleQuantity) {

        double remaining =
                saleQuantity;

        double cost =
                0.0;


        for (PurchaseStock purchase :
                stock) {


            if (remaining <= 0) {
                break;
            }


            if (!purchase.itemName
                    .equalsIgnoreCase(
                            itemName
                    )) {

                continue;
            }


            if (purchase.quantity <= 0) {
                continue;
            }


            double used =
                    Math.min(
                            remaining,
                            purchase.quantity
                    );


            cost +=
                    used
                    * purchase.unitPrice;


            purchase.quantity -= used;

            remaining -= used;
        }


        return cost;
    }


    // =====================================================
    // INTERNAL PURCHASE STOCK
    // =====================================================

    private static class PurchaseStock {

        private String itemName;

        private double quantity;

        private double unitPrice;


        public PurchaseStock(
                String itemName,
                double quantity,
                double unitPrice) {

            this.itemName =
                    itemName != null
                            ? itemName.trim()
                            : "";

            this.quantity =
                    quantity;

            this.unitPrice =
                    unitPrice;
        }
    }


    // =====================================================
    // MEMBER BUSINESS SUMMARY
    // =====================================================

    public static class MemberBusinessSummary {

        private double investment;

        private double investmentPercent;

        private double grossProfitLoss;

        private double expenseShare;

        private double netProfitLoss;


        public MemberBusinessSummary(
                double investment,
                double investmentPercent,
                double grossProfitLoss,
                double expenseShare,
                double netProfitLoss) {

            this.investment =
                    investment;

            this.investmentPercent =
                    investmentPercent;

            this.grossProfitLoss =
                    grossProfitLoss;

            this.expenseShare =
                    expenseShare;

            this.netProfitLoss =
                    netProfitLoss;
        }


        public double getInvestment() {
            return investment;
        }


        public double getInvestmentPercent() {
            return investmentPercent;
        }


        public double getGrossProfitLoss() {
            return grossProfitLoss;
        }


        public double getExpenseShare() {
            return expenseShare;
        }


        public double getNetProfitLoss() {
            return netProfitLoss;
        }
    }

}