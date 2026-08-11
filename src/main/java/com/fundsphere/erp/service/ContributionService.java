package com.fundsphere.erp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fundsphere.erp.entity.BusinessFund;
import com.fundsphere.erp.entity.Contribution;
import com.fundsphere.erp.entity.EmergencyFundTransaction;
import com.fundsphere.erp.repository.BusinessFundRepository;
import com.fundsphere.erp.repository.ContributionRepository;
import com.fundsphere.erp.repository.EmergencyFundTransactionRepository;

@Service
public class ContributionService {

    @Autowired
    private ContributionRepository contributionRepository;

    @Autowired
    private BusinessFundRepository businessFundRepository;

    @Autowired
    private EmergencyFundTransactionRepository emergencyFundTransactionRepository;


    // =========================================================
    // SAVE / UPDATE CONTRIBUTION
    // =========================================================

    @Transactional
    public Contribution saveContribution(Contribution contribution) {

        // =====================================================
        // DATE
        // =====================================================

        if (contribution.getContributionDate() == null) {

            contribution.setContributionDate(
                    LocalDate.now()
            );
        }


        // =====================================================
        // AMOUNT SAFETY
        // =====================================================

        if (contribution.getAmount() == null) {

            contribution.setAmount(0.0);
        }


        // =====================================================
        // PERCENTAGE NULL SAFETY
        // =====================================================

        if (contribution.getBusinessPercent() == null) {
            contribution.setBusinessPercent(0);
        }

        if (contribution.getEmergencyPercent() == null) {
            contribution.setEmergencyPercent(0);
        }

        if (contribution.getEducationPercent() == null) {
            contribution.setEducationPercent(0);
        }

        if (contribution.getWelfarePercent() == null) {
            contribution.setWelfarePercent(0);
        }

        if (contribution.getAdministrationPercent() == null) {
            contribution.setAdministrationPercent(0);
        }


        // =====================================================
        // TOTAL PERCENTAGE
        // =====================================================

        int totalPercentage =
                contribution.getBusinessPercent()
                + contribution.getEmergencyPercent()
                + contribution.getEducationPercent()
                + contribution.getWelfarePercent()
                + contribution.getAdministrationPercent();


        if (totalPercentage != 100) {

            throw new IllegalArgumentException(
                    "Total distribution percentage must be exactly 100%. Current total: "
                    + totalPercentage + "%"
            );
        }


        // =====================================================
        // CALCULATE FUND AMOUNTS
        // =====================================================

        double amount = contribution.getAmount();


        double businessAmount =
                amount
                * contribution.getBusinessPercent()
                / 100.0;


        double emergencyAmount =
                amount
                * contribution.getEmergencyPercent()
                / 100.0;


        double educationAmount =
                amount
                * contribution.getEducationPercent()
                / 100.0;


        double welfareAmount =
                amount
                * contribution.getWelfarePercent()
                / 100.0;


        double administrationAmount =
                amount
                * contribution.getAdministrationPercent()
                / 100.0;


        contribution.setBusinessAmount(
                businessAmount
        );

        contribution.setEmergencyAmount(
                emergencyAmount
        );

        contribution.setEducationAmount(
                educationAmount
        );

        contribution.setWelfareAmount(
                welfareAmount
        );

        contribution.setAdministrationAmount(
                administrationAmount
        );


        // =====================================================
        // RECEIPT NUMBER
        // =====================================================

        if (contribution.getId() == null) {

            if (contribution.getReceiptNo() == null
                    || contribution.getReceiptNo().trim().isEmpty()) {

                contribution.setReceiptNo(
                        "RC-" + System.currentTimeMillis()
                );
            }
        }


        // =====================================================
        // REMOVE OLD AUTO-GENERATED TRANSACTIONS
        // =====================================================

        String receiptNo =
                contribution.getReceiptNo();


        if (receiptNo != null
                && !receiptNo.trim().isEmpty()) {

            removeOldFundTransactions(receiptNo);
        }


        // =====================================================
        // SAVE CONTRIBUTION
        // =====================================================

        Contribution savedContribution =
                contributionRepository.save(
                        contribution
                );


        // =====================================================
        // CREATE BUSINESS FUND TRANSACTION
        // =====================================================

        if (businessAmount > 0) {

            BusinessFund businessFund =
                    new BusinessFund();

            businessFund.setTransactionNo(
                    "BF-CONTRIBUTION-" + receiptNo
            );

            businessFund.setTransactionDate(
                    savedContribution.getContributionDate()
            );

            businessFund.setTransactionType(
                    "Contribution"
            );

            businessFund.setAmount(
                    businessAmount
            );

            businessFund.setPaymentMethod(
                    savedContribution.getPaymentMethod()
            );

            businessFund.setMemberCode(
                    savedContribution.getMemberCode()
            );

            businessFund.setMemberName(
                    savedContribution.getMemberName()
            );

            businessFund.setDescription(
                    "Contribution - Business Fund Allocation"
            );

            businessFund.setRemarks(
                    "Automatically generated from contribution "
                    + receiptNo
            );

            businessFundRepository.save(
                    businessFund
            );
        }


        // =====================================================
        // CREATE EMERGENCY FUND TRANSACTION
        // =====================================================

        if (emergencyAmount > 0) {

            EmergencyFundTransaction emergencyFund =
                    new EmergencyFundTransaction();

            emergencyFund.setTransactionNo(
                    "EMG-CONTRIBUTION-" + receiptNo
            );

            emergencyFund.setTransactionDate(
                    savedContribution.getContributionDate()
            );

            emergencyFund.setTransactionType(
                    "Contribution"
            );

            emergencyFund.setMemberCode(
                    savedContribution.getMemberCode()
            );

            emergencyFund.setMemberName(
                    savedContribution.getMemberName()
            );

            emergencyFund.setDescription(
                    "Contribution - Emergency Fund Allocation"
            );

            emergencyFund.setPaymentMethod(
                    savedContribution.getPaymentMethod()
            );

            emergencyFund.setAmount(
                    emergencyAmount
            );

            emergencyFund.setRemarks(
                    "Automatically generated from contribution "
                    + receiptNo
            );

            emergencyFundTransactionRepository.save(
                    emergencyFund
            );
        }


        return savedContribution;
    }


    // =========================================================
    // DELETE CONTRIBUTION
    // =========================================================

    @Transactional
    public void deleteContribution(Long id) {

        Contribution contribution =
                contributionRepository
                        .findById(id)
                        .orElse(null);


        if (contribution == null) {
            return;
        }


        // =====================================================
        // GET RECEIPT NUMBER
        // =====================================================

        String receiptNo =
                contribution.getReceiptNo();


        // =====================================================
        // DELETE AUTOMATIC FUND TRANSACTIONS
        // =====================================================

        if (receiptNo != null
                && !receiptNo.trim().isEmpty()) {

            removeOldFundTransactions(
                    receiptNo
            );
        }


        // =====================================================
        // DELETE CONTRIBUTION
        // =====================================================

        contributionRepository.delete(
                contribution
        );
    }


    // =========================================================
    // REMOVE AUTO-GENERATED FUND TRANSACTIONS
    // =========================================================

    private void removeOldFundTransactions(
            String receiptNo) {


        // =====================================================
        // BUSINESS FUND
        // =====================================================

        String businessTransactionNo =
                "BF-CONTRIBUTION-" + receiptNo;


        List<BusinessFund> businessFunds =
                businessFundRepository.findAll();


        for (BusinessFund businessFund :
                businessFunds) {

            if (businessTransactionNo.equals(
                    businessFund.getTransactionNo()
            )) {

                businessFundRepository.delete(
                        businessFund
                );
            }
        }


        // =====================================================
        // EMERGENCY FUND
        // =====================================================

        String emergencyTransactionNo =
                "EMG-CONTRIBUTION-" + receiptNo;


        List<EmergencyFundTransaction>
                emergencyTransactions =
                emergencyFundTransactionRepository
                        .findAll();


        for (EmergencyFundTransaction transaction :
                emergencyTransactions) {

            if (emergencyTransactionNo.equals(
                    transaction.getTransactionNo()
            )) {

                emergencyFundTransactionRepository.delete(
                        transaction
                );
            }
        }
    }

}