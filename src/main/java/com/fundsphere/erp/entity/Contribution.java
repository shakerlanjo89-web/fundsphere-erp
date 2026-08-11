package com.fundsphere.erp.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contributions")
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String receiptNo;

    private String memberCode;

    private String memberName;

    private Double amount;

    private LocalDate contributionDate;

    private String paymentMethod;

    private Integer businessPercent;

    private Double businessAmount;

    private Integer emergencyPercent;

    private Double emergencyAmount;

    private Integer educationPercent;

    private Double educationAmount;

    private Integer welfarePercent;

    private Double welfareAmount;

    private Integer administrationPercent;

    private Double administrationAmount;

    @Column(columnDefinition = "TEXT")
    private String remarks;


    // =========================
    // CONSTRUCTOR
    // =========================

    public Contribution() {
    }


    // =========================
    // ID
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // =========================
    // RECEIPT NUMBER
    // =========================

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }


    // =========================
    // MEMBER CODE
    // =========================

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }


    // =========================
    // MEMBER NAME
    // =========================

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }


    // =========================
    // AMOUNT
    // =========================

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    // =========================
    // DATE
    // =========================

    public LocalDate getContributionDate() {
        return contributionDate;
    }

    public void setContributionDate(LocalDate contributionDate) {
        this.contributionDate = contributionDate;
    }


    // =========================
    // PAYMENT METHOD
    // =========================

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    // =========================
    // BUSINESS FUND
    // =========================

    public Integer getBusinessPercent() {
        return businessPercent;
    }

    public void setBusinessPercent(Integer businessPercent) {
        this.businessPercent = businessPercent;
    }

    public Double getBusinessAmount() {
        return businessAmount;
    }

    public void setBusinessAmount(Double businessAmount) {
        this.businessAmount = businessAmount;
    }


    // =========================
    // EMERGENCY FUND
    // =========================

    public Integer getEmergencyPercent() {
        return emergencyPercent;
    }

    public void setEmergencyPercent(Integer emergencyPercent) {
        this.emergencyPercent = emergencyPercent;
    }

    public Double getEmergencyAmount() {
        return emergencyAmount;
    }

    public void setEmergencyAmount(Double emergencyAmount) {
        this.emergencyAmount = emergencyAmount;
    }


    // =========================
    // EDUCATION FUND
    // =========================

    public Integer getEducationPercent() {
        return educationPercent;
    }

    public void setEducationPercent(Integer educationPercent) {
        this.educationPercent = educationPercent;
    }

    public Double getEducationAmount() {
        return educationAmount;
    }

    public void setEducationAmount(Double educationAmount) {
        this.educationAmount = educationAmount;
    }


    // =========================
    // WELFARE FUND
    // =========================

    public Integer getWelfarePercent() {
        return welfarePercent;
    }

    public void setWelfarePercent(Integer welfarePercent) {
        this.welfarePercent = welfarePercent;
    }

    public Double getWelfareAmount() {
        return welfareAmount;
    }

    public void setWelfareAmount(Double welfareAmount) {
        this.welfareAmount = welfareAmount;
    }


    // =========================
    // ADMINISTRATION FUND
    // =========================

    public Integer getAdministrationPercent() {
        return administrationPercent;
    }

    public void setAdministrationPercent(Integer administrationPercent) {
        this.administrationPercent = administrationPercent;
    }

    public Double getAdministrationAmount() {
        return administrationAmount;
    }

    public void setAdministrationAmount(Double administrationAmount) {
        this.administrationAmount = administrationAmount;
    }


    // =========================
    // REMARKS
    // =========================

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}