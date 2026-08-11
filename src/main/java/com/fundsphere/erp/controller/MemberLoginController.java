package com.fundsphere.erp.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fundsphere.erp.entity.BusinessFund;
import com.fundsphere.erp.entity.Contribution;
import com.fundsphere.erp.entity.EmergencyFundTransaction;
import com.fundsphere.erp.entity.Member;
import com.fundsphere.erp.entity.Setting;
import com.fundsphere.erp.repository.BusinessFundRepository;
import com.fundsphere.erp.repository.ContributionRepository;
import com.fundsphere.erp.repository.EmergencyFundTransactionRepository;
import com.fundsphere.erp.repository.MemberRepository;
import com.fundsphere.erp.service.BusinessTransactionService;
import com.fundsphere.erp.service.BusinessTransactionService.MemberBusinessSummary;
import com.fundsphere.erp.service.SettingService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberLoginController {


    // =====================================================
    // REPOSITORIES
    // =====================================================

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ContributionRepository contributionRepository;

    @Autowired
    private BusinessFundRepository businessFundRepository;

    @Autowired
    private EmergencyFundTransactionRepository
            emergencyFundTransactionRepository;


    // =====================================================
    // SERVICES
    // =====================================================

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BusinessTransactionService
            businessTransactionService;

    @Autowired
    private SettingService settingService;


    // =====================================================
    // MEMBER LOGIN PAGE
    // =====================================================

    @GetMapping("/member-login")
    public String memberLogin() {

        return "member-login";
    }


    // =====================================================
    // MEMBER AUTHENTICATION
    // =====================================================

    @PostMapping("/member-login/authenticate")
    public String authenticate(

            @RequestParam("memberCode")
            String memberCode,

            @RequestParam("password")
            String password,

            HttpSession session) {


        Member member =
                memberRepository.findByMemberCode(
                        memberCode.trim()
                );


        // -------------------------------------------------
        // MEMBER NOT FOUND
        // -------------------------------------------------

        if (member == null) {

            return "redirect:/member-login?error=true";
        }


        // -------------------------------------------------
        // PASSWORD NOT ASSIGNED
        // -------------------------------------------------

        if (member.getPasswordHash() == null
                || member.getPasswordHash().isBlank()) {

            return "redirect:/member-login?error=true";
        }


        // -------------------------------------------------
        // PASSWORD CHECK
        // -------------------------------------------------

        boolean passwordCorrect =
                passwordEncoder.matches(
                        password,
                        member.getPasswordHash()
                );


        if (!passwordCorrect) {

            return "redirect:/member-login?error=true";
        }


        // -------------------------------------------------
        // MEMBERSHIP STATUS
        // -------------------------------------------------

        if (member.getMembershipStatus() != null
                && (
                    member.getMembershipStatus()
                        .equalsIgnoreCase("INACTIVE")

                    ||

                    member.getMembershipStatus()
                        .equalsIgnoreCase("SUSPENDED")
                )) {

            return "redirect:/member-login?error=true";
        }


        // =================================================
        // CREATE MEMBER SESSION
        // =================================================

        session.setAttribute(
                "memberId",
                member.getId()
        );

        session.setAttribute(
                "memberCode",
                member.getMemberCode()
        );

        session.setAttribute(
                "memberName",
                member.getFullName()
        );


        return "redirect:/member-dashboard";
    }


    // =====================================================
    // MEMBER DASHBOARD
    // =====================================================

    @GetMapping("/member-dashboard")
    public String memberDashboard(

            HttpSession session,
            Model model) {


        // =================================================
        // CHECK LOGIN
        // =================================================

        Object memberId =
                session.getAttribute("memberId");


        if (memberId == null) {

            return "redirect:/member-login";
        }


        // =================================================
        // GET MEMBER
        // =================================================

        Member member =
                memberRepository.findById(
                        Long.valueOf(
                                memberId.toString()
                        )
                ).orElse(null);


        if (member == null) {

            session.invalidate();

            return "redirect:/member-login";
        }


        String memberCode =
                member.getMemberCode();


        // =================================================
        // ORGANIZATION SETTINGS
        // =================================================

        Setting setting =
                settingService.getSettings();


        if (setting != null) {

            model.addAttribute(
                    "organizationName",
                    safe(setting.getOrganizationName())
            );

            model.addAttribute(
                    "organizationPhone",
                    safe(setting.getPhone())
            );

            model.addAttribute(
                    "organizationEmail",
                    safe(setting.getEmail())
            );

            model.addAttribute(
                    "organizationWebsite",
                    safe(setting.getWebsite())
            );

            model.addAttribute(
                    "organizationAddress",
                    safe(setting.getAddress())
            );

            String logoPath =
                    safe(setting.getLogoPath());


            if (!logoPath.isBlank()) {

                model.addAttribute(
                        "organizationLogo",
                        logoPath
                );
            }
        }


        // =================================================
        // MEMBER CONTRIBUTIONS
        // =================================================

        List<Contribution> contributions =
                contributionRepository.findAll()
                        .stream()
                        .filter(c ->
                                c.getMemberCode() != null
                                && c.getMemberCode()
                                    .equalsIgnoreCase(
                                        memberCode
                                    )
                        )
                        .sorted(
                                Comparator.comparing(
                                        Contribution
                                            ::getContributionDate,
                                        Comparator.nullsLast(
                                            Comparator.reverseOrder()
                                        )
                                )
                        )
                        .toList();


        // =================================================
        // MEMBER BUSINESS FUND TRANSACTIONS
        // =================================================

        List<BusinessFund>
                businessTransactions =
                businessFundRepository.findAll()
                        .stream()
                        .filter(b ->
                                b.getMemberCode() != null
                                && b.getMemberCode()
                                    .equalsIgnoreCase(
                                        memberCode
                                    )
                        )
                        .sorted(
                                Comparator.comparing(
                                        BusinessFund
                                            ::getTransactionDate,
                                        Comparator.nullsLast(
                                            Comparator.reverseOrder()
                                        )
                                )
                        )
                        .toList();


        // =================================================
        // MEMBER EMERGENCY TRANSACTIONS
        // =================================================

        List<EmergencyFundTransaction>
                emergencyTransactions =
                emergencyFundTransactionRepository.findAll()
                        .stream()
                        .filter(e ->
                                e.getMemberCode() != null
                                && e.getMemberCode()
                                    .equalsIgnoreCase(
                                        memberCode
                                    )
                        )
                        .sorted(
                                Comparator.comparing(
                                        EmergencyFundTransaction
                                            ::getTransactionDate,
                                        Comparator.nullsLast(
                                            Comparator.reverseOrder()
                                        )
                                )
                        )
                        .toList();


        // =================================================
        // MEMBER CONTRIBUTION TOTALS
        // =================================================

        double totalContribution = 0.0;

        double businessInvestment = 0.0;

        double emergencyFund = 0.0;

        double educationFund = 0.0;

        double welfareFund = 0.0;

        double administrationFund = 0.0;


        for (Contribution c : contributions) {


            double amount =
                    c.getAmount() != null
                            ? c.getAmount()
                            : 0.0;


            totalContribution += amount;


            businessInvestment +=
                    c.getBusinessAmount() != null
                            ? c.getBusinessAmount()
                            : 0.0;


            emergencyFund +=
                    c.getEmergencyAmount() != null
                            ? c.getEmergencyAmount()
                            : 0.0;


            educationFund +=
                    c.getEducationAmount() != null
                            ? c.getEducationAmount()
                            : 0.0;


            welfareFund +=
                    c.getWelfareAmount() != null
                            ? c.getWelfareAmount()
                            : 0.0;


            administrationFund +=
                    c.getAdministrationAmount() != null
                            ? c.getAdministrationAmount()
                            : 0.0;
        }


        // =====================================================
        // MEMBER BUSINESS SUMMARY
        //
        // ALL BUSINESS INVESTMENT / PROFIT / EXPENSE
        // CALCULATIONS COME FROM BusinessTransactionService
        // =====================================================

        MemberBusinessSummary
                memberBusinessSummary =
                businessTransactionService
                        .getMemberBusinessSummary(
                                memberCode
                        );


        // =====================================================
        // TOTAL BUSINESS INVESTMENT
        // =====================================================

        double totalBusinessInvestment =
                businessTransactionService
                        .getTotalBusinessInvestment();


        // =====================================================
        // MEMBER INVESTMENT PERCENTAGE
        // =====================================================

        double memberInvestmentPercent =
                memberBusinessSummary
                        .getInvestmentPercent();


        // =====================================================
        // MEMBER PROFIT / LOSS SHARE
        // =====================================================

        double memberProfitLossShare =
                memberBusinessSummary
                        .getGrossProfitLoss();


        // =====================================================
        // MEMBER BUSINESS EXPENSE SHARE
        // =====================================================

        double memberExpenseShare =
                memberBusinessSummary
                        .getExpenseShare();


        // =====================================================
        // MEMBER NET BUSINESS RESULT
        // =====================================================

        double memberNetBusinessResult =
                memberBusinessSummary
                        .getNetProfitLoss();


        // =====================================================
        // BUSINESS TRANSACTION RESULTS
        //
        // Purchase / Sale / Expense
        // =====================================================

        double totalBusinessSales =
                businessTransactionService
                        .getTotalSales();


        double totalBusinessExpenses =
                businessTransactionService
                        .getTotalBusinessExpenses();


        double grossProfitLoss =
                businessTransactionService
                        .getGrossProfitLoss();


        // =====================================================
        // COST OF GOODS SOLD
        // =====================================================

        double totalCostOfGoodsSold =
                totalBusinessSales
                - grossProfitLoss;


        // =====================================================
        // NET BUSINESS PROFIT / LOSS
        // =====================================================

        double netBusinessProfitLoss =
                businessTransactionService
                        .getNetProfitLoss();


        // =====================================================
        // BUSINESS FUND TRANSACTIONS
        // =====================================================

        double businessIncome = 0.0;

        double businessProfit = 0.0;

        double businessWithdrawal = 0.0;


        for (BusinessFund b :
                businessTransactions) {


            double amount =
                    b.getAmount() != null
                            ? b.getAmount()
                            : 0.0;


            String type =
                    safe(b.getTransactionType());


            if ("Contribution"
                    .equalsIgnoreCase(type)) {

                /*
                 * Already included through
                 * Contribution.businessAmount.
                 */
            }


            else if ("Business Income"
                    .equalsIgnoreCase(type)) {

                businessIncome += amount;
            }


            else if ("Profit"
                    .equalsIgnoreCase(type)) {

                businessProfit += amount;
            }


            else if ("Withdrawal"
                    .equalsIgnoreCase(type)) {

                businessWithdrawal += amount;
            }
        }


        // =====================================================
        // MEMBER WITHDRAWAL SHARE
        //
        // Withdrawal is allocated according to the
        // member's actual business investment ratio.
        // =====================================================

        double memberWithdrawalShare =
                businessWithdrawal
                * (
                    memberInvestmentPercent
                    / 100.0
                );


        // =====================================================
        // MEMBER FINAL BUSINESS BALANCE
        //
        // Investment
        // + Net Profit/Loss
        // - Member Withdrawal Share
        // =====================================================

        double memberBusinessFinalBalance =
                businessInvestment
                + memberNetBusinessResult
                - memberWithdrawalShare;


        // =====================================================
        // BUSINESS BALANCE
        // =====================================================

        double businessBalance =
                businessInvestment
                + businessIncome
                + businessProfit
                - businessWithdrawal;


        // =====================================================
        // EMERGENCY FUND
        // =====================================================

        double emergencyIncome = 0.0;

        double emergencyWithdrawal = 0.0;


        for (EmergencyFundTransaction e :
                emergencyTransactions) {


            double amount =
                    e.getAmount() != null
                            ? e.getAmount()
                            : 0.0;


            String type =
                    safe(e.getTransactionType());


            if ("Contribution"
                    .equalsIgnoreCase(type)) {

                /*
                 * Already included in
                 * Contribution.emergencyAmount.
                 */
            }


            else if (
                    "Income".equalsIgnoreCase(type)

                    ||

                    "Deposit".equalsIgnoreCase(type)
            ) {

                emergencyIncome += amount;
            }


            else if (
                    "Withdrawal".equalsIgnoreCase(type)
            ) {

                emergencyWithdrawal += amount;
            }
        }


        // =====================================================
        // MEMBER EMERGENCY BALANCE
        // =====================================================

        double emergencyBalance =
                emergencyFund
                + emergencyIncome
                - emergencyWithdrawal;


        // =====================================================
        // TOTAL MEMBER BALANCE
        // =====================================================

        double totalBalance =
                memberBusinessFinalBalance
                + emergencyBalance
                + educationFund
                + welfareFund
                + administrationFund;


        // =====================================================
        // BASIC MEMBER DATA
        // =====================================================

        model.addAttribute(
                "member",
                member
        );

        model.addAttribute(
                "contributions",
                contributions
        );

        model.addAttribute(
                "businessTransactions",
                businessTransactions
        );

        model.addAttribute(
                "emergencyTransactions",
                emergencyTransactions
        );


        // =====================================================
        // MEMBER CONTRIBUTIONS
        // =====================================================

        model.addAttribute(
                "totalContribution",
                totalContribution
        );

        model.addAttribute(
                "businessInvestment",
                businessInvestment
        );

        model.addAttribute(
                "emergencyFund",
                emergencyFund
        );

        model.addAttribute(
                "educationFund",
                educationFund
        );

        model.addAttribute(
                "welfareFund",
                welfareFund
        );

        model.addAttribute(
                "administrationFund",
                administrationFund
        );


        // =====================================================
        // BUSINESS INVESTMENT SHARE
        // =====================================================

        model.addAttribute(
                "totalBusinessInvestment",
                totalBusinessInvestment
        );

        model.addAttribute(
                "memberInvestmentPercent",
                memberInvestmentPercent
        );


        // =====================================================
        // BUSINESS ACCOUNTING
        // =====================================================

        model.addAttribute(
                "totalBusinessSales",
                totalBusinessSales
        );

        model.addAttribute(
                "totalCostOfGoodsSold",
                totalCostOfGoodsSold
        );

        model.addAttribute(
                "totalBusinessExpenses",
                totalBusinessExpenses
        );

        model.addAttribute(
                "grossProfitLoss",
                grossProfitLoss
        );

        model.addAttribute(
                "netBusinessProfitLoss",
                netBusinessProfitLoss
        );


        // =====================================================
        // MEMBER BUSINESS SHARE
        // =====================================================

        model.addAttribute(
                "memberProfitLossShare",
                memberProfitLossShare
        );

        model.addAttribute(
                "memberExpenseShare",
                memberExpenseShare
        );

        model.addAttribute(
                "memberNetBusinessResult",
                memberNetBusinessResult
        );

        model.addAttribute(
                "memberWithdrawalShare",
                memberWithdrawalShare
        );

        model.addAttribute(
                "memberBusinessFinalBalance",
                memberBusinessFinalBalance
        );


        // =====================================================
        // MEMBER BUSINESS FUND
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
        // EMERGENCY FUND
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
        // TOTAL BALANCE
        // =====================================================

        model.addAttribute(
                "totalBalance",
                totalBalance
        );


        // =====================================================
        // RETURN DASHBOARD
        // =====================================================

        return "member-dashboard";
    }


    // =====================================================
    // MEMBER LOGOUT
    // =====================================================

    @GetMapping("/member-logout")
    public String memberLogout(
            HttpSession session) {


        session.invalidate();


        return "redirect:/member-login?logout=true";
    }


    // =====================================================
    // SAFE STRING
    // =====================================================

    private static String safe(
            String value) {

        return value != null
                ? value
                : "";
    }

}