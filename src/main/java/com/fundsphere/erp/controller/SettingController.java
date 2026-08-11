package com.fundsphere.erp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fundsphere.erp.entity.Setting;
import com.fundsphere.erp.service.SettingService;

@Controller
public class SettingController
        implements WebMvcConfigurer {


    @Autowired
    private SettingService settingService;


    // =========================================
    // SETTINGS PAGE
    // =========================================

    @GetMapping("/settings")
    public String settings(Model model) {

        Setting setting =
                settingService.getSettings();


        if (setting == null) {

            setting = new Setting();
        }


        model.addAttribute(
                "setting",
                setting
        );


        return "settings";
    }


    // =========================================
    // SAVE SETTINGS
    // =========================================

    @PostMapping("/settings/save")
    public String saveSettings(

            @ModelAttribute("setting")
            Setting setting,

            @RequestParam(
                    value = "logoFile",
                    required = false
            )
            MultipartFile logoFile,

            Model model) {


        try {


            // =========================================
            // FUND PERCENTAGE VALIDATION
            // =========================================

            settingService
                    .validateFundPercentages(
                            setting
                    );


            // =========================================
            // LOGO UPLOAD
            // =========================================

            if (logoFile != null
                    && !logoFile.isEmpty()) {


                String originalFileName =
                        logoFile.getOriginalFilename();


                String extension = "";


                if (originalFileName != null
                        && originalFileName.contains(".")) {

                    extension =
                            originalFileName
                                    .substring(
                                            originalFileName
                                                    .lastIndexOf(".")
                                    )
                                    .toLowerCase();
                }


                String newFileName =
                        UUID.randomUUID()
                                .toString()
                                + extension;


                Path uploadDirectory =
                        Paths.get("uploads");


                if (!Files.exists(
                        uploadDirectory)) {

                    Files.createDirectories(
                            uploadDirectory
                    );
                }


                Path targetPath =
                        uploadDirectory.resolve(
                                newFileName
                        );


                Files.copy(
                        logoFile.getInputStream(),
                        targetPath,
                        StandardCopyOption.REPLACE_EXISTING
                );


                setting.setLogoPath(
                        "/uploads/"
                        + newFileName
                );
            }


            // =========================================
            // SAVE SETTINGS
            // =========================================

            settingService
                    .saveSettings(setting);


            return "redirect:/settings?saved=true";


        } catch (IllegalArgumentException e) {


            model.addAttribute(
                    "setting",
                    setting
            );


            model.addAttribute(
                    "errorMessage",
                    e.getMessage()
            );


            return "settings";


        } catch (IOException e) {


            model.addAttribute(
                    "setting",
                    setting
            );


            model.addAttribute(
                    "errorMessage",
                    "Logo upload failed: "
                    + e.getMessage()
            );


            return "settings";


        } catch (Exception e) {


            model.addAttribute(
                    "setting",
                    setting
            );


            model.addAttribute(
                    "errorMessage",
                    "Settings save failed: "
                    + e.getMessage()
            );


            return "settings";
        }
    }


    // =========================================
    // SERVE UPLOADED LOGOS
    // =========================================

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry) {


        registry
                .addResourceHandler(
                        "/uploads/**"
                )
                .addResourceLocations(
                        "file:uploads/"
                );
    }


    // =========================================
    // FACTORY RESET
    // =========================================

    @PostMapping("/settings/factory-reset")
    public String factoryReset(
            Model model) {


        try {


            settingService.factoryReset();


            return "redirect:/settings?reset=true";


        } catch (Exception e) {


            model.addAttribute(
                    "setting",
                    settingService.getSettings()
            );


            model.addAttribute(
                    "errorMessage",
                    "Factory Reset failed: "
                    + e.getMessage()
            );


            return "settings";
        }
    }
}