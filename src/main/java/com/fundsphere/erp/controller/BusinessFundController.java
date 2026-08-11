package com.fundsphere.erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fundsphere.erp.entity.BusinessFund;
import com.fundsphere.erp.repository.BusinessFundRepository;
import com.fundsphere.erp.service.BusinessFundService;

@Controller
public class BusinessFundController {

    @Autowired
    private BusinessFundRepository businessFundRepository;

    @Autowired
    private BusinessFundService businessFundService;


    // =========================================================
    // BUSINESS FUND MAIN PAGE
    // =========================================================

    @GetMapping("/business-fund")
    public String businessFund(Model model) {

        List<BusinessFund> records =
                businessFundRepository.findAll();

        double totalIncome = 0.0;
        double totalProfit = 0.0;
        double totalWithdrawal = 0.0;


        // =====================================================
        // CALCULATE BUSINESS FUND
        // =====================================================

        for (BusinessFund record : records) {

            double amount =
                    record.getAmount() != null
                            ? record.getAmount()
                            : 0.0;

            String type =
                    record.getTransactionType();


            // -------------------------------------------------
            // BUSINESS INCOME ONLY
            // Contribution Allocation is NOT counted here.
            // -------------------------------------------------

            if ("Business Income".equalsIgnoreCase(type)) {

                totalIncome += amount;
            }


            // -------------------------------------------------
            // PROFIT
            // -------------------------------------------------

            else if ("Profit".equalsIgnoreCase(type)) {

                totalProfit += amount;
            }


            // -------------------------------------------------
            // WITHDRAWAL
            // -------------------------------------------------

            else if ("Withdrawal".equalsIgnoreCase(type)) {

                totalWithdrawal += amount;
            }
        }


        // =====================================================
        // CURRENT BUSINESS FUND BALANCE
        // =====================================================

        double currentBalance =
                totalIncome
                + totalProfit
                - totalWithdrawal;


        // =====================================================
        // SEND DATA TO PAGE
        // =====================================================

        model.addAttribute(
                "businessFunds",
                records
        );

        model.addAttribute(
                "totalRecords",
                records.size()
        );

        model.addAttribute(
                "totalIncome",
                totalIncome
        );

        model.addAttribute(
                "totalProfit",
                totalProfit
        );

        model.addAttribute(
                "totalWithdrawal",
                totalWithdrawal
        );

        model.addAttribute(
                "currentBalance",
                currentBalance
        );


        return "business-fund";
    }


    // =========================================================
    // ADD BUSINESS FUND
    // =========================================================

    @GetMapping("/business-fund/new")
    public String newBusinessFund(Model model) {

        model.addAttribute(
                "businessFund",
                new BusinessFund()
        );

        model.addAttribute(
                "availableBalance",
                businessFundService
                        .getCurrentBalance()
        );

        return "business-fund-form";
    }


    // =========================================================
    // SAVE / UPDATE
    // =========================================================

    @PostMapping("/business-fund/save")
    public String saveBusinessFund(
            @ModelAttribute BusinessFund businessFund,
            Model model) {

        try {

            businessFundService.saveBusinessFund(
                    businessFund
            );

            return "redirect:/business-fund";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "businessFund",
                    businessFund
            );

            model.addAttribute(
                    "availableBalance",
                    businessFundService
                            .getCurrentBalance(
                                    businessFund.getId()
                            )
            );

            model.addAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            return "business-fund-form";
        }
    }


    // =========================================================
    // VIEW
    // =========================================================

    @GetMapping("/business-fund/view/{id}")
    public String viewBusinessFund(
            @PathVariable Long id,
            Model model) {

        BusinessFund businessFund =
                businessFundRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Business Fund transaction not found"
                                )
                        );

        model.addAttribute(
                "businessFund",
                businessFund
        );

        return "business-fund-view";
    }


    // =========================================================
    // EDIT
    // =========================================================

    @GetMapping("/business-fund/edit/{id}")
    public String editBusinessFund(
            @PathVariable Long id,
            Model model) {

        BusinessFund businessFund =
                businessFundRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Business Fund transaction not found"
                                )
                        );

        model.addAttribute(
                "businessFund",
                businessFund
        );

        model.addAttribute(
                "availableBalance",
                businessFundService
                        .getCurrentBalance(id)
        );

        return "business-fund-form";
    }


    // =========================================================
    // DELETE
    // =========================================================

    @GetMapping("/business-fund/delete/{id}")
    public String deleteBusinessFund(
            @PathVariable Long id) {

        if (businessFundRepository.existsById(id)) {

            businessFundRepository.deleteById(id);
        }

        return "redirect:/business-fund";
    }

}