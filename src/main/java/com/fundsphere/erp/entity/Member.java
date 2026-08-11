package com.fundsphere.erp.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_code", unique = true, nullable = false)
    private String memberCode;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "cnic", unique = true, nullable = false)
    private String cnic;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "whatsapp_number")
    private String whatsappNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "district")
    private String district;

    @Column(name = "taluka")
    private String taluka;

    @Column(name = "village")
    private String village;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "membership_status")
    private String membershipStatus;

    @Column(name = "monthly_contribution")
    private Double monthlyContribution;

    @Column(name = "business_fund_percent")
    private Integer businessFundPercent;

    @Column(name = "emergency_fund_percent")
    private Integer emergencyFundPercent;

    @Column(name = "administration_fund_percent")
    private Integer administrationFundPercent;

    @Column(name = "nominee_name")
    private String nomineeName;

    @Column(name = "nominee_relationship")
    private String nomineeRelationship;

    @Column(name = "nominee_mobile")
    private String nomineeMobile;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "cnic_front_path")
    private String cnicFrontPath;

    @Column(name = "cnic_back_path")
    private String cnicBackPath;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "education")
    private String education;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    /*
     * Password is stored in encrypted form.
     */
    @Column(name = "password_hash")
    private String passwordHash;

    /*
     * This field is NOT stored in database.
     * It is only used by Add/Edit Member form.
     */
    @Transient
    private String loginPassword;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Member() {
    }


    // =====================================================
    // GETTERS / SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }


    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }


    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }


    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }


    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }


    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }


    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }


    public String getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(String membershipStatus) {
        this.membershipStatus = membershipStatus;
    }


    public Double getMonthlyContribution() {
        return monthlyContribution;
    }

    public void setMonthlyContribution(Double monthlyContribution) {
        this.monthlyContribution = monthlyContribution;
    }


    public Integer getBusinessFundPercent() {
        return businessFundPercent;
    }

    public void setBusinessFundPercent(Integer businessFundPercent) {
        this.businessFundPercent = businessFundPercent;
    }


    public Integer getEmergencyFundPercent() {
        return emergencyFundPercent;
    }

    public void setEmergencyFundPercent(Integer emergencyFundPercent) {
        this.emergencyFundPercent = emergencyFundPercent;
    }


    public Integer getAdministrationFundPercent() {
        return administrationFundPercent;
    }

    public void setAdministrationFundPercent(Integer administrationFundPercent) {
        this.administrationFundPercent = administrationFundPercent;
    }


    public String getNomineeName() {
        return nomineeName;
    }

    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }


    public String getNomineeRelationship() {
        return nomineeRelationship;
    }

    public void setNomineeRelationship(String nomineeRelationship) {
        this.nomineeRelationship = nomineeRelationship;
    }


    public String getNomineeMobile() {
        return nomineeMobile;
    }

    public void setNomineeMobile(String nomineeMobile) {
        this.nomineeMobile = nomineeMobile;
    }


    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }


    public String getCnicFrontPath() {
        return cnicFrontPath;
    }

    public void setCnicFrontPath(String cnicFrontPath) {
        this.cnicFrontPath = cnicFrontPath;
    }


    public String getCnicBackPath() {
        return cnicBackPath;
    }

    public void setCnicBackPath(String cnicBackPath) {
        this.cnicBackPath = cnicBackPath;
    }


    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }


    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }


    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }


    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

}