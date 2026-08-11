package com.fundsphere.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fundsphere.erp.service.SystemResetService;

@Controller
public class SystemResetController {

    @Autowired
    private SystemResetService systemResetService;


    // =========================================
    // FACTORY RESET
    // =========================================

    @PostMapping("/system/factory-reset")
    public String factoryReset(
            RedirectAttributes redirectAttributes) {

        try {

            systemResetService.factoryReset();

            redirectAttributes.addFlashAttribute(
                    "resetSuccess",
                    "Factory Reset completed successfully. "
                    + "FundSphere ERP is now in a fresh state."
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "resetError",
                    "Factory Reset failed: "
                    + e.getMessage()
            );
        }

        return "redirect:/settings";
    }

}