package com.fundsphere.erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fundsphere.erp.service.MemberBusinessService.MemberBusinessSummary;
import com.fundsphere.erp.service.ProfitService;
import com.fundsphere.erp.service.ProfitService.ProfitSummary;

@Controller
public class ProfitController {

    @Autowired
    private ProfitService profitService;


    // =====================================================
    // PROFIT DASHBOARD
    // =====================================================

    @GetMapping("/profit")
    public String profit(
            Model model) {


        // -------------------------------------------------
        // COMPLETE SUMMARY
        // -------------------------------------------------

        ProfitSummary summary =
                profitService
                        .getProfitSummary();


        // -------------------------------------------------
        // MEMBER-WISE PROFIT
        // -------------------------------------------------

        List<MemberBusinessSummary>
                memberProfitSummary =
                profitService
                        .getMemberProfitSummary();


        // -------------------------------------------------
        // MODEL
        // -------------------------------------------------

        model.addAttribute(
                "summary",
                summary
        );

        model.addAttribute(
                "memberProfitSummary",
                memberProfitSummary
        );


        return "profit";
    }


    // =====================================================
    // MEMBER PROFIT DETAIL
    // =====================================================

    @GetMapping("/profit/member")
    public String memberProfit(
            @RequestParam("memberCode")
            String memberCode,
            Model model) {


        MemberBusinessSummary summary =
                profitService
                        .getMemberProfit(
                                memberCode
                        );


        model.addAttribute(
                "member",
                summary
        );


        return "profit-member";
    }
}