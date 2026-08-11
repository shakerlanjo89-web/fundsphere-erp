package com.fundsphere.erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fundsphere.erp.entity.AppUser;
import com.fundsphere.erp.service.AppUserService;

@Controller
public class UserManagementController {


    @Autowired
    private AppUserService appUserService;


    // =====================================================
    // USER MANAGEMENT
    // =====================================================

    @GetMapping("/user-management")
    public String userManagement(
            Model model,
            Authentication authentication) {


        List<AppUser> users =
                appUserService.findAll();


        model.addAttribute(
                "users",
                users
        );


        model.addAttribute(
                "currentUsername",
                authentication != null
                        ? authentication.getName()
                        : ""
        );


        return "user-management";
    }


    // =====================================================
    // ADD USER FORM
    // =====================================================

    @GetMapping("/user-management/new")
    public String newUser(
            Model model) {


        AppUser user =
                new AppUser();


        user.setActive(true);

        user.setRole(
                "DATA_ENTRY_OPERATOR"
        );


        model.addAttribute(
                "appUser",
                user
        );


        model.addAttribute(
                "pageTitle",
                "Add User"
        );


        model.addAttribute(
                "editMode",
                false
        );


        return "user-form";
    }


    // =====================================================
    // SAVE USER
    // =====================================================

    @PostMapping("/user-management/save")
    public String saveUser(

            @ModelAttribute("appUser")
            AppUser appUser,

            @RequestParam(
                    name = "password",
                    required = false
            )
            String password,

            Model model) {


        try {

            if (appUser.getId() == null) {

                appUserService.save(
                        appUser,
                        password
                );

            } else {

                appUserService.update(
                        appUser,
                        password
                );
            }


            return "redirect:/user-management";


        } catch (IllegalArgumentException e) {


            model.addAttribute(
                    "appUser",
                    appUser
            );


            model.addAttribute(
                    "errorMessage",
                    e.getMessage()
            );


            model.addAttribute(
                    "pageTitle",
                    appUser.getId() == null
                            ? "Add User"
                            : "Edit User"
            );


            model.addAttribute(
                    "editMode",
                    appUser.getId() != null
            );


            return "user-form";
        }
    }


    // =====================================================
    // VIEW USER
    // =====================================================

    @GetMapping(
            "/user-management/view/{id}"
    )
    public String viewUser(

            @PathVariable Long id,

            Model model) {


        AppUser user =
                appUserService.findById(id);


        if (user == null) {

            return "redirect:/user-management";
        }


        model.addAttribute(
                "appUser",
                user
        );


        return "user-view";
    }


    // =====================================================
    // EDIT USER
    // =====================================================

    @GetMapping(
            "/user-management/edit/{id}"
    )
    public String editUser(

            @PathVariable Long id,

            Model model) {


        AppUser user =
                appUserService.findById(id);


        if (user == null) {

            return "redirect:/user-management";
        }


        model.addAttribute(
                "appUser",
                user
        );


        model.addAttribute(
                "pageTitle",
                "Edit User"
        );


        model.addAttribute(
                "editMode",
                true
        );


        return "user-form";
    }


    // =====================================================
    // ACTIVATE USER
    // =====================================================

    @GetMapping(
            "/user-management/activate/{id}"
    )
    public String activateUser(
            @PathVariable Long id) {


        appUserService.setActive(
                id,
                true
        );


        return "redirect:/user-management";
    }


    // =====================================================
    // DEACTIVATE USER
    // =====================================================

    @GetMapping(
            "/user-management/deactivate/{id}"
    )
    public String deactivateUser(
            @PathVariable Long id) {


        appUserService.setActive(
                id,
                false
        );


        return "redirect:/user-management";
    }


    // =====================================================
    // DELETE USER
    // =====================================================

    @GetMapping(
            "/user-management/delete/{id}"
    )
    public String deleteUser(
            @PathVariable Long id) {


        appUserService.delete(
                id
        );


        return "redirect:/user-management";
    }
}