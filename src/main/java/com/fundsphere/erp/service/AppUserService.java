package com.fundsphere.erp.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fundsphere.erp.entity.AppUser;
import com.fundsphere.erp.repository.AppUserRepository;

@Service
public class AppUserService
        implements UserDetailsService {


    @Autowired
    private AppUserRepository appUserRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;


    // =====================================================
    // GET ALL USERS
    // =====================================================

    public List<AppUser> findAll() {

        return appUserRepository
                .findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                AppUser::getId,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )
                )
                .toList();
    }


    // =====================================================
    // FIND BY ID
    // =====================================================

    public AppUser findById(Long id) {

        if (id == null) {
            return null;
        }

        return appUserRepository
                .findById(id)
                .orElse(null);
    }


    // =====================================================
    // FIND BY USERNAME
    // =====================================================

    public AppUser findByUsername(
            String username) {

        if (username == null
                || username.trim().isEmpty()) {

            return null;
        }

        return appUserRepository
                .findByUsernameIgnoreCase(
                        username.trim()
                )
                .orElse(null);
    }


    // =====================================================
    // SAVE NEW USER
    // =====================================================

    public AppUser save(
            AppUser appUser,
            String password) {

        validateUser(
                appUser,
                password,
                false
        );


        String username =
                appUser.getUsername()
                        .trim();


        appUser.setUsername(
                username
        );


        appUser.setFullName(
                safe(appUser.getFullName())
        );


        appUser.setEmail(
                safe(appUser.getEmail())
        );


        appUser.setRole(
                normalizeRole(
                        appUser.getRole()
                )
        );


        if (appUser.getActive() == null) {

            appUser.setActive(true);
        }


        appUser.setPasswordHash(
                passwordEncoder.encode(
                        password
                )
        );


        return appUserRepository.save(
                appUser
        );
    }


    // =====================================================
    // UPDATE USER
    // =====================================================

    public AppUser update(
            AppUser appUser,
            String password) {

        if (appUser == null
                || appUser.getId() == null) {

            throw new IllegalArgumentException(
                    "Invalid user."
            );
        }


        AppUser existing =
                findById(
                        appUser.getId()
                );


        if (existing == null) {

            throw new IllegalArgumentException(
                    "User not found."
            );
        }


        validateUser(
                appUser,
                password,
                true
        );


        existing.setUsername(
                appUser.getUsername()
                        .trim()
        );


        existing.setFullName(
                safe(appUser.getFullName())
        );


        existing.setEmail(
                safe(appUser.getEmail())
        );


        existing.setRole(
                normalizeRole(
                        appUser.getRole()
                )
        );


        existing.setActive(
                appUser.getActive() != null
                        ? appUser.getActive()
                        : true
        );


        /*
         * Password is optional during edit.
         *
         * If password is blank:
         * old password remains unchanged.
         */

        if (password != null
                && !password.trim().isEmpty()) {

            existing.setPasswordHash(
                    passwordEncoder.encode(
                            password
                    )
            );
        }


        return appUserRepository.save(
                existing
        );
    }


    // =====================================================
    // DELETE USER
    // =====================================================

    public void delete(Long id) {

        AppUser user =
                findById(id);


        if (user == null) {

            throw new IllegalArgumentException(
                    "User not found."
            );
        }


        appUserRepository.delete(
                user
        );
    }


    // =====================================================
    // ACTIVATE / DEACTIVATE
    // =====================================================

    public void setActive(
            Long id,
            boolean active) {

        AppUser user =
                findById(id);


        if (user == null) {

            throw new IllegalArgumentException(
                    "User not found."
            );
        }


        user.setActive(
                active
        );


        appUserRepository.save(
                user
        );
    }


    // =====================================================
    // SPRING SECURITY LOGIN
    // =====================================================

    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {


        /*
         * Existing default admin is preserved.
         *
         * admin / admin123
         */

        if (username != null
                && username.trim()
                    .equalsIgnoreCase("admin")) {

            return User
                    .withUsername("admin")
                    .password(
                            passwordEncoder.encode(
                                    "admin123"
                            )
                    )
                    .roles("ADMIN")
                    .build();
        }


        AppUser appUser =
                appUserRepository
                        .findByUsernameIgnoreCase(
                                username.trim()
                        )
                        .orElseThrow(
                                () ->
                                    new UsernameNotFoundException(
                                        "User not found."
                                    )
                        );


        if (appUser.getActive() == null
                || !appUser.getActive()) {

            throw new UsernameNotFoundException(
                    "User account is inactive."
            );
        }


        String role =
                normalizeRole(
                        appUser.getRole()
                );


        return User
                .withUsername(
                        appUser.getUsername()
                )
                .password(
                        appUser.getPasswordHash()
                )
                .roles(role)
                .build();
    }


    // =====================================================
    // VALIDATION
    // =====================================================

    private void validateUser(
            AppUser user,
            String password,
            boolean editing) {


        if (user == null) {

            throw new IllegalArgumentException(
                    "User information is required."
            );
        }


        if (user.getUsername() == null
                || user.getUsername()
                    .trim()
                    .isEmpty()) {

            throw new IllegalArgumentException(
                    "Username is required."
            );
        }


        if (user.getFullName() == null
                || user.getFullName()
                    .trim()
                    .isEmpty()) {

            throw new IllegalArgumentException(
                    "Full name is required."
            );
        }


        if (user.getRole() == null
                || user.getRole()
                    .trim()
                    .isEmpty()) {

            throw new IllegalArgumentException(
                    "Role is required."
            );
        }


        String username =
                user.getUsername()
                        .trim();


        boolean usernameExists;


        if (editing) {

            usernameExists =
                    appUserRepository
                        .existsByUsernameIgnoreCaseAndIdNot(
                                username,
                                user.getId()
                        );

        } else {

            usernameExists =
                    appUserRepository
                        .existsByUsernameIgnoreCase(
                                username
                        );
        }


        /*
         * admin is reserved for the existing
         * built-in administrator account.
         */

        if ("admin".equalsIgnoreCase(username)) {

            throw new IllegalArgumentException(
                    "Username 'admin' is reserved for the system administrator."
            );
        }


        if (usernameExists) {

            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }


        if (!editing) {

            if (password == null
                    || password.trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Password is required."
                );
            }


            if (password.length() < 6) {

                throw new IllegalArgumentException(
                        "Password must contain at least 6 characters."
                );
            }
        }


        if (editing
                && password != null
                && !password.trim().isEmpty()
                && password.length() < 6) {

            throw new IllegalArgumentException(
                    "Password must contain at least 6 characters."
            );
        }


        String role =
                normalizeRole(
                        user.getRole()
                );


        if (!isValidRole(role)) {

            throw new IllegalArgumentException(
                    "Invalid user role."
            );
        }
    }


    // =====================================================
    // ROLE NORMALIZATION
    // =====================================================

    private String normalizeRole(
            String role) {

        if (role == null
                || role.trim().isEmpty()) {

            return "DATA_ENTRY_OPERATOR";
        }


        return role
                .trim()
                .toUpperCase()
                .replace(
                        " ",
                        "_"
                )
                .replace(
                        "-",
                        "_"
                );
    }


    // =====================================================
    // VALID ROLE
    // =====================================================

    private boolean isValidRole(
            String role) {

        return
                "SUPER_ADMIN".equals(role)
                ||
                "ADMIN".equals(role)
                ||
                "ACCOUNTANT".equals(role)
                ||
                "DATA_ENTRY_OPERATOR".equals(role)
                ||
                "COMMITTEE_MEMBER".equals(role)
                ||
                "MEMBER".equals(role);
    }


    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(
            String value) {

        return value != null
                ? value.trim()
                : "";
    }
}