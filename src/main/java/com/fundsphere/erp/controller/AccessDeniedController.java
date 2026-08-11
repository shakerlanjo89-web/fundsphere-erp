package com.fundsphere.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    // =====================================================
    // ACCESS DENIED
    // =====================================================

    @GetMapping("/access-denied")
    public String accessDenied() {

        return "access-denied";
    }

}