package com.fundsphere.erp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "settings")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================
    // ORGANIZATION INFORMATION
    // =========================================

    private String organizationName;

    private String shortName;

    private String tagline;

    private String address;

    private String district;

    private String province;

    private String country;

    private String phone;

    private String email;

    private String website;

    private String registrationNo;

    private String logoPath;


    // =========================================
    // SYSTEM SETTINGS
    // =========================================

    private String currency;

    private String dateFormat;


    // =========================================
    // FUND PERCENTAGES
    // =========================================

    private Integer businessPercent;

    private Integer emergencyPercent;

    private Integer educationPercent;

    private Integer welfarePercent;

    private Integer administrationPercent;


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public Setting() {
    }


    // =========================================
    // ID
    // =========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // =========================================
    // ORGANIZATION NAME
    // =========================================

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(
            String organizationName) {

        this.organizationName =
                organizationName;
    }


    // =========================================
    // SHORT NAME
    // =========================================

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    // =========================================
    // TAGLINE
    // =========================================

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }


    // =========================================
    // ADDRESS
    // =========================================

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    // =========================================
    // DISTRICT
    // =========================================

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


    // =========================================
    // PROVINCE
    // =========================================

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }


    // =========================================
    // COUNTRY
    // =========================================

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    // =========================================
    // PHONE
    // =========================================

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    // =========================================
    // EMAIL
    // =========================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // =========================================
    // WEBSITE
    // =========================================

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    // =========================================
    // REGISTRATION NUMBER
    // =========================================

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(
            String registrationNo) {

        this.registrationNo =
                registrationNo;
    }


    // =========================================
    // LOGO PATH
    // =========================================

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }


    // =========================================
    // CURRENCY
    // =========================================

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    // =========================================
    // DATE FORMAT
    // =========================================

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }


    // =========================================
    // BUSINESS %
    // =========================================

    public Integer getBusinessPercent() {
        return businessPercent;
    }

    public void setBusinessPercent(
            Integer businessPercent) {

        this.businessPercent =
                businessPercent;
    }


    // =========================================
    // EMERGENCY %
    // =========================================

    public Integer getEmergencyPercent() {
        return emergencyPercent;
    }

    public void setEmergencyPercent(
            Integer emergencyPercent) {

        this.emergencyPercent =
                emergencyPercent;
    }


    // =========================================
    // EDUCATION %
    // =========================================

    public Integer getEducationPercent() {
        return educationPercent;
    }

    public void setEducationPercent(
            Integer educationPercent) {

        this.educationPercent =
                educationPercent;
    }


    // =========================================
    // WELFARE %
    // =========================================

    public Integer getWelfarePercent() {
        return welfarePercent;
    }

    public void setWelfarePercent(
            Integer welfarePercent) {

        this.welfarePercent =
                welfarePercent;
    }


    // =========================================
    // ADMINISTRATION %
    // =========================================

    public Integer getAdministrationPercent() {
        return administrationPercent;
    }

    public void setAdministrationPercent(
            Integer administrationPercent) {

        this.administrationPercent =
                administrationPercent;
    }

}