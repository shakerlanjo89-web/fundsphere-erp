package com.fundsphere.erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.fundsphere.erp.entity.BusinessFund;
import com.fundsphere.erp.entity.EmergencyFundTransaction;
import com.fundsphere.erp.entity.Setting;
import com.fundsphere.erp.repository.BusinessFundRepository;
import com.fundsphere.erp.repository.ContributionRepository;
import com.fundsphere.erp.repository.EmergencyFundTransactionRepository;
import com.fundsphere.erp.repository.ExpenseRepository;
import com.fundsphere.erp.repository.MemberRepository;
import com.fundsphere.erp.service.SettingService;

@Controller
public class DashboardController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ContributionRepository contributionRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BusinessFundRepository businessFundRepository;

    @Autowired
    private EmergencyFundTransactionRepository
            emergencyFundTransactionRepository;

    @Autowired
    private SettingService settingService;


    // =========================================================
    // DASHBOARD
    // =========================================================

    @org.springframework.web.bind.annotation.GetMapping("/dashboard")
    public String dashboard(
            Model model,
            Authentication authentication) {


        // =====================================================
        // USER INFORMATION / ROLE
        // =====================================================

        String username = "Administrator";

        String role = "Super Admin";

        boolean isAdmin = false;

        boolean isFinanceManager = false;

        boolean isAccountant = false;


        if (authentication != null) {

            if (authentication.getName() != null
                    && !authentication.getName().trim().isEmpty()) {

                username =
                        authentication.getName();
            }


            for (GrantedAuthority authority :
                    authentication.getAuthorities()) {

                if (authority == null) {
                    continue;
                }


                String authorityName =
                        authority.getAuthority();


                if (authorityName == null) {
                    continue;
                }


                // =============================================
                // ADMIN
                // =============================================

                if ("ROLE_ADMIN"
                        .equalsIgnoreCase(authorityName)) {

                    isAdmin = true;

                    role = "Super Admin";
                }


                // =============================================
                // FINANCE MANAGER
                // =============================================

                else if ("ROLE_FINANCE_MANAGER"
                        .equalsIgnoreCase(authorityName)) {

                    isFinanceManager = true;

                    if (!isAdmin) {
                        role = "Finance Manager";
                    }
                }


                // =============================================
                // ACCOUNTANT
                // =============================================

                else if ("ROLE_ACCOUNTANT"
                        .equalsIgnoreCase(authorityName)) {

                    isAccountant = true;

                    if (!isAdmin
                            && !isFinanceManager) {

                        role = "Accountant";
                    }
                }
            }
        }


        // =====================================================
        // DASHBOARD USER INFORMATION
        // =====================================================

        model.addAttribute(
                "currentUsername",
                username
        );

        model.addAttribute(
                "currentRole",
                role
        );

        model.addAttribute(
                "isAdmin",
                isAdmin
        );

        model.addAttribute(
                "isFinanceManager",
                isFinanceManager
        );

        model.addAttribute(
                "isAccountant",
                isAccountant
        );


        // =====================================================
        // ORGANIZATION SETTINGS
        // =====================================================

        Setting setting =
                settingService.getSettings();


        if (setting != null) {


            model.addAttribute(
                    "organizationName",
                    safe(
                        setting.getOrganizationName()
                    )
            );


            model.addAttribute(
                    "organizationShortName",
                    safe(
                        setting.getShortName()
                    )
            );


            model.addAttribute(
                    "organizationTagline",
                    safe(
                        setting.getTagline()
                    )
            );


            model.addAttribute(
                    "organizationPhone",
                    safe(
                        setting.getPhone()
                    )
            );


            model.addAttribute(
                    "organizationEmail",
                    safe(
                        setting.getEmail()
                    )
            );


            model.addAttribute(
                    "organizationWebsite",
                    safe(
                        setting.getWebsite()
                    )
            );


            model.addAttribute(
                    "organizationAddress",
                    safe(
                        setting.getAddress()
                    )
            );


            model.addAttribute(
                    "organizationDistrict",
                    safe(
                        setting.getDistrict()
                    )
            );


            model.addAttribute(
                    "organizationProvince",
                    safe(
                        setting.getProvince()
                    )
            );


            model.addAttribute(
                    "organizationCountry",
                    safe(
                        setting.getCountry()
                    )
            );


            model.addAttribute(
                    "organizationRegistrationNo",
                    safe(
                        setting.getRegistrationNo()
                    )
            );


            model.addAttribute(
                    "organizationLogo",
                    safe(
                        setting.getLogoPath()
                    )
            );


            model.addAttribute(
                    "currency",
                    safe(
                        setting.getCurrency()
                    )
            );


            model.addAttribute(
                    "dateFormat",
                    safe(
                        setting.getDateFormat()
                    )
            );

        } else {


            model.addAttribute(
                    "organizationName",
                    ""
            );


            model.addAttribute(
                    "organizationShortName",
                    ""
            );


            model.addAttribute(
                    "organizationTagline",
                    ""
            );


            model.addAttribute(
                    "organizationPhone",
                    ""
            );


            model.addAttribute(
                    "organizationEmail",
                    ""
            );


            model.addAttribute(
                    "organizationWebsite",
                    ""
            );


            model.addAttribute(
                    "organizationAddress",
                    ""
            );


            model.addAttribute(
                    "organizationDistrict",
                    ""
            );


            model.addAttribute(
                    "organizationProvince",
                    ""
            );


            model.addAttribute(
                    "organizationCountry",
                    ""
            );


            model.addAttribute(
                    "organizationRegistrationNo",
                    ""
            );


            model.addAttribute(
                    "organizationLogo",
                    ""
            );


            model.addAttribute(
                    "currency",
                    "Rs."
            );


            model.addAttribute(
                    "dateFormat",
                    "dd-MM-yyyy"
            );
        }


        // =====================================================
        // MEMBER SUMMARY
        // =====================================================

        model.addAttribute(
                "totalMembers",
                memberRepository.count()
        );

        model.addAttribute(
                "activeMembers",
                memberRepository.countByMembershipStatus("ACTIVE")
        );

        model.addAttribute(
                "inactiveMembers",
                memberRepository.countByMembershipStatus("INACTIVE")
        );


        // =====================================================
        // TOTAL CONTRIBUTIONS
        // =====================================================

        Double totalContributions =
                contributionRepository
                        .getTotalContributions();


        if (totalContributions == null) {

            totalContributions = 0.0;
        }


        model.addAttribute(
                "totalContributions",
                totalContributions
        );


        // =====================================================
        // BUSINESS FUND
        // =====================================================

        List<BusinessFund> businessFunds =
                businessFundRepository.findAll();


        double businessIncome = 0.0;

        double businessContribution = 0.0;

        double businessProfit = 0.0;

        double businessWithdrawal = 0.0;


        for (BusinessFund b :
                businessFunds) {


            double amount =
                    b.getAmount() != null
                            ? b.getAmount()
                            : 0.0;


            String type =
                    b.getTransactionType();


            // ===============================================
            // CONTRIBUTION
            // ===============================================

            if ("Contribution"
                    .equalsIgnoreCase(type)) {

                businessContribution +=
                        amount;
            }


            // ===============================================
            // BUSINESS INCOME
            // ===============================================

            else if ("Business Income"
                    .equalsIgnoreCase(type)) {

                businessIncome +=
                        amount;
            }


            // ===============================================
            // PROFIT
            // ===============================================

            else if ("Profit"
                    .equalsIgnoreCase(type)) {

                businessProfit +=
                        amount;
            }


            // ===============================================
            // WITHDRAWAL
            // ===============================================

            else if ("Withdrawal"
                    .equalsIgnoreCase(type)) {

                businessWithdrawal +=
                        amount;
            }
        }


        // =====================================================
        // BUSINESS FUND BALANCE
        // =====================================================

        double businessBalance =
                businessContribution
                + businessIncome
                + businessProfit
                - businessWithdrawal;


        model.addAttribute(
                "businessFund",
                businessBalance
        );


        // =====================================================
        // EMERGENCY FUND
        // =====================================================

        List<EmergencyFundTransaction>
                emergencyTransactions =
                emergencyFundTransactionRepository
                        .findAll();


        double emergencyContribution = 0.0;

        double emergencyIncome = 0.0;

        double emergencyWithdrawal = 0.0;


        for (EmergencyFundTransaction e
                : emergencyTransactions) {


            double amount =
                    e.getAmount() != null
                            ? e.getAmount()
                            : 0.0;


            String type =
                    e.getTransactionType();


            // ===============================================
            // CONTRIBUTION
            // ===============================================

            if ("Contribution"
                    .equalsIgnoreCase(type)) {

                emergencyContribution +=
                        amount;
            }


            // ===============================================
            // INCOME
            // ===============================================

            else if ("Income"
                    .equalsIgnoreCase(type)) {

                emergencyIncome +=
                        amount;
            }


            // ===============================================
            // WITHDRAWAL
            // ===============================================

            else if ("Withdrawal"
                    .equalsIgnoreCase(type)) {

                emergencyWithdrawal +=
                        amount;
            }
        }


        // =====================================================
        // EMERGENCY FUND BALANCE
        // =====================================================

        double emergencyBalance =
                emergencyContribution
                + emergencyIncome
                - emergencyWithdrawal;


        model.addAttribute(
                "emergencyFund",
                emergencyBalance
        );


        // =====================================================
        // TOTAL EXPENSES
        // =====================================================

        Double totalExpenses =
                expenseRepository
                        .getTotalExpenses();


        if (totalExpenses == null) {

            totalExpenses = 0.0;
        }


        model.addAttribute(
                "totalExpenses",
                totalExpenses
        );


        // =====================================================
        // TOTAL PROFIT
        // =====================================================

        model.addAttribute(
                "totalProfit",
                businessProfit
        );


        // =====================================================
        // OVERALL BALANCE
        // =====================================================

        double overallBalance =
                businessBalance
                + emergencyBalance
                - totalExpenses;


        model.addAttribute(
                "overallBalance",
                overallBalance
        );


        // =====================================================
        // OTHER FUNDS
        // =====================================================

        Double educationFund =
                contributionRepository
                        .getTotalEducationFund();


        if (educationFund == null) {

            educationFund = 0.0;
        }


        Double welfareFund =
                contributionRepository
                        .getTotalWelfareFund();


        if (welfareFund == null) {

            welfareFund = 0.0;
        }


        Double administrationFund =
                contributionRepository
                        .getTotalAdministrationFund();


        if (administrationFund == null) {

            administrationFund = 0.0;
        }


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
        // RETURN DASHBOARD
        // =====================================================

        return "dashboard";
    }


    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safe(String value) {

        return value != null
                ? value
                : "";
    }
}