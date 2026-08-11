package com.fundsphere.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.fundsphere.erp.entity.Setting;
import com.fundsphere.erp.service.SettingService;

@ControllerAdvice
public class GlobalModelControllerAdvice {

    @Autowired
    private SettingService settingService;


    @ModelAttribute
    public void addOrganizationInformation(
            org.springframework.ui.Model model) {

        try {

            Setting setting =
                    settingService.getSettings();

            model.addAttribute(
                    "organizationName",
                    safe(setting.getOrganizationName())
            );

            model.addAttribute(
                    "organizationAddress",
                    safe(setting.getAddress())
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
                    "organizationLogo",
                    safe(setting.getLogoPath())
            );

            model.addAttribute(
                    "organizationCurrency",
                    setting.getCurrency() != null
                            ? setting.getCurrency()
                            : "Rs."
            );

            model.addAttribute(
                    "organizationDateFormat",
                    setting.getDateFormat() != null
                            ? setting.getDateFormat()
                            : "yyyy-MM-dd"
            );

        } catch (Exception e) {

            // Safe defaults
            model.addAttribute(
                    "organizationName",
                    "FundSphere ERP"
            );

            model.addAttribute(
                    "organizationAddress",
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
                    "organizationLogo",
                    ""
            );

            model.addAttribute(
                    "organizationCurrency",
                    "Rs."
            );

            model.addAttribute(
                    "organizationDateFormat",
                    "yyyy-MM-dd"
            );
        }
    }


    private String safe(String value) {

        return value != null
                ? value
                : "";
    }
}