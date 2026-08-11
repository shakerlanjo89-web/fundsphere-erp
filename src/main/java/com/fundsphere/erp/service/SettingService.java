package com.fundsphere.erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fundsphere.erp.entity.Setting;
import com.fundsphere.erp.repository.BusinessFundRepository;
import com.fundsphere.erp.repository.BusinessTransactionRepository;
import com.fundsphere.erp.repository.ContributionRepository;
import com.fundsphere.erp.repository.EmergencyFundTransactionRepository;
import com.fundsphere.erp.repository.ExpenseRepository;
import com.fundsphere.erp.repository.LedgerRepository;
import com.fundsphere.erp.repository.MemberRepository;
import com.fundsphere.erp.repository.SettingRepository;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ContributionRepository contributionRepository;

    @Autowired
    private BusinessFundRepository businessFundRepository;

    @Autowired
    private BusinessTransactionRepository
            businessTransactionRepository;

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private EmergencyFundTransactionRepository
            emergencyFundTransactionRepository;

    @Autowired
    private ExpenseRepository expenseRepository;


    // =====================================================
    // GET SETTINGS
    // =====================================================

    public Setting getSettings() {

        return settingRepository
                .findAll()
                .stream()
                .findFirst()
                .orElseGet(() -> {

                    Setting setting = new Setting();

                    // -----------------------------------------
                    // DEFAULT ORGANIZATION
                    // -----------------------------------------

                    setting.setOrganizationName(
                            "FundSphere ERP"
                    );

                    setting.setShortName(
                            "FSP"
                    );

                    setting.setTagline(
                            "One Family. One Fund. One Future."
                    );

                    setting.setAddress("");

                    setting.setDistrict("");

                    setting.setProvince(
                            "Sindh"
                    );

                    setting.setCountry(
                            "Pakistan"
                    );

                    setting.setPhone("");

                    setting.setEmail("");

                    setting.setWebsite("");

                    setting.setRegistrationNo("");

                    setting.setLogoPath("");


                    // -----------------------------------------
                    // DEFAULT SYSTEM SETTINGS
                    // -----------------------------------------

                    setting.setCurrency(
                            "Rs."
                    );

                    setting.setDateFormat(
                            "yyyy-MM-dd"
                    );


                    // -----------------------------------------
                    // DEFAULT FUND DISTRIBUTION
                    // -----------------------------------------

                    setting.setBusinessPercent(
                            70
                    );

                    setting.setEmergencyPercent(
                            25
                    );

                    setting.setEducationPercent(
                            0
                    );

                    setting.setWelfarePercent(
                            0
                    );

                    setting.setAdministrationPercent(
                            5
                    );


                    return settingRepository.save(
                            setting
                    );
                });
    }


    // =====================================================
    // SAVE SETTINGS
    // =====================================================

    public Setting saveSettings(
            Setting setting) {

        validateFundPercentages(setting);

        return settingRepository.save(
                setting
        );
    }


    // =====================================================
    // FUND PERCENTAGE VALIDATION
    // =====================================================

    public void validateFundPercentages(
            Setting setting) {

        int business =
                setting.getBusinessPercent() != null
                        ? setting.getBusinessPercent()
                        : 0;

        int emergency =
                setting.getEmergencyPercent() != null
                        ? setting.getEmergencyPercent()
                        : 0;

        int education =
                setting.getEducationPercent() != null
                        ? setting.getEducationPercent()
                        : 0;

        int welfare =
                setting.getWelfarePercent() != null
                        ? setting.getWelfarePercent()
                        : 0;

        int administration =
                setting.getAdministrationPercent() != null
                        ? setting.getAdministrationPercent()
                        : 0;


        int total =
                business
                + emergency
                + education
                + welfare
                + administration;


        if (total != 100) {

            throw new IllegalArgumentException(
                    "Fund distribution percentage must be exactly 100%. Current total: "
                    + total
                    + "%"
            );
        }


        if (business < 0
                || emergency < 0
                || education < 0
                || welfare < 0
                || administration < 0) {

            throw new IllegalArgumentException(
                    "Fund percentages cannot be negative."
            );
        }


        if (business > 100
                || emergency > 100
                || education > 100
                || welfare > 100
                || administration > 100) {

            throw new IllegalArgumentException(
                    "Fund percentages cannot be greater than 100%."
            );
        }
    }


    // =====================================================
    // FACTORY RESET
    // =====================================================

    @Transactional
    public void factoryReset() {

        /*
         * IMPORTANT:
         *
         * Admin / Login / User authentication
         * records are NOT touched here.
         *
         * Factory Reset deletes all operational
         * ERP data including:
         *
         * Members
         * Contributions
         * Business Fund
         * Business Accounting
         * Profit / Ledger records
         * Emergency Fund
         * Expenses
         * Settings
         */


        // -----------------------------------------
        // 1. LEDGER
        //
        // Delete ledger records first because
        // Ledger may contain records related to
        // other operational transactions.
        // -----------------------------------------

        ledgerRepository.deleteAll();


        // -----------------------------------------
        // 2. BUSINESS ACCOUNTING
        //
        // Purchase / Sales / Expense / Profit
        // transaction records.
        // -----------------------------------------

        businessTransactionRepository.deleteAll();


        // -----------------------------------------
        // 3. BUSINESS FUND
        // -----------------------------------------

        businessFundRepository.deleteAll();


        // -----------------------------------------
        // 4. EMERGENCY FUND
        // -----------------------------------------

        emergencyFundTransactionRepository
                .deleteAll();


        // -----------------------------------------
        // 5. EXPENSES
        // -----------------------------------------

        expenseRepository.deleteAll();


        // -----------------------------------------
        // 6. CONTRIBUTIONS
        // -----------------------------------------

        contributionRepository.deleteAll();


        // -----------------------------------------
        // 7. MEMBERS
        // -----------------------------------------

        memberRepository.deleteAll();


        // -----------------------------------------
        // 8. SETTINGS
        // -----------------------------------------

        settingRepository.deleteAll();


        /*
         * Settings will automatically be recreated
         * with default values when /settings is opened.
         *
         * Admin / Login / User accounts are NOT deleted.
         */
    }
}