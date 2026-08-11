package com.fundsphere.erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fundsphere.erp.entity.BusinessTransaction;
import com.fundsphere.erp.service.BusinessTransactionService;
import com.fundsphere.erp.service.MemberBusinessService;
import com.fundsphere.erp.service.MemberBusinessService.MemberBusinessSummary;

@Controller
public class BusinessTransactionController {

    @Autowired
    private BusinessTransactionService businessTransactionService;

    @Autowired
    private MemberBusinessService memberBusinessService;


    // =====================================================
    // BUSINESS ACCOUNTING
    // =====================================================

    @GetMapping("/business-accounting")
    public String businessAccounting(Model model) {

        List<BusinessTransaction> transactions =
                businessTransactionService.findAll();


        // -------------------------------------------------
        // BUSINESS TOTALS
        // -------------------------------------------------

        double totalPurchases =
                businessTransactionService
                        .getTotalPurchases();

        double totalSales =
                businessTransactionService
                        .getTotalSales();

        double businessExpenses =
                businessTransactionService
                        .getTotalBusinessExpenses();

        double grossProfitLoss =
                businessTransactionService
                        .getGrossProfitLoss();

        double netProfitLoss =
                businessTransactionService
                        .getNetProfitLoss();


        // -------------------------------------------------
        // MEMBER-WISE BUSINESS RESULTS
        // -------------------------------------------------

        List<MemberBusinessSummary>
                memberBusinessSummary =
                memberBusinessService
                        .getMemberBusinessSummary();


        // -------------------------------------------------
        // MODEL
        // -------------------------------------------------

        model.addAttribute(
                "transactions",
                transactions
        );

        model.addAttribute(
                "totalPurchases",
                totalPurchases
        );

        model.addAttribute(
                "totalSales",
                totalSales
        );

        model.addAttribute(
                "businessExpenses",
                businessExpenses
        );

        model.addAttribute(
                "grossProfitLoss",
                grossProfitLoss
        );

        model.addAttribute(
                "netProfitLoss",
                netProfitLoss
        );

        model.addAttribute(
                "memberBusinessSummary",
                memberBusinessSummary
        );


        return "business-accounting";
    }


    // =====================================================
    // NEW TRANSACTION
    // =====================================================

    @GetMapping("/business-accounting/new")
    public String newTransaction(
            @RequestParam(required = false)
            String type,
            Model model) {

        BusinessTransaction transaction =
                new BusinessTransaction();


        if (type != null
                && !type.trim().isEmpty()) {

            transaction.setTransactionType(
                    type.trim().toUpperCase()
            );
        }


        model.addAttribute(
                "businessTransaction",
                transaction
        );


        return "business-transaction-form";
    }


    // =====================================================
    // SAVE
    // =====================================================

    @PostMapping("/business-accounting/save")
    public String saveTransaction(
            @ModelAttribute(
                    "businessTransaction")
            BusinessTransaction transaction,
            Model model) {

        try {

            businessTransactionService.save(
                    transaction
            );

            return "redirect:/business-accounting";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "businessTransaction",
                    transaction
            );

            model.addAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            return "business-transaction-form";
        }
    }


    // =====================================================
    // VIEW
    // =====================================================

    @GetMapping("/business-accounting/view/{id}")
    public String viewTransaction(
            @PathVariable Long id,
            Model model) {

        BusinessTransaction transaction =
                businessTransactionService
                        .findById(id);


        model.addAttribute(
                "businessTransaction",
                transaction
        );


        return "business-transaction-view";
    }


    // =====================================================
    // EDIT
    // =====================================================

    @GetMapping("/business-accounting/edit/{id}")
    public String editTransaction(
            @PathVariable Long id,
            Model model) {

        BusinessTransaction transaction =
                businessTransactionService
                        .findById(id);


        model.addAttribute(
                "businessTransaction",
                transaction
        );


        return "business-transaction-form";
    }


    // =====================================================
    // DELETE
    // =====================================================

    @GetMapping("/business-accounting/delete/{id}")
    public String deleteTransaction(
            @PathVariable Long id) {

        businessTransactionService.delete(id);

        return "redirect:/business-accounting";
    }

}