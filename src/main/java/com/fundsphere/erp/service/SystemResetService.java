package com.fundsphere.erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fundsphere.erp.repository.BusinessFundRepository;
import com.fundsphere.erp.repository.ContributionRepository;
import com.fundsphere.erp.repository.EmergencyFundTransactionRepository;
import com.fundsphere.erp.repository.ExpenseRepository;
import com.fundsphere.erp.repository.MemberRepository;
import com.fundsphere.erp.repository.SettingRepository;

@Service
public class SystemResetService {

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
    private MemberRepository memberRepository;

    @Autowired
    private SettingRepository settingRepository;


    // =========================================
    // FACTORY RESET
    // =========================================

    @Transactional
    public void factoryReset() {

        /*
         * Delete transaction data first.
         */

        emergencyFundTransactionRepository.deleteAll();

        businessFundRepository.deleteAll();

        expenseRepository.deleteAll();

        contributionRepository.deleteAll();


        /*
         * Delete members.
         */

        memberRepository.deleteAll();


        /*
         * Delete settings.
         *
         * Default settings will be recreated
         * automatically when Settings page is opened.
         */

        settingRepository.deleteAll();
    }

}