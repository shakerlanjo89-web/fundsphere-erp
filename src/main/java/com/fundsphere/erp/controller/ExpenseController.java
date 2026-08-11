package com.fundsphere.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fundsphere.erp.entity.Expense;
import com.fundsphere.erp.repository.ExpenseRepository;
import com.fundsphere.erp.service.ExpenseService;

@Controller
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseService expenseService;


    // =========================================
    // EXPENSE LIST
    // =========================================

    @GetMapping("/expenses")
    public String expenses(Model model) {

        model.addAttribute(
                "expenses",
                expenseRepository.findAll()
        );

        return "expenses";
    }


    // =========================================
    // ADD EXPENSE
    // =========================================

    @GetMapping("/expense/new")
    public String newExpense(Model model) {

        model.addAttribute(
                "expense",
                new Expense()
        );

        return "expense-form";
    }


    // =========================================
    // SAVE / UPDATE EXPENSE
    // =========================================

    @PostMapping("/expense/save")
    public String saveExpense(
            @ModelAttribute Expense expense) {

        expenseService.saveExpense(expense);

        return "redirect:/expenses";
    }


    // =========================================
    // VIEW EXPENSE
    // =========================================

    @GetMapping("/expense/view/{id}")
    public String viewExpense(
            @PathVariable Long id,
            Model model) {

        Expense expense =
                expenseRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Expense not found"
                        )
                );

        model.addAttribute(
                "expense",
                expense
        );

        return "expense-view";
    }


    // =========================================
    // EDIT EXPENSE
    // =========================================

    @GetMapping("/expense/edit/{id}")
    public String editExpense(
            @PathVariable Long id,
            Model model) {

        Expense expense =
                expenseRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Expense not found"
                        )
                );

        model.addAttribute(
                "expense",
                expense
        );

        return "expense-form";
    }


    // =========================================
    // DELETE EXPENSE
    // =========================================

    @GetMapping("/expense/delete/{id}")
    public String deleteExpense(
            @PathVariable Long id) {

        if (expenseRepository.existsById(id)) {

            expenseRepository.deleteById(id);
        }

        return "redirect:/expenses";
    }

}