package com.fundsphere.erp.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fundsphere.erp.entity.Member;
import com.fundsphere.erp.repository.MemberRepository;

@Controller
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // =====================================================
    // MEMBERS LIST
    // =====================================================

    @GetMapping("/members")
    public String members(Model model) {

        model.addAttribute(
                "members",
                memberRepository.findAll()
        );

        model.addAttribute(
                "totalMembers",
                memberRepository.count()
        );

        return "members";
    }


    // =====================================================
    // ADD MEMBER FORM
    // =====================================================

    @GetMapping("/member-form")
    public String memberForm(Model model) {

        model.addAttribute(
                "member",
                new Member()
        );

        return "member-form";
    }


    // =====================================================
    // SAVE MEMBER
    // =====================================================

    @PostMapping("/members/save")
    public String saveMember(
            Member member,
            @RequestParam("photo")
            MultipartFile photo) throws IOException {


        /*
         * NEW MEMBER
         */

        if (member.getId() == null) {

            long nextId =
                    memberRepository.count() + 1;

            member.setMemberCode(
                    String.format(
                            "FSP-%06d",
                            nextId
                    )
            );

            member.setCreatedDate(
                    LocalDate.now()
            );


            /*
             * New member MUST have password.
             */

            if (member.getLoginPassword() == null
                    || member.getLoginPassword().isBlank()) {

                return "redirect:/member-form?passwordRequired=true";
            }

        }


        /*
         * PASSWORD
         *
         * Only encode when a new password
         * has actually been entered.
         */

        if (member.getLoginPassword() != null
                && !member.getLoginPassword().isBlank()) {

            member.setPasswordHash(
                    passwordEncoder.encode(
                            member.getLoginPassword()
                    )
            );
        }


        /*
         * UPDATED DATE
         */

        member.setUpdatedDate(
                LocalDate.now()
        );


        // =================================================
        // MEMBER PHOTO
        // =================================================

        if (photo != null && !photo.isEmpty()) {

            String uploadDir =
                    System.getProperty("user.dir")
                    + File.separator
                    + "src"
                    + File.separator
                    + "main"
                    + File.separator
                    + "resources"
                    + File.separator
                    + "static"
                    + File.separator
                    + "uploads";


            File folder =
                    new File(uploadDir);


            if (!folder.exists()) {
                folder.mkdirs();
            }


            String fileName =
                    UUID.randomUUID()
                    + "_"
                    + photo.getOriginalFilename();


            File destination =
                    new File(
                            folder,
                            fileName
                    );


            System.out.println(
                    "Saving To: "
                    + destination.getAbsolutePath()
            );


            photo.transferTo(
                    destination
            );


            member.setPhotoPath(
                    fileName
            );
        }


        memberRepository.save(member);


        return "redirect:/members";
    }


    // =====================================================
    // VIEW MEMBER
    // =====================================================

    @GetMapping("/members/view/{id}")
    public String viewMember(
            @PathVariable Long id,
            Model model) {

        Member member =
                memberRepository
                        .findById(id)
                        .orElse(null);

        model.addAttribute(
                "member",
                member
        );

        return "member-view";
    }


    // =====================================================
    // EDIT MEMBER
    // =====================================================

    @GetMapping("/members/edit/{id}")
    public String editMember(
            @PathVariable Long id,
            Model model) {

        Member member =
                memberRepository
                        .findById(id)
                        .orElse(null);

        model.addAttribute(
                "member",
                member
        );

        return "member-form";
    }


    // =====================================================
    // DELETE MEMBER
    // =====================================================

    @GetMapping("/members/delete/{id}")
    public String deleteMember(
            @PathVariable Long id) {

        memberRepository.deleteById(id);

        return "redirect:/members";
    }

}