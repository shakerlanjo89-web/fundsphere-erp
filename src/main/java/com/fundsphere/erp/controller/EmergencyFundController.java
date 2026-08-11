package com.fundsphere.erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fundsphere.erp.entity.EmergencyFundTransaction;
import com.fundsphere.erp.repository.EmergencyFundTransactionRepository;
import com.fundsphere.erp.repository.MemberRepository;
import com.fundsphere.erp.service.EmergencyFundService;

@Controller
public class EmergencyFundController {

    @Autowired
    private EmergencyFundTransactionRepository transactionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmergencyFundService emergencyFundService;


    // =========================================================
    // EMERGENCY FUND MAIN PAGE
    // =========================================================

    @GetMapping("/emergency-fund")
    public String emergencyFund(Model model) {

        List<EmergencyFundTransaction> transactions =
                transactionRepository
                        .findAllByOrderByTransactionDateDesc();


        double totalIncome = 0.0;
        double totalWithdrawal = 0.0;


        // =====================================================
        // CALCULATE EMERGENCY FUND
        // =====================================================

        for (EmergencyFundTransaction transaction :
                transactions) {

            double amount =
                    transaction.getAmount() != null
                            ? transaction.getAmount()
                            : 0.0;

            String type =
                    transaction.getTransactionType();


            // -------------------------------------------------
            // ONLY REAL EMERGENCY INCOME
            // Contribution Allocation is NOT counted here.
            // -------------------------------------------------

            if ("Income".equalsIgnoreCase(type)) {

                totalIncome += amount;
            }


            // -------------------------------------------------
            // WITHDRAWAL
            // -------------------------------------------------

            else if ("Withdrawal".equalsIgnoreCase(type)) {

                totalWithdrawal += amount;
            }
        }


        // =====================================================
        // CURRENT EMERGENCY FUND BALANCE
        // =====================================================

        double currentBalance =
                totalIncome - totalWithdrawal;


        // =====================================================
        // SEND DATA TO PAGE
        // =====================================================

        model.addAttribute(
                "transactions",
                transactions
        );

        model.addAttribute(
                "totalIncome",
                totalIncome
        );

        model.addAttribute(
                "totalWithdrawal",
                totalWithdrawal
        );

        model.addAttribute(
                "currentBalance",
                currentBalance
        );

        model.addAttribute(
                "totalTransactions",
                transactions.size()
        );


        return "emergency-fund";
    }


    // =========================================================
    // ADD TRANSACTION
    // =========================================================

    @GetMapping("/emergency-fund/new")
    public String newTransaction(Model model) {

        model.addAttribute(
                "transaction",
                new EmergencyFundTransaction()
        );

        model.addAttribute(
                "members",
                memberRepository.findAll()
        );

        return "emergency-fund-form";
    }


    // =========================================================
    // SAVE / UPDATE TRANSACTION
    // =========================================================

    @PostMapping("/emergency-fund/save")
    public String saveTransaction(
            @ModelAttribute("transaction")
            EmergencyFundTransaction transaction,
            Model model) {

        try {

            emergencyFundService.saveTransaction(
                    transaction
            );

            return "redirect:/emergency-fund";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            model.addAttribute(
                    "transaction",
                    transaction
            );

            model.addAttribute(
                    "members",
                    memberRepository.findAll()
            );

            return "emergency-fund-form";
        }
    }


    // =========================================================
    // VIEW TRANSACTION
    // =========================================================

    @GetMapping("/emergency-fund/view/{id}")
    public String viewTransaction(
            @PathVariable Long id,
            Model model) {

        EmergencyFundTransaction transaction =
                transactionRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Emergency Fund transaction not found"
                                )
                        );

        model.addAttribute(
                "transaction",
                transaction
        );

        return "emergency-fund-view";
    }


    // =========================================================
    // EDIT TRANSACTION
    // =========================================================

    @GetMapping("/emergency-fund/edit/{id}")
    public String editTransaction(
            @PathVariable Long id,
            Model model) {

        EmergencyFundTransaction transaction =
                transactionRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Emergency Fund transaction not found"
                                )
                        );

        model.addAttribute(
                "transaction",
                transaction
        );

        model.addAttribute(
                "members",
                memberRepository.findAll()
        );

        return "emergency-fund-form";
    }


    // =========================================================
    // DELETE
    // =========================================================

    @GetMapping("/emergency-fund/delete/{id}")
    public String deleteTransaction(
            @PathVariable Long id) {

        emergencyFundService.deleteTransaction(
                id
        );

        return "redirect:/emergency-fund";
    }

}