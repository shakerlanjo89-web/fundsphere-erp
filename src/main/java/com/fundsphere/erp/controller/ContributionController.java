package com.fundsphere.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fundsphere.erp.entity.Contribution;
import com.fundsphere.erp.repository.ContributionRepository;
import com.fundsphere.erp.repository.MemberRepository;
import com.fundsphere.erp.service.ContributionService;

@Controller
public class ContributionController {

    @Autowired
    private ContributionRepository contributionRepository;

    @Autowired
    private ContributionService contributionService;

    @Autowired
    private MemberRepository memberRepository;


    // =========================================================
    // CONTRIBUTION LIST
    // =========================================================

    @GetMapping("/contributions")
    public String contributions(Model model) {

        model.addAttribute(
                "contributions",
                contributionRepository.findAll()
        );

        return "contributions";
    }


    // =========================================================
    // ADD NEW CONTRIBUTION
    // =========================================================

    @GetMapping("/contribution/new")
    public String newContribution(Model model) {

        model.addAttribute(
                "contribution",
                new Contribution()
        );

        model.addAttribute(
                "members",
                memberRepository.findAll()
        );

        return "contribution-form";
    }


    // =========================================================
    // SAVE CONTRIBUTION
    // =========================================================

    @PostMapping("/contribution/save")
    public String saveContribution(
            @ModelAttribute Contribution contribution) {

        contributionService.saveContribution(
                contribution
        );

        return "redirect:/contributions";
    }


    // =========================================================
    // VIEW CONTRIBUTION
    // =========================================================

    @GetMapping("/contribution/view/{id}")
    public String viewContribution(
            @PathVariable Long id,
            Model model) {

        Contribution contribution =
                contributionRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Contribution not found"
                                )
                        );

        model.addAttribute(
                "contribution",
                contribution
        );

        return "contribution-view";
    }


    // =========================================================
    // EDIT CONTRIBUTION
    // =========================================================

    @GetMapping("/contribution/edit/{id}")
    public String editContribution(
            @PathVariable Long id,
            Model model) {

        Contribution contribution =
                contributionRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Contribution not found"
                                )
                        );

        model.addAttribute(
                "contribution",
                contribution
        );

        model.addAttribute(
                "members",
                memberRepository.findAll()
        );

        return "contribution-form";
    }


    // =========================================================
    // DELETE CONTRIBUTION
    // =========================================================

    @GetMapping("/contribution/delete/{id}")
    public String deleteContribution(
            @PathVariable Long id) {

        contributionService.deleteContribution(
                id
        );

        return "redirect:/contributions";
    }

}